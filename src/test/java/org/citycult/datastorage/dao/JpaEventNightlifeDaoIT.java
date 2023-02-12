package org.citycult.datastorage.dao;

import org.citycult.datastorage.entity.Category;
import org.citycult.datastorage.entity.JpaEntityFactory;
import org.citycult.datastorage.entity.JpaEventNightlife;
import org.citycult.datastorage.entity.JpaVenue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author cpieloth
 */
public class JpaEventNightlifeDaoIT {

    private static final JpaEntityFactory edf = new JpaEntityFactory();
    private static final JpaEventNightlifeDao dao = JpaEntityDaoFactory.getInstance().getEventNightlifeDao();
    private static final String event_bname = "Test Event Nightlife";
    private static final String venue_bname = "Test Venue";

    @Test
    public void getAll() {
        JpaEventNightlife event;
        JpaVenue venue;

        event = edf.createEventNightLife();
        event.setName("Test Event1");
        venue = edf.createVenue();
        venue.setName("Test Venue1");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        event = edf.createEventNightLife();
        event.setName("Test Event2");
        venue = edf.createVenue();
        venue.setName("Test Venue2");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        List<JpaEventNightlife> events = dao.getAll();
        Assertions.assertNotNull(events);
        Assertions.assertTrue(events.size() >= 2);
    }

    @Test
    public void find() {
        JpaEventNightlife event;
        JpaVenue venue;

        event = edf.createEventNightLife();
        event.setName("Test Event");
        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));

        List<JpaEventNightlife> finds = dao.find(event);
        Assertions.assertNotNull(finds);
        Assertions.assertTrue(finds.size() == 1);
    }

    @Test
    public void insertGet() {
        JpaEventNightlife event;
        JpaVenue venue;

        event = edf.createEventNightLife();
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
        JpaEventNightlife event;
        JpaVenue venue;

        event = edf.createEventNightLife();
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
        JpaEventNightlife event;
        JpaVenue venue;

        event = edf.createEventNightLife();
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
        JpaEventNightlife event;
        JpaVenue venue;
        venue = edf.createVenue();
        venue.setName("Test Venue");

        event = edf.createEventNightLife();
        event.setName("Test Event Nightlife");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        event = edf.createEventNightLife();
        event.setName("Test Event Nightlife");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        List<JpaEventNightlife> events = dao.getForVenue(venue);
        Assertions.assertNotNull(events);
        Assertions.assertTrue(events.size() >= 2);

        for (JpaEventNightlife e : events) {
            Assertions.assertSame(Category.NIGHTLIFE, e.getCategory());
        }
    }
}
