package cm.fighterconsole;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class ScoresTest {
    @TempDir Path directory;
    @Test void persistsTopFiveAcrossRepositoryInstances() throws Exception {
        Path path = directory.resolve("test.sqlite");
        Scores first = new Scores(path);
        for (int i = 0; i < 7; i++) {
            first.save(new Scores.Entry("Joueur" + i, "Rival", "SOLO", 40 + 10 * i, 10 - i, ""));
        }
        Scores second = new Scores(path);
        assertEquals(5, second.topFive().size());
        assertEquals(100, second.topFive().get(0).score());
        assertEquals("Joueur6", second.topFive().get(0).player());
        assertEquals(60, second.topFive().get(4).score());
    }
    @Test void malformedScoresAreRejected() throws Exception {
        Scores repository = new Scores(directory.resolve("secure.db"));
        assertThrows(IllegalArgumentException.class, () -> repository.save(new Scores.Entry("", "A", "SOLO", 10, 3, "")));
    }
}
