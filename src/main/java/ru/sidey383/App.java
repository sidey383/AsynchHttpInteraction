package ru.sidey383;

import ru.sidey383.jikan.JikanRESTClient;
import ru.sidey383.jikan.objects.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class App {

    private record FullRelatedAnime(String relation, SingleData<AnimeFull> anime) {
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        JikanRESTClient client = new JikanRESTClient();
        System.out.println("Enter anime name for search");
        String name = br.readLine();
        DataArray<AnimeSearchEntry> searchResult;
        try {
            searchResult = client.getAnimeSearch(name).get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: ");
            e.printStackTrace();
            return;
        }
        Integer choose = null;
        while (choose == null) {
            for (int i = 0; i < searchResult.getData().size(); i++) {
                var a = searchResult.getData().get(i);
                System.out.println(
                        i + ": " +
                        a.getTitles().stream().map(c -> c.title() + "\n").reduce("", String::concat) +
                        a.getType() + "\n"
                );
            }
            System.out.println("Select anime number or 'c' for cancel");
            String c = br.readLine();
            if (c.equals("c")) {
                return;
            }
            try {
                choose = Integer.parseInt(c);
                if (choose >= searchResult.getData().size()) {
                    choose = null;
                    System.out.println("Wrong number");
                }
            } catch (NumberFormatException ignore) {
                System.out.println("Not a number");
            }
        }
        var chooseAnime = searchResult.getData().get(choose);
        CompletableFuture<Collection<SingleData<AnimeFull>>> recommendFuture = client.getAnimeRecommendations(chooseAnime.getId()).thenCompose(d -> {
            @SuppressWarnings("unchecked")
            CompletableFuture<SingleData<AnimeFull>>[] futures = (CompletableFuture<SingleData<AnimeFull>>[]) d.getData().stream()
                    .map(Entry::getEntry)
                    .map(AnimeRecommendationsEntry::getId)
                    .map(client::getAnimeFull)
                    .toArray(CompletableFuture[]::new);
            return CompletableFuture
                    .allOf(futures)
                    .thenApply(v -> Arrays.stream(futures).map(CompletableFuture::join).collect(Collectors.toList()));
        });
        CompletableFuture<Collection<FullRelatedAnime>> relatedFuture = client.getAnimeRelations(chooseAnime.getId()).thenCompose(d -> {
            List<CompletableFuture<FullRelatedAnime>> list = new ArrayList<>();
            for (var relation : d.getData()) {
                for (var a : relation.getEntry()) {
                    list.add(client.getAnimeFull(a.getId()).thenApply(an -> new FullRelatedAnime(relation.getRelation(), an)));
                }
            }
            @SuppressWarnings("unchecked")
            CompletableFuture<FullRelatedAnime>[] ar = (CompletableFuture<FullRelatedAnime>[]) list.toArray(CompletableFuture[]::new);
            return CompletableFuture
                    .allOf(ar)
                    .thenApply(v -> Arrays.stream(ar).map(CompletableFuture::join).collect(Collectors.toList()));
        });
        try {
            Collection<FullRelatedAnime> related = relatedFuture.get();
            System.out.println("Related anime: \n");
            related.forEach(rel -> {
                if (rel.anime == null)
                    return;
                System.out.println("\tRelations: " + rel.relation);
                printAnime(rel.anime());
            });
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: ");
            e.printStackTrace();
        }
        try {
            Collection<SingleData<AnimeFull>> recommend = recommendFuture.get();
            System.out.println("Recommended anime: \n");
            recommend.forEach(App::printAnime);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: ");
            e.printStackTrace();
        }
    }

    private static void printAnime(SingleData<AnimeFull> data) {
        if (data == null || data.getData() == null)
            return;
        var anime = data.getData();
        System.out.println(
                (anime.getTitles() == null ? "\tTitle: " + anime.getTitle() + "\n" : "\tTitles: \n" + anime.getTitles().stream().map(c -> "\t\t" + c.title() + "\n").distinct().reduce("", String::concat)) +
                (anime.getImages() != null ?  "\tImage: " + anime.getImages().jpg().large_image_url() + "\n" : "") +
                (anime.getTrailer() != null ? "\tTrailer: " + anime.getTrailer().getUrl() + "\n" : "") +
                "\tSeason: " + anime.getSeason() + "\n" +
                "\tType: " + anime.getType() + "\n" +
                "\tStatus: " + anime.getStatus() + "\n" +
                "\tRating: " + anime.getRating() + "\n" +
                "\tScore: " + anime.getScore() + "\n" +
                "\tPopularity: " + anime.getPopularity() + "\n" +
                "\tSynopsis: \n" + Arrays.stream(anime.getSynopsis().split("\n")).map(s -> "\t\t" + s + "\n").collect(Collectors.joining()) + "\n"
        );
    }

}
