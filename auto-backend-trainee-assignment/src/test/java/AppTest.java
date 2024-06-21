import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import model.ShortUrl;
import model.Url;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ShortUrlRepository;
import repository.UrlRepository;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
class AppTest {
    Javalin app;
    private static MockWebServer server;

    @BeforeEach
    public final void setApp() throws SQLException, IOException, URISyntaxException {
        app = App.getApp();
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        server = new MockWebServer();
        var serverResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setResponseCode(200);
        server.enqueue(serverResponse);
        server.start();
    }

    @AfterAll
    public static void shutdownMock() throws IOException {
        server.shutdown();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string().contains("Генератор ссылок"));
        });
    }

    @Test
    public void testAddCustomUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://github.com&readablePart=jack";
            var response = client.post("/", requestBody);
            assertThat(response.code()).isEqualTo(200);

            var url = ShortUrlRepository.findById(1L).orElse(new ShortUrl(1L, ""));
            var name = url.getName();
            assertThat(name).contains("http://localhost:7070/jack");
        });
    }

    @Test
    public void testAddUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://github.com";
            var response = client.post("/", requestBody);
            assertThat(response.code()).isEqualTo(200);

            var url = UrlRepository.findById(1L).orElse(new Url(""));
            var name = url.getName();
            assertThat(name).contains("https://github.com");
        });
    }

    @Test
    public void testWrongUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=123";
            var response = client.post("/", requestBody);
            assertThat(response.code()).isEqualTo(200);

            var url = UrlRepository.findById(1L);
            assertThat(url).isEmpty();
        });
    }

    @Test
    public void testRedirect() {
        var baseUrl = server.url("/").toString();
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + baseUrl;
            client.post("/", requestBody);
            var response = client.get("/1");
            assertThat(response.code()).isEqualTo(200);
        });
    }
}
