package controller.handlers;

import io.javalin.http.Context;
import model.Url;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Objects;

public interface RequestHandler {
    boolean apply(Context ctx);
    void showMainPage(Context ctx);
    void createShortUrl(Context ctx);
    void makeRedirect(Context ctx);

    default String generateShortLink(Url link, String readablePart)  {
        var host = System.getenv()
                .getOrDefault("SERVER_HOST", "localhost:7070");
        var protocol = System.getenv()
                .getOrDefault("PROTOCOL", "http");
        var uri = Objects.equals(readablePart, "") ? link.getId() : readablePart;
        return protocol + "://" + host + "/" + uri;
    }

    default Url parseLink(String link) throws URISyntaxException, MalformedURLException, SQLException {
        var uri = new URI(link);
        var uriToUrl = uri.toURL();
        return new Url(uriToUrl.toString());
    }

}
