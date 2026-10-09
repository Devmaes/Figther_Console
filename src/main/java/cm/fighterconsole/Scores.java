package cm.fighterconsole;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Local SQLite leaderboard. All SQL is parameterized; no network service is required. */
public final class Scores {
    private final String url;

    public record Entry(String player, String opponent, String mode, int score, int rounds, String playedAt) {}

    public Scores(Path databasePath) throws SQLException {
        try {
            Path parent = databasePath.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
        } catch (java.io.IOException e) {
            throw new SQLException("Impossible de créer le dossier des scores", e);
        }
        this.url = "jdbc:sqlite:" + databasePath.toAbsolutePath();
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA busy_timeout = 3000");
            statement.execute("CREATE TABLE IF NOT EXISTS matches ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "player TEXT NOT NULL, opponent TEXT NOT NULL, mode TEXT NOT NULL, "
                + "score INTEGER NOT NULL CHECK(score >= 0), rounds INTEGER NOT NULL CHECK(rounds > 0), "
                + "played_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_matches_rank ON matches(score DESC, rounds ASC, id ASC)");
        }
    }

    public void save(Entry entry) throws SQLException {
        if (entry.player().isBlank() || entry.player().length() > 32 || entry.opponent().isBlank()
                || entry.score() < 0 || entry.rounds() < 1 || !List.of("SOLO", "DUEL").contains(entry.mode()))
            throw new IllegalArgumentException("Entrée de classement invalide");
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(
                 "INSERT INTO matches(player,opponent,mode,score,rounds) VALUES (?,?,?,?,?)")) {
            statement.setString(1, entry.player());
            statement.setString(2, entry.opponent());
            statement.setString(3, entry.mode());
            statement.setInt(4, entry.score());
            statement.setInt(5, entry.rounds());
            statement.executeUpdate();
        }
    }

    public List<Entry> topFive() throws SQLException {
        List<Entry> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(
                 "SELECT player,opponent,mode,score,rounds,played_at FROM matches "
                 + "ORDER BY score DESC, rounds ASC, id ASC LIMIT 5");
             ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                result.add(new Entry(rows.getString(1), rows.getString(2), rows.getString(3),
                    rows.getInt(4), rows.getInt(5), rows.getString(6)));
            }
        }
        return result;
    }

    public static int calculate(Combat.Fighter winner, int exchanges, int hits, int blocks) {
        if (winner.knockedOut() || exchanges < 1 || hits < 0 || blocks < 0)
            throw new IllegalArgumentException("Score invalide");
        return 100 + winner.life() * 30 + winner.attack() * 4 + winner.defense() * 3
            + hits * 10 + blocks * 5 + Math.max(0, 60 - exchanges);
    }
}
