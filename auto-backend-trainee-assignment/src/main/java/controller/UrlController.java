package controller;

import controller.handlers.JSONRequestHandler;
import controller.handlers.NonJSONRequestHandler;
import controller.handlers.RequestHandler;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;


public class UrlController {
    private static List<RequestHandler> handlers;

    static {
        handlers = new ArrayList<>();
        handlers.add(new JSONRequestHandler());
        handlers.add(new NonJSONRequestHandler());
    }

    public static void showMainPage(Context ctx) {
        for (var handler : handlers) {
            if (handler.apply(ctx)) {
                handler.showMainPage(ctx);
            }
        }

    }

    public static void createShortUrl(Context ctx) {
        for (var handler : handlers) {
            if (handler.apply(ctx)) {
                handler.createShortUrl(ctx);
            }
        }
    }

    public static void makeRedirect(Context ctx) {
        for (var handler : handlers) {
            if (handler.apply(ctx)) {
                handler.makeRedirect(ctx);
            }
        }
    }

}
