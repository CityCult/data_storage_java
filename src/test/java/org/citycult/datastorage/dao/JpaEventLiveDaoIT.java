package org.citycult.datastorage.dao;

import junit.framework.Assert;
import org.citycult.datastorage.entity.Category;
import org.citycult.datastorage.entity.JpaEntityFactory;
import org.citycult.datastorage.entity.JpaEventLive;
import org.citycult.datastorage.entity.JpaVenue;
import org.junit.Test;

import java.util.List;

/**
 * @author cpieloth
 */
public class JpaEventLiveDaoIT {

    private static final JpaEntityFactory edf = new JpaEntityFactory();
    private static final JpaEventLiveDao dao = JpaEntityDaoFactory.getInstance().getEventLiveDao();
    private static final String event_bname = "Test Event Live";
    private static final String venue_bname = "Test Venue";

    @Test
    public void getAll() {
        JpaEventLive event;
        JpaVenue venue;

        event = edf.createEventLive();
        event.setName("Test Event1");
        venue = edf.createVenue();
        venue.setName("Test Venue1");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        event = edf.createEventLive();
        event.setName("Test Event2");
        venue = edf.createVenue();
        venue.setName("Test Venue2");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        List<JpaEventLive> events = dao.getAll();
        Assert.assertNotNull(events);
        Assert.assertTrue(events.size() >= 2);
    }

    @Test
    public void find() {
        JpaEventLive event;
        JpaVenue venue;

        event = edf.createEventLive();
        event.setName("Test Event");
        venue = edf.createVenue();
        venue.setName("Test Venue");
        event.setVenue(venue);

        Assert.assertNotNull(dao.insert(event));

        List<JpaEventLive> finds = dao.find(event);
        Assert.assertNotNull(finds);
        Assert.assertTrue(finds.size() == 1);
    }

    @Test
    public void insertGet() {
        JpaEventLive event;
        JpaVenue venue;

        event = edf.createEventLive();
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
        JpaEventLive event;
        JpaVenue venue;

        event = edf.createEventLive();
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
        JpaEventLive event;
        JpaVenue venue;

        event = edf.createEventLive();
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
        JpaEventLive event;
        JpaVenue venue;
        venue = edf.createVenue();
        venue.setName("Test Venue");

        event = edf.createEventLive();
        event.setName("Test Event Live");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        event = edf.createEventLive();
        event.setName("Test Event Live");
        event.setVenue(venue);
        Assert.assertNotNull(dao.insert(event));

        List<JpaEventLive> events = dao.getForVenue(venue);
        Assert.assertNotNull(events);
        Assert.assertTrue(events.size() >= 2);

        for (JpaEventLive e : events) {
            Assert.assertTrue(e.getCategory() == Category.LIVE);
        }
    }
}
