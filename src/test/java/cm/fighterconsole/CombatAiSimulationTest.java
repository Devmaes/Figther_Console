package cm.fighterconsole;

import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class CombatAiSimulationTest {
    @Test void fiftyArtificialMatchesFinishWithoutExternalTimeout() {
        for (int seed = 0; seed < 50; seed++) {
            CombatAI ai = new CombatAI(new Random(seed));
            Combat.Fighter a = Combat.Fighter.starter("A");
            Combat.Fighter b = Combat.Fighter.starter("B");
            int exchanges = 0;
            while (!a.knockedOut() && !b.knockedOut() && exchanges < 500) {
                boolean first = exchanges % 2 == 0;
                Combat.Fighter active = first ? a : b;
                Combat.Fighter opponent = first ? b : a;
                CombatAI.Opening opening = ai.chooseOpening(active, opponent);
                Combat.Outcome outcome = opening.rest() ? Combat.rest(active, opponent)
                    : Combat.resolve(active, opponent, opening.power(),
                        ai.chooseReaction(active, opponent, opening.power()));
                if (first) { a = outcome.attacker(); b = outcome.defender(); }
                else { b = outcome.attacker(); a = outcome.defender(); }
                exchanges++;
            }
            assertTrue(a.knockedOut() || b.knockedOut(), "Simulation bloquée au seed " + seed);
        }
    }
}
