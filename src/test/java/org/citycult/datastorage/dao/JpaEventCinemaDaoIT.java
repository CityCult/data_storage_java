package org.citycult.datastorage.dao;

import org.citycult.datastorage.entity.Category;
import org.citycult.datastorage.entity.JpaEntityFactory;
import org.citycult.datastorage.entity.JpaEventCinema;
import org.citycult.datastorage.entity.JpaMovie;
import org.citycult.datastorage.entity.JpaVenue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertNotNull(dao.insert(event1));

        event2 = edf.createEventCinema();
        event2.setName("Test Event2");
        venue = edf.createVenue();
        venue.setName("Test Venue2");
        event2.setVenue(venue);
        movie = edf.createMovie();
        movie.setTitle("Test Movie1");
        event2.setMovie(movie);
        Assertions.assertNotNull(dao.insert(event1));

        List<JpaEventCinema> events = dao.getAll();
        Assertions.assertNotNull(events);
        Assertions.assertTrue(events.size() >= 2);
        for (JpaEventCinema e : events) {
            if (e.getEventUid().equals(event1.getEventUid()) || e.getEventUid().equals(event2.getEventUid()))
                Assertions.assertNotNull(e.getMovie());
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
        Assertions.assertNotNull(dao.insert(event));

        List<JpaEventCinema> finds = dao.find(event);
        Assertions.assertNotNull(finds);
        Assertions.assertEquals(1, finds.size());
        for (JpaEventCinema e : finds) {
            Assertions.assertNotNull(e.getMovie());
        }
    }

    @Test
    public void insertGet() {
        JpaEventCinema event;
        JpaVenue venue;

        event = edf.createEventCinema();
        event.setName("Test Event");

        Assertions.assertNull(dao.insert(event)); // no venue!

        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));
        Assertions.assertNotNull(dao.get(event.getEventUid()));

        // Check cinema with no movie
        event = edf.createEventCinema();
        event.setName("Test Event Cinema");

        venue = edf.createVenue();
        venue.setName("Test Venue Cinema");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));
        Assertions.assertNotNull(dao.get(event.getEventUid()));

        // Check cinema with movie
        event = edf.createEventCinema();
        event.setName("Test Event Cinema");

        JpaMovie movie = edf.createMovie();
        movie.setTitle("Test Movie");
        event.setMovie(movie);
        venue = edf.createVenue();
        venue.setName("Test Venue Cinema");
        event.setVenue(venue);

        Assertions.assertNotNull(dao.insert(event));

        event = dao.get(event.getEventUid());
        Assertions.assertNotNull(event);
        Assertions.assertNotNull(event.getMovie());
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
        JpaEventCinema event;
        JpaVenue venue;

        event = edf.createEventCinema();
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
        Assertions.assertNotNull(dao.insert(event));

        event = edf.createEventCinema();
        event.setName("Test Cinema2");
        venue = edf.createVenue();
        venue.setName("Test Venue2");
        event.setMovie(movie);
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        List<JpaEventCinema> events = dao.getForMovie(movie);
        Assertions.assertNotNull(events);
        Assertions.assertTrue(events.size() >= 2);
        for (JpaEventCinema e : events) {
            Assertions.assertNotNull(e.getMovie());
            Assertions.assertTrue(e.getMovie().getMovieUid().equals(movie.getMovieUid()));
        }

        movie = edf.createMovie();
        movie.setTitle("Test Movie2");

        event = edf.createEventCinema();
        event.setName("Test Cinema3");
        venue = edf.createVenue();
        venue.setName("Test Venue3");
        event.setMovie(movie);
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        events = dao.getForMovie(movie);
        Assertions.assertNotNull(events);
        Assertions.assertTrue(events.size() >= 1);
        for (JpaEventCinema e : events) {
            Assertions.assertNotNull(e.getMovie());
            Assertions.assertTrue(e.getMovie().getMovieUid().equals(movie.getMovieUid()));
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
        Assertions.assertNotNull(dao.insert(event));

        event = edf.createEventCinema();
        event.setName("Test Event Cinema");
        event.setVenue(venue);
        Assertions.assertNotNull(dao.insert(event));

        List<JpaEventCinema> events = dao.getForVenue(venue);
        Assertions.assertNotNull(events);
        Assertions.assertTrue(events.size() >= 2);

        for (JpaEventCinema e : events) {
            Assertions.assertTrue(e.getCategory() == Category.CINEMA);
        }
    }
}
