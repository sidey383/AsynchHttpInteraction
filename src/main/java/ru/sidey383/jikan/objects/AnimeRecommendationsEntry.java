package ru.sidey383.jikan.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimeRecommendationsEntry {

    private int id;
    private String url;
    private ImageOptions images;
    private String title;

    public int getId() {
        return id;
    }

    @JsonSetter("mal_id")
    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageOptions getImages() {
        return images;
    }

    public void setImages(ImageOptions images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
