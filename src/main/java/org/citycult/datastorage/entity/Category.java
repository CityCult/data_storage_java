package org.citycult.datastorage.entity;

public enum Category {

    MISC(1), CINEMA(2), NIGHTLIFE(3), LIVE(4), SPORT(5), ARTCULTURE(6);

    public final Integer id;

    private Category(Integer id) {
        this.id = id;
    }
}
