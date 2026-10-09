package cm.fighterconsole;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** One-ply tactical search: enumerate legal reactions and compare resource-adjusted outcomes. */
public final class CombatAI {
    private final Random random;
    public CombatAI(Random random) { this.random = random; }

    public record Opening(boolean rest, int power) {}
    private static final int HIT_MARGIN = Combat.HIT_THRESHOLD;

    public Combat.Reaction chooseReaction(Combat.Fighter attacker, Combat.Fighter computer, int strike) {
        List<Combat.Reaction> options = legalReactions(computer);
        Combat.Reaction best = Combat.Reaction.take();
        double bestValue = Double.NEGATIVE_INFINITY;
        for (Combat.Reaction option : options) {
            Combat.Outcome result = Combat.resolve(attacker, computer, strike, option);
            double value = value(result.defender(), result.attacker());
            if (result.damageToAttacker() > 0) value += 12;
            if (result.successfulBlock()) value += 3;
            value += random.nextDouble() * 0.06;
            if (value > bestValue) { bestValue = value; best = option; }
        }
        return best;
    }

    public Opening chooseOpening(Combat.Fighter computer, Combat.Fighter human) {
        int pressureStrike = Math.max(4, human.defense() + HIT_MARGIN);
        if (computer.attack() >= pressureStrike && computer.attack() >= 11) {
            return new Opening(false, pressureStrike);
        }
        double restValue = value(Combat.rest(computer, human).attacker(), human)
            - (computer.attack() >= 12 ? 14.0 : 3.0);
        int bestPower = 0;
        double bestValue = restValue;
        for (int power = 1; power <= computer.attack(); power++) {
            // Approximate the opponent's best response; avoid wasting points on a predictable block.
            double worstForComputer = Double.POSITIVE_INFINITY;
            for (Combat.Reaction response : legalReactions(human)) {
                Combat.Outcome outcome = Combat.resolve(computer, human, power, response);
                worstForComputer = Math.min(worstForComputer, value(outcome.attacker(), outcome.defender()));
            }
            // Favor decisive threats without unreasonably spending all attack resources.
            double aggressiveBonus = power >= Combat.HIT_THRESHOLD ? 8.0 : 0.0;
            if (human.life() == 1 && power >= Combat.HIT_THRESHOLD) aggressiveBonus += 4.0;
            double resultValue = worstForComputer + aggressiveBonus + random.nextDouble() * 0.07;
            if (resultValue > bestValue) { bestValue = resultValue; bestPower = power; }
        }
        return bestPower == 0 ? new Opening(true, 0) : new Opening(false, bestPower);
    }

    private static List<Combat.Reaction> legalReactions(Combat.Fighter defender) {
        List<Combat.Reaction> actions = new ArrayList<>();
        actions.add(Combat.Reaction.take());
        for (int n = 1; n <= defender.defense(); n++) {
            actions.add(new Combat.Reaction(Combat.ReactionType.BLOCK, n));
        }
        for (int n = 1; n <= defender.attack(); n++) {
            actions.add(new Combat.Reaction(Combat.ReactionType.COUNTER, n));
        }
        return actions;
    }

    private static double value(Combat.Fighter self, Combat.Fighter enemy) {
        if (self.life() == 0) return -100000;
        if (enemy.life() == 0) return 100000;
        return 110.0 * (self.life() - enemy.life())
            + 2.7 * (self.attack() - enemy.attack())
            + 2.0 * (self.defense() - enemy.defense());
    }
}
