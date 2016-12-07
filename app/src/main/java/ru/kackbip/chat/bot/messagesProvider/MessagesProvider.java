package ru.kackbip.chat.bot.messagesProvider;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Владимир on 05.12.2016.
 */

public class MessagesProvider implements IMessagesProvider {

    private URL url;
    private Gson gson;

    public MessagesProvider(Gson gson) {
        this.gson = gson;
        try {
            url = new URL("https://api.chucknorris.io/jokes/random");
        } catch (MalformedURLException e) {
        }
    }

    @Override
    public Observable<String> next() {
        return Observable
                .<String>fromEmitter(
                        emitter -> {
                            String joke = null;
                            try {
                                joke = loadNext();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            emitter.onNext(joke);
                            emitter.onCompleted();
                        },
                        Emitter.BackpressureMode.ERROR
                )
                .onErrorResumeNext(throwable -> Observable.just("Шутки кончились..."))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private String loadNext() throws IOException {
        URLConnection conn = url.openConnection();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("utf-8")));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            return extractJoke(sb.toString());

        } finally {
            if (reader != null)
                reader.close();
        }
    }

    private String extractJoke(String response) {
        Joke joke = gson.fromJson(response, Joke.class);
        return joke.value;
    }

    private class Joke {
        String value;
    }
}
