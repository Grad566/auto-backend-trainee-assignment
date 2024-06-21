package controller;

import dto.MainPage;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import model.ShortUrl;
import model.Url;
import repository.ShortUrlRepository;
import repository.UrlRepository;
import util.RefPaths;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlController {
    public static void showMainPage(Context ctx) throws SQLException {
        String flash = ctx.consumeSessionAttribute("flash");
        var id = ctx.consumeSessionAttribute("id");
        if (id != null) {
            var shortUrl = ShortUrlRepository.findById((Long) id)
                        .orElseThrow(() -> new NotFoundResponse("Url с id " + id + " не найден"));
            var page = new MainPage(flash, shortUrl);
            ctx.render("MainPage.jte", model("page", page));
            page.setFlash(null);
        } else {
            var page = new MainPage(flash);
            ctx.render("MainPage.jte", model("page", page));
            page.setFlash(null);
        }

    }

    public static void createShortUrl(Context ctx) throws SQLException {
        var link = ctx.formParam("url");
        var readablePart = ctx.formParam("readablePart");
        try {
            var uri = new URI(link);
            var uriToUrl = uri.toURL();
            var originUrl = new Url(uriToUrl.toString());
            UrlRepository.save(originUrl);

            var shortLink = generateShortLink(originUrl, readablePart);
            var shortUrl = new ShortUrl(originUrl.getId(), shortLink);
            ShortUrlRepository.save(shortUrl);
            ctx.result(shortUrl.getName());

            ctx.sessionAttribute("flash", "Ссылка успешна создана");
            ctx.sessionAttribute("id", shortUrl.getId());
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
        } catch (SQLException e) {
            ctx.sessionAttribute("flash", "Эта комбинация уже занята, кастомизируйте ссылку по другому");
        } finally {
            ctx.redirect(RefPaths.rootPath());
        }

    }

    public static void makeRedirect(Context ctx) {
        var name = ctx.fullUrl();
        try {
            var shortUrl = ShortUrlRepository.findByName(name)
                    .orElseThrow(() -> new NotFoundResponse("Ссылка не действительная"));
            var originUrlId = shortUrl.getOriginUrlId();
            var url = UrlRepository.findById(originUrlId)
                    .orElseThrow(() -> new NotFoundResponse("Ссылка не действительная"));
            ctx.redirect(url.getName());

        } catch (SQLException | NotFoundResponse e) {
            ctx.result("Ссылка не действительная");
        }
    }


    private static String generateShortLink(Url link, String readablePart)  {
        var host = System.getenv()
                .getOrDefault("SERVER_HOST", "localhost:7070");
        var protocol = System.getenv()
                .getOrDefault("PROTOCOL", "http");
        var uri = readablePart == null ? link.getId() : readablePart;
        return protocol + "://" + host + "/" + uri;
    }
}
