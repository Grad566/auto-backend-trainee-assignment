import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import controller.UrlController;
import gg.jte.ContentType;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import gg.jte.TemplateEngine;
import repository.BaseRepository;
import util.RefPaths;
import java.nio.file.Paths;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            var app = getApp();
            app.start(getPort());
        } catch (IOException | URISyntaxException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Javalin getApp() throws IOException, URISyntaxException, SQLException {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDataBaseUrl());

        var dataSource = new HikariDataSource(hikariConfig);

        var sql = readSourceFile("sqlSchemas/schema.sql");

        try (var connection = dataSource.getConnection();
                var stmt = connection.createStatement()) {
            stmt.execute(sql);
        }

        BaseRepository.dataSource = dataSource;

        app.before(ctx -> {
            ctx.contentType("application/json; charset=utf=8");
        });

        app.get(RefPaths.rootPath(), UrlController::showMainPage);
        app.post(RefPaths.rootPath(), UrlController::createShortUrl);
        app.get(RefPaths.generatedLinkPath(), UrlController::makeRedirect);

        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    private static String getDataBaseUrl() {
        return System.getenv()
                    .getOrDefault("getDataBaseUrl", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    private static String readSourceFile(String fileName) throws IOException, URISyntaxException {
        return Files.readString(Paths.get(App.class
                    .getClassLoader().getResource(fileName).toURI()), StandardCharsets.UTF_8);
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }
}
