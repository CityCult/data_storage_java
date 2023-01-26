package org.citycult.datastorage.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author cpieloth
 */
public class JpaEntityFactory {

    private static final Logger log = LoggerFactory.getLogger(JpaEntityFactory.class);

    public JpaEvent createEvent() {
        return new JpaEvent();
    }

    public JpaEvent createEvent(Category category) {
        switch (category) {
            case MISC:
                return createEvent();
            case CINEMA:
                return createEventCinema();
            case NIGHTLIFE:
                return createEventNightLife();
            case LIVE:
                return createEventLive();
            case SPORT:
                return createEventSport();
            case ARTCULTURE:
                return createEventArtCulture();
            default:
                log.error("Unhandled category in createEvent()");
                return createEvent();
        }
    }

    public JpaEventArtCulture createEventArtCulture() {
        return new JpaEventArtCulture();
    }

    public JpaEventCinema createEventCinema() {
        return new JpaEventCinema();
    }

    public JpaEventLive createEventLive() {
        return new JpaEventLive();
    }

    public JpaEventNightlife createEventNightLife() {
        return new JpaEventNightlife();
    }

    public JpaEventSport createEventSport() {
        return new JpaEventSport();
    }

    public JpaMovie createMovie() {
        return new JpaMovie();
    }

    public JpaVenue createVenue() {
        return new JpaVenue();
    }
}
