package cm.fighterconsole;

/** Rules engine with no console, audio, random or database dependencies. */
public final class Combat {
    private Combat() {}

    public static final int MAX_LIFE = 3;
    public static final int MAX_ATTACK = 20;
    public static final int MAX_DEFENSE = 12;
    public static final int HIT_THRESHOLD = 4;

    public record Fighter(String name, int life, int attack, int defense) {
        public Fighter {
            if (name == null || name.isBlank()) throw new IllegalArgumentException("Pseudo requis");
            if (life < 0 || life > MAX_LIFE || attack < 0 || attack > MAX_ATTACK || defense < 0 || defense > MAX_DEFENSE)
                throw new IllegalArgumentException("Ressources hors limites");
        }
        public static Fighter starter(String name) { return new Fighter(name, 3, 15, 7); }
        Fighter with(int life, int attack, int defense) {
            return new Fighter(name, cap(life, MAX_LIFE), cap(attack, MAX_ATTACK), cap(defense, MAX_DEFENSE));
        }
        public boolean knockedOut() { return life == 0; }
    }

    public enum ReactionType { BLOCK, COUNTER, TAKE }
    public record Reaction(ReactionType type, int power) {
        public Reaction {
            if (type == null || power < 0 || (type == ReactionType.TAKE && power != 0))
                throw new IllegalArgumentException("Réaction invalide");
        }
        public static Reaction take() { return new Reaction(ReactionType.TAKE, 0); }
    }

    /** Changes are calculated only AFTER the defender has chosen a reaction. */
    public record Outcome(Fighter attacker, Fighter defender, int damageToAttacker,
                          int damageToDefender, boolean successfulBlock, String commentary) {}

    public static Outcome rest(Fighter active, Fighter other) {
        if (active.knockedOut() || other.knockedOut()) throw new IllegalStateException("Combat terminé");
        Fighter rested = active.with(active.life, active.attack + 3, active.defense + 1);
        return new Outcome(rested, other, 0, 0, false, active.name + " reprend son souffle (+3 ATQ, +1 DEF au maximum).");
    }

    public static Outcome resolve(Fighter attacker, Fighter defender, int strike, Reaction response) {
        if (attacker.knockedOut() || defender.knockedOut()) throw new IllegalStateException("Combat terminé");
        if (strike < 1 || strike > attacker.attack) throw new IllegalArgumentException("Attaque impossible");
        if (response.type == ReactionType.BLOCK && (response.power < 1 || response.power > defender.defense))
            throw new IllegalArgumentException("Défense insuffisante");
        if (response.type == ReactionType.COUNTER && (response.power < 1 || response.power > defender.attack))
            throw new IllegalArgumentException("Contre-attaque impossible");

        Fighter afterAttack = attacker.with(attacker.life, attacker.attack - strike, attacker.defense);
        Fighter afterDefense;
        int damageA = 0, damageD = 0;
        boolean successfulBlock = false;
        String detail;

        switch (response.type) {
            case BLOCK -> {
                int remaining = Math.max(0, strike - response.power);
                damageD = remaining >= HIT_THRESHOLD ? 1 : 0;
                successfulBlock = remaining == 0;
                int recoveredLife = successfulBlock && response.power >= 5 ? 1 : 0;
                afterDefense = defender.with(defender.life - damageD + recoveredLife,
                    defender.attack + 1, defender.defense - response.power);
                detail = "Blocage " + response.power + " | impact résiduel " + remaining
                    + (recoveredLife > 0 ? " | récupération de vie" : "");
            }
            case COUNTER -> {
                int difference = strike - response.power;
                if (difference >= HIT_THRESHOLD) {
                    damageD = 1;
                } else if (difference <= -HIT_THRESHOLD) {
                    damageA = 1;
                } else {
                    // Close-range counterattacks trade blows: both strikes can land.
                    damageA = response.power >= HIT_THRESHOLD ? 1 : 0;
                    damageD = strike >= HIT_THRESHOLD ? 1 : 0;
                }
                afterAttack = afterAttack.with(afterAttack.life - damageA, afterAttack.attack, afterAttack.defense);
                afterDefense = defender.with(defender.life - damageD,
                    defender.attack - response.power, defender.defense);
                detail = "Contre-attaque " + response.power + " | différence de puissance " + Math.abs(difference);
            }
            case TAKE -> {
                damageD = strike >= HIT_THRESHOLD || (defender.attack == 0 && defender.defense == 0) ? 1 : 0;
                afterDefense = defender.with(defender.life - damageD,
                    defender.attack - 1, defender.defense - strike);
                detail = "Pas de riposte | pression sur les ressources adverses";
            }
            default -> throw new IllegalStateException("Réaction inconnue");
        }
        String commentary = attacker.name + " attaque (" + strike + "). " + defender.name + " : " + detail
            + " | dégâts " + attacker.name + " -" + damageA + ", " + defender.name + " -" + damageD;
        return new Outcome(afterAttack, afterDefense, damageA, damageD, successfulBlock, commentary);
    }

    private static int cap(int number, int maximum) { return Math.max(0, Math.min(number, maximum)); }
}
