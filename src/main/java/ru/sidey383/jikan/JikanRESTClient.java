package ru.sidey383.jikan;

import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.HttpUrl;
import okhttp3.Request;
import ru.sidey383.RESTClient;
import ru.sidey383.jikan.objects.*;

import java.util.concurrent.CompletableFuture;

public class JikanRESTClient extends RESTClient {

    private static final String rootUrl = "https://api.jikan.moe/v4";

    public JikanRESTClient() {
        super(false);
    }

    private HttpUrl.Builder getBaseBuilder() {
       return new HttpUrl.Builder().scheme("http").host("api.jikan.moe").addPathSegment("v4");
    }

    public CompletableFuture<DataArray<AnimeSearchEntry>> getAnimeSearch(String name) {
        HttpUrl url = getBaseBuilder().addPathSegment("anime").setQueryParameter("q", name).build();
        Request request = new Request.Builder().url(url).build();
        return getResponse(request, new TypeReference<>() {});
    }

    public CompletableFuture<DataArray<AnimeRelations>> getAnimeRelations(long id) {
        HttpUrl url = getBaseBuilder().addPathSegment("anime").addPathSegment(Long.toString(id)).addPathSegment("relations").build();
        Request request = new Request.Builder().url(url).build();
        return getResponse(request, new TypeReference<>() {});
    }

    public CompletableFuture<DataArray<Entry<AnimeRecommendationsEntry>>> getAnimeRecommendations(long id) {
        HttpUrl url = getBaseBuilder().addPathSegment("anime").addPathSegment(Long.toString(id)).addPathSegment("recommendations").build();
        Request request = new Request.Builder().url(url).build();
        return getResponse(request, new TypeReference<>() {});
    }

    public CompletableFuture<SingleData<AnimeFull>> getAnimeFull(long id) {
        HttpUrl url = getBaseBuilder().addPathSegment("anime").addPathSegment(Long.toString(id)).addPathSegment("full").build();
        Request request = new Request.Builder().url(url).build();
        return getResponse(request, new TypeReference<>() {});
    }

}
