package org.citycult.datastorage.dao;

import org.citycult.datastorage.entity.Category;
import org.citycult.datastorage.entity.JpaEntityFactory;
import org.citycult.datastorage.entity.JpaEventArtCulture;
import org.citycult.datastorage.entity.JpaVenue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author cpieloth
 */
public class JpaEventArtCultureDaoIT {

    private static final JpaEntityFactory edf = new JpaEntityFactory();
    private static final JpaEventArtCultureDao dao = JpaEntityDaoFactory.getInstance().getEventArtCultureDao();
    private static final String event_bname = "Test Event ArtCulture";
    private static final String venue_bname = "Test Venue";

    @Test
    public void getAll() {
        JpaEventArtCulture event;
        JpaVenue venue;

        event = edf.createEventArtCulture();
        event.setName("Test Event1");
        venue = edf.createVenue();
        venue.setName("Test Venue1");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        event = edf.createEventArtCulture();
        event.setName("Test Event2");
        venue = edf.createVenue();
        venue.setName("Test Venue2");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        List<JpaEventArtCulture> events = dao.getAll();
        Assertions.assertNotNull(events);
        Assertions.assertTrue(events.size() >= 2);
    }

    @Test
    public void find() {
        JpaEventArtCulture event;
        JpaVenue venue;

        event = edf.createEventArtCulture();
        event.setName("Test Event");
        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));

        List<JpaEventArtCulture> finds = dao.find(event);
        Assertions.assertNotNull(finds);
        Assertions.assertEquals(1, finds.size());
    }

    @Test
    public void insertGet() {
        JpaEventArtCulture event;
        JpaVenue venue;

        event = edf.createEventArtCulture();
        event.setName("Test Event");

        Assertions.assertNull(dao.insert(event)); // no venue!

        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));
        Assertions.assertNotNull(dao.get(event.getEventUid()));
    }

    @Test
    public void update() {
        JpaEventArtCulture event;
        JpaVenue venue;

        event = edf.createEventArtCulture();
        event.setName("Test Event");
        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));

        final String newName = event.getName() + " UPDATED";
        event.setName(newName);

        Assertions.assertNotNull(dao.update(event));

        event = dao.get(event.getEventUid());
        Assertions.assertNotNull(event);
        Assertions.assertEquals(newName, event.getName());
    }

    @Test
    public void delete() {
        JpaEventArtCulture event;
        JpaVenue venue;

        event = edf.createEventArtCulture();
        event.setName("Test Event");
        venue = edf.createVenue();
        venue.setName("Test Venue Cinema");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));
        event = dao.get(event.getEventUid());
        Assertions.assertNotNull(event);

        Assertions.assertTrue(dao.delete(event));
        event = dao.get(event.getEventUid());
        Assertions.assertNull(event);
    }

    @Test
    public void getForVenue() {
        JpaEventArtCulture event;
        JpaVenue venue;
        venue = edf.createVenue();
        venue.setName("Test Venue");

        event = edf.createEventArtCulture();
        event.setName("Test Event Art");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        event = edf.createEventArtCulture();
        event.setName("Test Event Culture");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        List<JpaEventArtCulture> events = dao.getForVenue(venue);
        Assertions.assertNotNull(events);
        Assertions.assertTrue(events.size() >= 2);

        for (JpaEventArtCulture e : events) {
            Assertions.assertSame(Category.ARTCULTURE, e.getCategory());
        }
    }
}
