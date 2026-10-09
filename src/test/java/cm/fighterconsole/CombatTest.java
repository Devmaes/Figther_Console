package cm.fighterconsole;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CombatTest {
    @Test void originalValuesArePreserved() {
        Combat.Fighter fighter = Combat.Fighter.starter("Maes");
        assertEquals(3, fighter.life()); assertEquals(15, fighter.attack()); assertEquals(7, fighter.defense());
    }
    @Test void resolutionWaitsForReactionAndDoesNotMutateInput() {
        var first = Combat.Fighter.starter("A");
        var second = Combat.Fighter.starter("B");
        var outcome = Combat.resolve(first, second, 5, new Combat.Reaction(Combat.ReactionType.BLOCK, 5));
        assertEquals(3, first.life()); assertEquals(15, first.attack());
        assertEquals(3, outcome.defender().life());
        assertEquals(10, outcome.attacker().attack());
        assertEquals(2, outcome.defender().defense());
        assertTrue(outcome.successfulBlock());
    }
    @Test void takingFourOrMoreCostsLife() {
        var result = Combat.resolve(Combat.Fighter.starter("A"), Combat.Fighter.starter("B"), 4, Combat.Reaction.take());
        assertEquals(2, result.defender().life());
        assertEquals(3, result.defender().defense());
    }
    @Test void counterattackCanReverseTheStrike() {
        var result = Combat.resolve(Combat.Fighter.starter("A"), Combat.Fighter.starter("B"), 4,
            new Combat.Reaction(Combat.ReactionType.COUNTER, 9));
        assertEquals(2, result.attacker().life());
        assertEquals(3, result.defender().life());
    }
    @Test void closeCounterattackTradesDamageWhenDifferenceUnderFour() {
        var result = Combat.resolve(Combat.Fighter.starter("A"), Combat.Fighter.starter("B"), 6,
            new Combat.Reaction(Combat.ReactionType.COUNTER, 4));
        assertEquals(2, result.attacker().life()); assertEquals(2, result.defender().life());
    }
    @Test void guardCanHealButNeverBeyondMaximum() {
        var a = Combat.Fighter.starter("A");
        var wounded = new Combat.Fighter("B", 2, 5, 7);
        var result = Combat.resolve(a, wounded, 5, new Combat.Reaction(Combat.ReactionType.BLOCK, 5));
        assertEquals(3, result.defender().life());
        assertEquals(6, result.defender().attack());
    }
    @Test void emptyResourcesForceRestAndCapsAreObserved() {
        var f = new Combat.Fighter("A", 3, 0, Combat.MAX_DEFENSE);
        assertEquals(3, Combat.rest(f, Combat.Fighter.starter("B")).attacker().attack());
        assertThrows(IllegalArgumentException.class, () -> Combat.resolve(f, Combat.Fighter.starter("B"), 1, Combat.Reaction.take()));
    }
    @Test void impossibleActionsAreRejected() {
        var a = Combat.Fighter.starter("A"); var b = Combat.Fighter.starter("B");
        assertThrows(IllegalArgumentException.class, () -> Combat.resolve(a, b, 16, Combat.Reaction.take()));
        assertThrows(IllegalArgumentException.class, () -> Combat.resolve(a, b, 4, new Combat.Reaction(Combat.ReactionType.BLOCK, 8)));
    }
    @Test void intelligentComputerOnlyChoosesLegalActions() {
        var ai = new CombatAI(new java.util.Random(12));
        var human = Combat.Fighter.starter("Human");
        var machine = Combat.Fighter.starter("Bot");
        var opening = ai.chooseOpening(machine, human);
        assertTrue(opening.rest() || (opening.power() >= 1 && opening.power() <= machine.attack()));
        var reaction = ai.chooseReaction(human, machine, 5);
        assertTrue(reaction.type() == Combat.ReactionType.TAKE || reaction.power() >= 1);
        assertDoesNotThrow(() -> Combat.resolve(human, machine, 5, reaction));
    }
    @Test void scoreEncouragesResourceManagement() {
        var high = Scores.calculate(Combat.Fighter.starter("Winner"), 6, 2, 2);
        var low = Scores.calculate(new Combat.Fighter("Winner", 1, 2, 1), 20, 0, 0);
        assertTrue(high > low);
    }
}
