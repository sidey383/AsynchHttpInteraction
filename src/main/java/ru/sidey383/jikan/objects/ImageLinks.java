package ru.sidey383.jikan.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ImageLinks(
        String image_url,
        String small_image_url,
        String large_image_url
) {
}
