package ru.sidey383.jikan.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AnimeTitle (
    String type,
    String title
) {}
