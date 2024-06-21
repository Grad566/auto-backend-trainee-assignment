package repository;

import model.ShortUrl;
import util.Utils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class ShortUrlRepository extends BaseRepository {
    public static void save(ShortUrl shortUrl) throws SQLException {
        var sql = "INSERT INTO short_urls (name, origin_url_id) VALUES (?, ?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, shortUrl.getName());
            stmt.setLong(2, shortUrl.getOriginUrlId());
            stmt.executeUpdate();

            var generatedKey = stmt.getGeneratedKeys();
            if (generatedKey.next()) {
                shortUrl.setId(generatedKey.getLong(1));
                shortUrl.setCreatedAt(Utils
                        .getFormattedDate(generatedKey.getTimestamp("created_at")));
            }
        }
    }

    public static Optional<ShortUrl> findByName(String name) throws SQLException {
        var sql = "SELECT * FROM short_urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var originUrlId = resultSet.getLong("origin_url_id");
                var createdAt = Utils
                        .getFormattedDate(resultSet.getTimestamp("created_at"));
                var shortedUrl = new ShortUrl(id, name, originUrlId, createdAt);
                return Optional.of(shortedUrl);
            } else {
                return Optional.empty();
            }
        }
    }

    public static Optional<ShortUrl> findById(Long id) throws SQLException {
        var sql = "SELECT * FROM short_urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var urlName = resultSet.getString("name");
                var originUrlId = resultSet.getLong("origin_url_id");
                var createdAt = Utils
                        .getFormattedDate(resultSet.getTimestamp("created_at"));
                var shortedUrl = new ShortUrl(id, urlName, originUrlId, createdAt);
                return Optional.of(shortedUrl);
            } else {
                return Optional.empty();
            }
        }
    }
}
