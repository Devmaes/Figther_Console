package cm.fighterconsole;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Presentation and input validation are intentionally separate from the combat rules. */
public final class Terminal {
    private static final int WIDTH = 73;
    private final BufferedReader input;
    private final PrintStream out;
    private final boolean ansi;

    public Terminal(boolean ansi) {
        this(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)), System.out, ansi);
    }
    public Terminal(BufferedReader input, PrintStream out, boolean ansi) {
        this.input = input;
        this.out = out;
        this.ansi = ansi;
    }

    private String accent(String content) { return ansi ? "\u001b[38;5;214m" + content + "\u001b[0m" : content; }
    private String green(String content) { return ansi ? "\u001b[38;5;48m" + content + "\u001b[0m" : content; }

    public void clear() {
        if (ansi) out.print("\u001b[2J\u001b[H");
        else out.println("\n".repeat(2));
    }
    public void banner() {
        out.println(accent("╔" + "═".repeat(WIDTH - 2) + "╗"));
        out.println(accent("║") + center("F I G H T E R    C O N S O L E   //   V 2 . 0", WIDTH - 2) + accent("║"));
        out.println(accent("║") + center("STRATÉGIE • CONTRE-ATTAQUE • SURVIE   |   DEVMAES", WIDTH - 2) + accent("║"));
        out.println(accent("╚" + "═".repeat(WIDTH - 2) + "╝"));
    }
    public void menu(boolean musicEnabled, boolean dbActive) {
        clear(); banner();
        out.println("\n  [1]  ⚔  SOLO         Affronter une IA tactique");
        out.println("  [2]  ⚔  DUEL         Deux joueurs sur le même terminal");
        out.println("  [3]  ★  TOP 5        Classement local des meilleurs scores");
        out.println("  [4]  ?  RÈGLES       Comprendre les échanges et les points");
        out.println("  [5]  ♪  MUSIQUE      " + (musicEnabled ? "activée" : "désactivée"));
        out.println("  [0]  ⏻  QUITTER");
        out.println("\n  Base locale : " + (dbActive ? green("SQLite prête") : "indisponible (classement désactivé)"));
        out.println(accent("─".repeat(WIDTH)));
    }
    public void heading(String text) {
        out.println("\n" + accent("━━  " + text.toUpperCase() + "  " + "━".repeat(Math.max(4, 66 - text.length()))));
    }
    public void message(String text) { out.println("  " + text); }
    public void line() { out.println(accent("─".repeat(WIDTH))); }

    public void stats(Combat.Fighter first, Combat.Fighter second, int round) {
        heading("Échange " + round);
        out.printf("  %-35s %s%n", first.name(), second.name());
        out.printf("  %-35s %s%n", "VIE   " + bar(first.life(), Combat.MAX_LIFE, '♥'),
                "VIE   " + bar(second.life(), Combat.MAX_LIFE, '♥'));
        out.printf("  %-35s %s%n", "ATQ   " + bar(first.attack(), Combat.MAX_ATTACK, '▲'),
                "ATQ   " + bar(second.attack(), Combat.MAX_ATTACK, '▲'));
        out.printf("  %-35s %s%n", "DEF   " + bar(first.defense(), Combat.MAX_DEFENSE, '■'),
                "DEF   " + bar(second.defense(), Combat.MAX_DEFENSE, '■'));
        line();
    }
    static String bar(int value, int max, char symbol) {
        int count = (int) Math.round(8.0 * value / max);
        return String.valueOf(symbol).repeat(count) + "·".repeat(8 - count) + " " + value;
    }

    public int number(String question, int min, int max) throws IOException {
        while (true) {
            out.printf("  %s %s", question, accent("➜ "));
            out.flush();
            String raw = input.readLine();
            if (raw == null) throw new EOFException("Entrée fermée");
            try {
                int value = Integer.parseInt(raw.trim());
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException ignored) {}
            message("Choisir un nombre entre " + min + " et " + max + ".");
        }
    }

    public String nickname(String label) throws IOException {
        while (true) {
            out.print("  " + label + " ➜ "); out.flush();
            String raw = input.readLine();
            if (raw == null) throw new EOFException("Entrée fermée");
            String candidate = raw.strip().replaceAll("[\\p{Cntrl}\\u001b]", "").replaceAll("\\s+", " ");
            if (!candidate.isBlank() && candidate.length() <= 24) return candidate;
            message("Pseudo requis (1 à 24 caractères).");
        }
    }

    public void leaderboard(List<Scores.Entry> ranks) {
        heading("Les 5 meilleurs scores");
        if (ranks.isEmpty()) { message("Aucun combat remporté pour le moment."); return; }
        out.printf("  %-4s %-21s %-8s %-8s %-7s %s%n", "#", "PSEUDO", "SCORE", "MODE", "TOURS", "ADVERSAIRE");
        line();
        for (int i = 0; i < ranks.size(); i++) {
            Scores.Entry e = ranks.get(i);
            out.printf("  %-4d %-21s %-8d %-8s %-7d %s%n", i + 1, cut(e.player(), 20),
                e.score(), e.mode(), e.rounds(), cut(e.opponent(), 15));
        }
    }

    public void rules() {
        heading("Règles officielles — Fighter Console V2");
        message("Chaque combattant commence avec 3 vies, 15 ATQ et 7 DEF.");
        message("1. Le joueur actif choisit une puissance d'ATTAQUE (1 à son ATQ)");
        message("   ou reprend son souffle (+3 ATQ, +1 DEF, plafonds 20/12).");
        message("2. Face à une attaque, l'adversaire DOIT réagir avant tout calcul :");
        message("   [1] BLOQUER : dépense de DEF, atténue l'impact ; +1 ATQ.");
        message("       Si blocage total de puissance ≥5 : +1 vie, maximum 3.");
        message("   [2] CONTRE-ATTAQUER : dépense de l'ATQ et confronte les coups.");
        message("   [3] ENCAISSER : aucune dépense volontaire ; l'attaque non");
        message("       bloquée réduit DEF et ATQ de l'adversaire.");
        message("3. Résolution après réaction : si l'impact résiduel ≥4,");
        message("   la cible perd 1 vie. La différence des contre-attaques");
        message("   ≥4 fait perdre 1 vie au plus faible ; sinon les deux coups");
        message("   d’au moins 4 peuvent atteindre leur adversaire.");
        message("4. Aucun point n'est modifié avant le choix de la réaction.");
        message("5. La partie se termine dès qu'un joueur atteint 0 vie.");
        message("   Le gagnant entre au classement local des cinq meilleurs");
        message("   scores calculés selon vies, ressources et efficacité.");
        message("   Tous les détails : docs/REGLES.md.");
    }
    public void goodbye() { message("Merci d'avoir joué — Developed by Devmaes."); }
    private static String cut(String s, int max) { return s.length() > max ? s.substring(0, max - 1) + "…" : s; }
    private static String center(String s, int width) {
        int pad = Math.max(0, (width - s.length()) / 2);
        return " ".repeat(pad) + s + " ".repeat(Math.max(0, width - pad - s.length()));
    }
}
