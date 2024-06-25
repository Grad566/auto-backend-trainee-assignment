package controller.handlers;

import dto.MainPage;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.SneakyThrows;
import model.ShortUrl;
import repository.ShortUrlRepository;
import repository.UrlRepository;
import util.RefPaths;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Objects;

import static io.javalin.rendering.template.TemplateUtil.model;

public final class NonJSONRequestHandler implements RequestHandler {
    @Override
    public boolean apply(Context ctx) {
        return !Objects.equals(ctx.contentType(), "application/json");
    }

    @SneakyThrows
    @Override
    public void showMainPage(Context ctx) {
        String flash = ctx.consumeSessionAttribute("flash");
        var id = ctx.consumeSessionAttribute("id");
        if (id != null) {
            var shortUrl = ShortUrlRepository.findById((Long) id)
                    .orElseThrow(NotFoundResponse::new);
            var page = new MainPage(flash, shortUrl);
            ctx.render("MainPage.jte", model("page", page));
            page.setFlash(null);
        } else {
            var page = new MainPage(flash);
            ctx.render("MainPage.jte", model("page", page));
            page.setFlash(null);
        }
    }

    @Override
    public void createShortUrl(Context ctx) {
        var link = ctx.formParam("url");
        var readablePart = ctx.formParam("readablePart");
        try {
            var originUrl = parseLink(link);
            UrlRepository.save(originUrl);

            var shortLink = generateShortLink(originUrl, readablePart);
            var shortUrl = new ShortUrl(originUrl.getId(), shortLink);
            ShortUrlRepository.save(shortUrl);

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

    @Override
    public void makeRedirect(Context ctx) {
        var name = ctx.fullUrl();
        try {
            var shortUrl = ShortUrlRepository.findByName(name)
                    .orElseThrow(NotFoundResponse::new);
            var originUrlId = shortUrl.getOriginUrlId();
            var url = UrlRepository.findById(originUrlId)
                    .orElseThrow(NotFoundResponse::new);
            ctx.redirect(url.getName());

        } catch (NotFoundResponse e) {
            ctx.result("Ссылка не действительная");
        } catch (SQLException e) {
            ctx.result("Проблема с бд!");
        }
    }
}
