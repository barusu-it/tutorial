package it.barusu.tutorial.vertxwebsimple;

import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class App extends AbstractVerticle {

    private static final String SERVER = "vertx-web-simple";
    private String date;

    static {
        Json.mapper.registerModule(new AfterburnerModule());
        Json.prettyMapper.registerModule(new AfterburnerModule());
    }


    @Override
    public void start() {
        final Router router = Router.router(vertx);

        date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now());
        vertx.setPeriodic(1000, handler -> date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()));

        router.get("/json").handler(rc -> rc.response()
                .putHeader(HttpHeaders.SERVER, SERVER)
                .putHeader(HttpHeaders.DATE, date)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(Json.encodeToBuffer(new Message("Hello, World!"))));
        router.get("/plaintext").handler(rc -> rc.response()
                .putHeader(HttpHeaders.SERVER, SERVER)
                .putHeader(HttpHeaders.DATE, date)
                .putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
                .end("Hello, World!"));

        vertx.createHttpServer().requestHandler(router).listen(8080);
    }
}
