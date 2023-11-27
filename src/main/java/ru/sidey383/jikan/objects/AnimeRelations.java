package ru.sidey383.jikan.objects;

import java.util.List;

public class AnimeRelations {

    private String relation;

    private List<AnimeRelationsEntry> entry;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public List<AnimeRelationsEntry> getEntry() {
        return entry;
    }

    public void setEntry(List<AnimeRelationsEntry> entry) {
        this.entry = entry;
    }
}
