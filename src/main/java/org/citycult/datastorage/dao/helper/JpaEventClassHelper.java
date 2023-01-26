package org.citycult.datastorage.dao.helper;

import org.citycult.datastorage.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cpieloth
 */
public class JpaEventClassHelper {

    private static final Logger log = LoggerFactory.getLogger(JpaEventClassHelper.class);

    public static Class<? extends JpaEvent> getJPAClass(Category category) {
        switch (category) {
            case MISC:
                return JpaEvent.class;
            case CINEMA:
                return JpaEventCinema.class;
            case NIGHTLIFE:
                return JpaEventNightlife.class;
            case LIVE:
                return JpaEventLive.class;
            case SPORT:
                return JpaEventSport.class;
            case ARTCULTURE:
                return JpaEventArtCulture.class;
            default:
                log.error("Unhandled category in getJPAEventClass()");
                return JpaEvent.class;
        }
    }

    public static Category getCategory(Class<? extends JpaEvent> clazz) {
        if (clazz == null)
            return null;

        if (clazz.isAssignableFrom(JpaEventCinema.class))
            return Category.CINEMA;
        else if (clazz.isAssignableFrom(JpaEventNightlife.class))
            return Category.NIGHTLIFE;
        else if (clazz.isAssignableFrom(JpaEventLive.class))
            return Category.LIVE;
        else if (clazz.isAssignableFrom(JpaEventSport.class))
            return Category.SPORT;
        else if (clazz.isAssignableFrom(JpaEventArtCulture.class))
            return Category.ARTCULTURE;
        else if (clazz.isAssignableFrom(JpaEvent.class))
            return Category.MISC;
        else {
            log.error("No category found for: " + clazz.getCanonicalName());
            return null;
        }
    }
}
