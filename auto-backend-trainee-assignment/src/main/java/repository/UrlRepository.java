package repository;

import model.ShortUrl;
import model.Url;
import util.Utils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UrlRepository extends BaseRepository{
    public static void save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name) VALUES (?)";
        try (var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, url.getName());
            stmt.executeUpdate();

            var generatedKey = stmt.getGeneratedKeys();
            if (generatedKey.next()) {
                url.setId(generatedKey.getLong(1));
                url.setCreatedAt(Utils
                        .getFormattedDate(generatedKey.getTimestamp("created_at")));
            }
        }
    }

    public static Optional<Url> findById(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var urlName = resultSet.getString("name");
                var createdAt = Utils
                        .getFormattedDate(resultSet.getTimestamp("created_at"));
                var url = new Url(id, urlName, createdAt);
                return Optional.of(url);
            } else {
                return Optional.empty();
            }
        }
    }
}
