package org.citycult.datastorage.dao;

import org.citycult.datastorage.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author cpieloth
 */
public class JpaEventDaoIT {

    private static final JpaEntityFactory edf = new JpaEntityFactory();
    private static final JpaEventDao dao = JpaEntityDaoFactory.getInstance().getEventDao();

    @Test
    public void getAll() {
        JpaEvent event;
        JpaVenue venue;

        event = edf.createEvent(Category.NIGHTLIFE);
        event.setName("Test Event1");
        venue = edf.createVenue();
        venue.setName("Test Venue1");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        event = edf.createEvent(Category.NIGHTLIFE);
        event.setName("Test Event2");
        venue = edf.createVenue();
        venue.setName("Test Venue2");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        List<JpaEvent> events = dao.getAll();
        Assertions.assertNotNull(events);
        Assertions.assertTrue(events.size() >= 2);
    }

    @Test
    public void find() {
        JpaEvent event;
        JpaVenue venue;

        event = edf.createEvent(Category.NIGHTLIFE);
        event.setName("Test Event");
        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));

        List<JpaEvent> finds = dao.find(event);
        Assertions.assertNotNull(finds);
        Assertions.assertEquals(1, finds.size());
    }

    @Test
    public void insertGet() {
        JpaEvent event;
        JpaVenue venue;

        event = edf.createEvent(Category.ARTCULTURE);
        event.setName("Test Event");

        Assertions.assertNull(dao.insert(event)); // no venue!

        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));
        Assertions.assertNotNull(dao.get(event.getEventUid()));

        // Check cinema with no movie
        event = edf.createEvent(Category.CINEMA);
        event.setName("Test Event Cinema");

        venue = edf.createVenue();
        venue.setName("Test Venue Cinema");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));
        Assertions.assertNotNull(dao.get(event.getEventUid()));

        // Check cinema with movie
        JpaEventCinema cinema = edf.createEventCinema();
        cinema.setName("Test Event Cinema");

        JpaMovie movie = edf.createMovie();
        movie.setTitle("Test Movie");
        cinema.setMovie(movie);
        venue = edf.createVenue();
        venue.setName("Test Venue Cinema");
        cinema.setVenue(venue);

        Assertions.assertNotNull(dao.insert(cinema));

        event = dao.get(event.getEventUid());
        Assertions.assertNotNull(event);
    }

    @Test
    public void update() {
        JpaEvent event;
        JpaVenue venue;

        event = edf.createEvent(Category.NIGHTLIFE);
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
        JpaEvent event;
        JpaVenue venue;

        event = edf.createEvent(Category.LIVE);
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
        JpaEvent event;
        JpaVenue venue;
        venue = edf.createVenue();
        venue.setName("Test Venue");

        event = edf.createEvent(Category.LIVE);
        event.setName("Test Event Live");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        event = edf.createEvent(Category.CINEMA);
        event.setName("Test Event Cinema");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        List<JpaEvent> events = dao.getForVenue(venue);
        Assertions.assertNotNull(events);
        Assertions.assertTrue(events.size() >= 2);

        event = dao.get(event.getEventUid());
        Assertions.assertNotNull(event);
    }
}
