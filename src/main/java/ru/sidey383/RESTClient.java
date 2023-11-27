package ru.sidey383;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public abstract class RESTClient {


    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper mapper = new ObjectMapper();

    private final boolean debug;

    public RESTClient(boolean debug) {
        this.debug = debug;
    }

    protected CompletableFuture<Response> getResponse(Request request) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        Response resp = client.newCall(request).execute();
                        while (resp.code() == 429) {
                            resp.close();
                            Thread.sleep(1000);
                            resp = client.newCall(request).execute();
                        }
                        return resp;
                    } catch (IOException | InterruptedException e) {
                        throw new CompletionException(e);
                    }
                }
        );
    }

    protected <T> CompletableFuture<T> getResponse(Request request, Class<T> type) {
        return getResponse(request).thenApply((r) -> {
            try(r) {
                if (r.body() == null)
                    throw new CompletionException(new IllegalStateException("No answer body"));
                String data = r.body().string();
                if (debug)
                    System.out.println(data);
                return mapper.readValue(data, type);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    protected <T> CompletableFuture<T> getResponse(Request request, TypeReference<T> type) {
        return getResponse(request).thenApply((r) -> {
            try(r) {
                if (r.body() == null)
                    throw new CompletionException(new IllegalStateException("No answer body"));
                String data = r.body().string();
                if (debug)
                    System.out.println(data);
                return mapper.readValue(data, type);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

}
