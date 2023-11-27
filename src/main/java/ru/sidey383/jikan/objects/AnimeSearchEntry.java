package ru.sidey383.jikan.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimeSearchEntry {
    private int id;
    private String url;
    private ImageOptions images;
    private List<AnimeTitle> titles;
    private String type;

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

    public List<AnimeTitle> getTitles() {
        return titles;
    }

    public void setTitles(List<AnimeTitle> titles) {
        this.titles = titles;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AnimeSearchEntry{" +
               "id=" + id +
               ", url='" + url + '\'' +
               ", images=" + images +
               ", titles=" + titles +
               ", type='" + type + '\'' +
               '}';
    }
}
