package org.citycult.datastorage.dao;

import junit.framework.Assert;
import org.citycult.datastorage.entity.*;
import org.junit.Test;

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
        Assert.assertNotNull(dao.insert(event));

        event = edf.createEvent(Category.NIGHTLIFE);
        event.setName("Test Event2");
        venue = edf.createVenue();
        venue.setName("Test Venue2");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        List<JpaEvent> events = dao.getAll();
        Assert.assertNotNull(events);
        Assert.assertTrue(events.size() >= 2);
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

        Assert.assertNotNull(dao.insert(event));

        List<JpaEvent> finds = dao.find(event);
        Assert.assertNotNull(finds);
        Assert.assertTrue(finds.size() == 1);
    }

    @Test
    public void insertGet() {
        JpaEvent event;
        JpaVenue venue;

        event = edf.createEvent(Category.ARTCULTURE);
        event.setName("Test Event");

        Assert.assertNull(dao.insert(event)); // no venue!

        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assert.assertNotNull(dao.insert(event));
        Assert.assertNotNull(dao.get(event.getEventUid()));

        // Check cinema with no movie
        event = edf.createEvent(Category.CINEMA);
        event.setName("Test Event Cinema");

        venue = edf.createVenue();
        venue.setName("Test Venue Cinema");
        event.setVenue(venue);

        Assert.assertNotNull(dao.insert(event));
        Assert.assertNotNull(dao.get(event.getEventUid()));

        // Check cinema with movie
        JpaEventCinema cinema = edf.createEventCinema();
        cinema.setName("Test Event Cinema");

        JpaMovie movie = edf.createMovie();
        movie.setTitle("Test Movie");
        cinema.setMovie(movie);
        venue = edf.createVenue();
        venue.setName("Test Venue Cinema");
        cinema.setVenue(venue);

        Assert.assertNotNull(dao.insert(cinema));

        event = dao.get(event.getEventUid());
        Assert.assertNotNull(event);
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

        Assert.assertNotNull(dao.insert(event));

        final String newName = event.getName() + " UPDATED";
        event.setName(newName);

        Assert.assertNotNull(dao.update(event));

        event = dao.get(event.getEventUid());
        Assert.assertNotNull(event);
        Assert.assertEquals(newName, event.getName());
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

        Assert.assertNotNull(dao.insert(event));
        event = dao.get(event.getEventUid());
        Assert.assertNotNull(event);

        Assert.assertTrue(dao.delete(event));
        event = dao.get(event.getEventUid());
        Assert.assertNull(event);
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
        Assert.assertNotNull(dao.insert(event));

        event = edf.createEvent(Category.CINEMA);
        event.setName("Test Event Cinema");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        List<JpaEvent> events = dao.getForVenue(venue);
        Assert.assertNotNull(events);
        Assert.assertTrue(events.size() >= 2);

        event = dao.get(event.getEventUid());
        Assert.assertNotNull(event);
    }
}
