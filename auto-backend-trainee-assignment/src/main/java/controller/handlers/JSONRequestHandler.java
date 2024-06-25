package controller.handlers;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import model.ShortUrl;
import org.json.JSONObject;
import repository.ShortUrlRepository;
import repository.UrlRepository;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public final class JSONRequestHandler implements RequestHandler {
    @Override
    public boolean apply(Context ctx) {
        return Objects.equals(ctx.contentType(), "application/json");
    }

    @Override
    public void showMainPage(Context ctx) {
        var map = new HashMap<String, String>();
        map.put("message", "Отправьте ссылку");
        ctx.json(map);
    }

    @Override
    public void createShortUrl(Context ctx) {
        var json = new JSONObject(ctx.body());
        var link = json.getString("url");
        var readablePart = json.keySet().contains("readablePart")
                ? json.getString("readablePart") : "";
        var response = new HashMap<String, String>();

        try {
            var originUrl = parseLink(link);
            UrlRepository.save(originUrl);

            var shortLink = generateShortLink(originUrl, readablePart);
            var shortUrl = new ShortUrl(originUrl.getId(), shortLink);
            ShortUrlRepository.save(shortUrl);

            response.put("shortUrl", shortUrl.getName());
            response.put("message", "Ссылка успешно создана");
            ctx.json(response);
        } catch (URISyntaxException | MalformedURLException e) {
            response.put("error", "Некорректный адрес");
            ctx.status(400);
            ctx.json(response);
        } catch (SQLException e) {
            response.put("error", "Эта комбинация уже занята, кастомизируйте ссылку по другому");
            ctx.json(response);
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

        } catch (SQLException | NotFoundResponse e) {
            var response = new HashMap<String, String>();
            response.put("error", "Ссылка не действительная");
            ctx.json(response);
            ctx.status(400);
        }
    }
}
