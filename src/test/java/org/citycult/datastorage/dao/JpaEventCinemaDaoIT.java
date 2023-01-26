package org.citycult.datastorage.dao;

import junit.framework.Assert;
import org.citycult.datastorage.entity.Category;
import org.citycult.datastorage.entity.JpaEntityFactory;
import org.citycult.datastorage.entity.JpaEventCinema;
import org.citycult.datastorage.entity.JpaMovie;
import org.citycult.datastorage.entity.JpaVenue;
import org.junit.Test;

import java.util.List;

/**
 * @author cpieloth
 */
public class JpaEventCinemaDaoIT {

    private static final JpaEntityFactory edf = new JpaEntityFactory();
    private static final JpaEventCinemaDao dao = JpaEntityDaoFactory.getInstance().getEventCinemaDao();
    private static final String event_bname = "Test Event Cinema";
    private static final String venue_bname = "Test Venue";

    @Test
    public void getAll() {
        JpaEventCinema event1, event2;
        JpaVenue venue;
        JpaMovie movie;

        event1 = edf.createEventCinema();
        event1.setName("Test Event1");
        venue = edf.createVenue();
        venue.setName("Test Venue1");
        event1.setVenue(venue);
        movie = edf.createMovie();
        movie.setTitle("Test Movie1");
        event1.setMovie(movie);
        Assert.assertNotNull(dao.insert(event1));

        event2 = edf.createEventCinema();
        event2.setName("Test Event2");
        venue = edf.createVenue();
        venue.setName("Test Venue2");
        event2.setVenue(venue);
        movie = edf.createMovie();
        movie.setTitle("Test Movie1");
        event2.setMovie(movie);
        Assert.assertNotNull(dao.insert(event1));

        List<JpaEventCinema> events = dao.getAll();
        Assert.assertNotNull(events);
        Assert.assertTrue(events.size() >= 2);
        for (JpaEventCinema e : events) {
            if (e.getEventUid().equals(event1.getEventUid()) || e.getEventUid().equals(event2.getEventUid()))
                Assert.assertNotNull(e.getMovie());
        }
    }

    @Test
    public void find() {
        JpaEventCinema event;
        JpaVenue venue;
        JpaMovie movie;

        event = edf.createEventCinema();
        event.setName("Test Event");
        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);
        movie = edf.createMovie();
        movie.setTitle("Test Movie");
        event.setMovie(movie);
        Assert.assertNotNull(dao.insert(event));

        List<JpaEventCinema> finds = dao.find(event);
        Assert.assertNotNull(finds);
        Assert.assertTrue(finds.size() == 1);
        for (JpaEventCinema e : finds) {
            Assert.assertNotNull(e.getMovie());
        }
    }

    @Test
    public void insertGet() {
        JpaEventCinema event;
        JpaVenue venue;

        event = edf.createEventCinema();
        event.setName("Test Event");

        Assert.assertNull(dao.insert(event)); // no venue!

        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assert.assertNotNull(dao.insert(event));
        Assert.assertNotNull(dao.get(event.getEventUid()));

        // Check cinema with no movie
        event = edf.createEventCinema();
        event.setName("Test Event Cinema");

        venue = edf.createVenue();
        venue.setName("Test Venue Cinema");
        event.setVenue(venue);

        Assert.assertNotNull(dao.insert(event));
        Assert.assertNotNull(dao.get(event.getEventUid()));

        // Check cinema with movie
        event = edf.createEventCinema();
        event.setName("Test Event Cinema");

        JpaMovie movie = edf.createMovie();
        movie.setTitle("Test Movie");
        event.setMovie(movie);
        venue = edf.createVenue();
        venue.setName("Test Venue Cinema");
        event.setVenue(venue);

        Assert.assertNotNull(dao.insert(event));

        event = dao.get(event.getEventUid());
        Assert.assertNotNull(event);
        Assert.assertNotNull(event.getMovie());
    }

    @Test
    public void update() {
        JpaEventCinema event;
        JpaVenue venue;

        event = edf.createEventCinema();
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
        JpaEventCinema event;
        JpaVenue venue;

        event = edf.createEventCinema();
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
    public void getForMovie() {
        JpaEventCinema event;
        JpaMovie movie;
        JpaVenue venue;

        movie = edf.createMovie();
        movie.setTitle("Test Movie1");

        event = edf.createEventCinema();
        event.setName("Test Cinema1");
        venue = edf.createVenue();
        venue.setName("Test Venue1");
        event.setMovie(movie);
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        event = edf.createEventCinema();
        event.setName("Test Cinema2");
        venue = edf.createVenue();
        venue.setName("Test Venue2");
        event.setMovie(movie);
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        List<JpaEventCinema> events = dao.getForMovie(movie);
        Assert.assertNotNull(events);
        Assert.assertTrue(events.size() >= 2);
        for (JpaEventCinema e : events) {
            Assert.assertNotNull(e.getMovie());
            Assert.assertTrue(e.getMovie().getMovieUid().equals(movie.getMovieUid()));
        }

        movie = edf.createMovie();
        movie.setTitle("Test Movie2");

        event = edf.createEventCinema();
        event.setName("Test Cinema3");
        venue = edf.createVenue();
        venue.setName("Test Venue3");
        event.setMovie(movie);
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        events = dao.getForMovie(movie);
        Assert.assertNotNull(events);
        Assert.assertTrue(events.size() >= 1);
        for (JpaEventCinema e : events) {
            Assert.assertNotNull(e.getMovie());
            Assert.assertTrue(e.getMovie().getMovieUid().equals(movie.getMovieUid()));
        }
    }

    @Test
    public void getForVenue() {
        JpaEventCinema event;
        JpaVenue venue;
        venue = edf.createVenue();
        venue.setName("Test Venue");

        event = edf.createEventCinema();
        event.setName("Test Event Cinema");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        event = edf.createEventCinema();
        event.setName("Test Event Cinema");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        List<JpaEventCinema> events = dao.getForVenue(venue);
        Assert.assertNotNull(events);
        Assert.assertTrue(events.size() >= 2);

        for (JpaEventCinema e : events) {
            Assert.assertTrue(e.getCategory() == Category.CINEMA);
        }
    }
}
