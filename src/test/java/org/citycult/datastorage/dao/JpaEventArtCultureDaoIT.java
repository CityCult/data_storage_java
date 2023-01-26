package org.citycult.datastorage.dao;

import junit.framework.Assert;
import org.citycult.datastorage.entity.Category;
import org.citycult.datastorage.entity.JpaEntityFactory;
import org.citycult.datastorage.entity.JpaEventArtCulture;
import org.citycult.datastorage.entity.JpaVenue;
import org.junit.Test;

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
        Assert.assertNotNull(dao.insert(event));

        event = edf.createEventArtCulture();
        event.setName("Test Event2");
        venue = edf.createVenue();
        venue.setName("Test Venue2");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        List<JpaEventArtCulture> events = dao.getAll();
        Assert.assertNotNull(events);
        Assert.assertTrue(events.size() >= 2);
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

        Assert.assertNotNull(dao.insert(event));

        List<JpaEventArtCulture> finds = dao.find(event);
        Assert.assertNotNull(finds);
        Assert.assertTrue(finds.size() == 1);
    }

    @Test
    public void insertGet() {
        JpaEventArtCulture event;
        JpaVenue venue;

        event = edf.createEventArtCulture();
        event.setName("Test Event");

        Assert.assertNull(dao.insert(event)); // no venue!

        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assert.assertNotNull(dao.insert(event));
        Assert.assertNotNull(dao.get(event.getEventUid()));
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
        JpaEventArtCulture event;
        JpaVenue venue;

        event = edf.createEventArtCulture();
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
        JpaEventArtCulture event;
        JpaVenue venue;
        venue = edf.createVenue();
        venue.setName("Test Venue");

        event = edf.createEventArtCulture();
        event.setName("Test Event Art");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        event = edf.createEventArtCulture();
        event.setName("Test Event Culture");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        List<JpaEventArtCulture> events = dao.getForVenue(venue);
        Assert.assertNotNull(events);
        Assert.assertTrue(events.size() >= 2);

        for (JpaEventArtCulture e : events) {
            Assert.assertTrue(e.getCategory() == Category.ARTCULTURE);
        }
    }
}
