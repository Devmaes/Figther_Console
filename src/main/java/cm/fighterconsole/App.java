package cm.fighterconsole;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Random;

/** Interactive application coordinator. Game engine, AI, I/O, persistence and audio are isolated. */
public final class App {
    private final Terminal terminal;
    private final CombatAI artificialIntelligence;
    private final Soundtrack soundtrack;
    private Scores scores;

    public App(Terminal terminal, CombatAI ai, Soundtrack music, Scores scores) {
        this.terminal = terminal;
        this.artificialIntelligence = ai;
        this.soundtrack = music;
        this.scores = scores;
    }

    public static void main(String[] args) {
        boolean noColor = java.util.Arrays.asList(args).contains("--no-color")
            || System.getenv().containsKey("NO_COLOR") || System.console() == null;
        boolean noSound = java.util.Arrays.asList(args).contains("--no-sound")
            || "1".equals(System.getenv("FIGHTER_NO_SOUND"));
        Path path = Path.of(System.getenv().getOrDefault("FIGHTER_DB_PATH",
            Path.of(System.getProperty("user.home"), ".fighter-console", "scores.db").toString()));
        Scores database = null;
        try { database = new Scores(path); }
        catch (SQLException error) { System.err.println("[Scores] SQLite indisponible : " + error.getMessage()); }
        try (Soundtrack music = new Soundtrack()) {
            music.setEnabled(!noSound);
            new App(new Terminal(!noColor), new CombatAI(new Random()), music, database).run();
        } catch (IOException error) {
            System.err.println("Entrée du terminal fermée : " + error.getMessage());
        }
    }

    public void run() throws IOException {
        while (true) {
            terminal.menu(soundtrack.enabled(), scores != null);
            int command = terminal.number("Choix", 0, 5);
            switch (command) {
                case 1 -> play(true);
                case 2 -> play(false);
                case 3 -> showScores();
                case 4 -> terminal.rules();
                case 5 -> {
                    soundtrack.setEnabled(!soundtrack.enabled());
                    terminal.message("Musique " + (soundtrack.enabled() ? "activée" : "désactivée") + ".");
                }
                case 0 -> { terminal.goodbye(); return; }
                default -> throw new IllegalStateException("Commande non prise en charge");
            }
        }
    }

    private void showScores() {
        if (scores == null) { terminal.message("SQLite indisponible : classement désactivé."); return; }
        try { terminal.leaderboard(scores.topFive()); }
        catch (SQLException error) { terminal.message("Lecture des scores impossible : " + error.getMessage()); }
    }

    private void play(boolean solo) throws IOException {
        terminal.heading(solo ? "Solo — contre ordinateur" : "Duel — deux joueurs");
        Combat.Fighter first = Combat.Fighter.starter(terminal.nickname("Pseudo du joueur 1"));
        Combat.Fighter second;
        if (solo) {
            String[] rivals = {"Rodrigo", "Joselu", "Bellingham", "Camavinga", "Vinicius"};
            for (int i = 0; i < rivals.length; i++) terminal.message("[" + (i + 1) + "] " + rivals[i]);
            int choice = terminal.number("Choisis ton adversaire", 1, rivals.length);
            // Each rival has a recognizable resource profile, still within the global caps.
            int[] bonusAttack = {0, 1, 2, 2, 3};
            int[] bonusDefense = {0, 1, 1, 3, 2};
            second = new Combat.Fighter(rivals[choice - 1], 3,
                15 + bonusAttack[choice - 1], 7 + bonusDefense[choice - 1]);
        } else {
            String rivalName = terminal.nickname("Pseudo du joueur 2");
            while (rivalName.equalsIgnoreCase(first.name())) {
                terminal.message("Utilise un pseudo différent.");
                rivalName = terminal.nickname("Pseudo du joueur 2");
            }
            second = Combat.Fighter.starter(rivalName);
        }

        int active = 0, exchanges = 0, hitsFirst = 0, hitsSecond = 0, blocksFirst = 0, blocksSecond = 0;
        soundtrack.start();
        try {
            while (!first.knockedOut() && !second.knockedOut()) {
                terminal.stats(first, second, exchanges + 1);
                Combat.Fighter attacker = active == 0 ? first : second;
                Combat.Fighter defender = active == 0 ? second : first;
                boolean aiOpens = solo && active == 1;
                CombatAI.Opening opening = aiOpens
                    ? artificialIntelligence.chooseOpening(attacker, defender)
                    : humanOpening(attacker);
                if (opening.rest()) {
                    Combat.Outcome result = Combat.rest(attacker, defender);
                    if (active == 0) first = result.attacker(); else second = result.attacker();
                    terminal.message(result.commentary());
                } else {
                    int attackPower = opening.power();
                    terminal.message(attacker.name() + " annonce une ATTAQUE de puissance " + attackPower + ".");
                    terminal.message("Les points restent INCHANGÉS jusqu'à la réaction.");
                    Combat.Reaction response = solo && active == 0
                        ? artificialIntelligence.chooseReaction(attacker, defender, attackPower)
                        : humanReaction(defender);
                    Combat.Outcome result = Combat.resolve(attacker, defender, attackPower, response);
                    terminal.heading("Résolution de l'échange");
                    terminal.message(result.commentary());
                    if (active == 0) { first = result.attacker(); second = result.defender(); }
                    else { second = result.attacker(); first = result.defender(); }
                    if (result.damageToDefender() > 0) {
                        if (active == 0) hitsFirst++; else hitsSecond++;
                    }
                    if (result.damageToAttacker() > 0) {
                        if (active == 0) hitsSecond++; else hitsFirst++;
                    }
                    if (result.successfulBlock()) {
                        if (active == 0) blocksSecond++; else blocksFirst++;
                    }
                }
                exchanges++;
                active = 1 - active;
            }
        } catch (EOFException interrupted) {
            terminal.message("Partie interrompue : aucun score enregistré.");
            return;
        } finally {
            soundtrack.stop();
        }

        terminal.stats(first, second, exchanges);
        if (first.knockedOut() && second.knockedOut()) {
            terminal.heading("Double K.O. — Match nul");
            terminal.message("Les deux joueurs tombent à zéro vie : aucun score enregistré.");
            return;
        }
        Combat.Fighter winner = first.knockedOut() ? second : first;
        Combat.Fighter loser = first.knockedOut() ? first : second;
        int hitCount = first.knockedOut() ? hitsSecond : hitsFirst;
        int blockCount = first.knockedOut() ? blocksSecond : blocksFirst;
        int score = Scores.calculate(winner, exchanges, hitCount, blockCount);
        terminal.heading("K.O. — Victoire de " + winner.name());
        terminal.message("Score final : " + score + " points en " + exchanges + " échanges.");
        if (scores != null) {
            try {
                scores.save(new Scores.Entry(winner.name(), loser.name(), solo ? "SOLO" : "DUEL",
                    score, exchanges, ""));
                terminal.message("Résultat enregistré dans la base SQLite locale.");
                showScores();
            } catch (SQLException error) {
                terminal.message("Résultat non enregistré : " + error.getMessage());
            }
        }
    }

    private CombatAI.Opening humanOpening(Combat.Fighter player) throws IOException {
        terminal.message("Tour de " + player.name() + " : [1] Attaquer   [2] Reprendre souffle");
        if (player.attack() == 0) {
            terminal.message("Plus de points d'attaque : récupération obligatoire.");
            return new CombatAI.Opening(true, 0);
        }
        int type = terminal.number("Action", 1, 2);
        if (type == 2) return new CombatAI.Opening(true, 0);
        return new CombatAI.Opening(false, terminal.number("Puissance d'attaque", 1, player.attack()));
    }

    private Combat.Reaction humanReaction(Combat.Fighter player) throws IOException {
        terminal.message("Réaction de " + player.name() + " : [1] Bloquer   [2] Contre-attaquer   [3] Encaisser");
        int type = terminal.number("Réaction", 1, 3);
        if (type == 1 && player.defense() > 0) {
            return new Combat.Reaction(Combat.ReactionType.BLOCK,
                terminal.number("Puissance de défense", 1, player.defense()));
        }
        if (type == 2 && player.attack() > 0) {
            return new Combat.Reaction(Combat.ReactionType.COUNTER,
                terminal.number("Puissance de contre-attaque", 1, player.attack()));
        }
        if (type != 3) terminal.message("Ressources insuffisantes : attaque encaissée.");
        return Combat.Reaction.take();
    }
}
