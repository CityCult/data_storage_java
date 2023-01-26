package org.citycult.datastorage.dao;

import junit.framework.Assert;
import org.citycult.datastorage.entity.JpaEntityFactory;
import org.citycult.datastorage.entity.JpaVenue;
import org.junit.Test;

import java.util.List;

/**
 * @author cpieloth
 */
public class JpaVenueDaoIT {

    private static final JpaEntityFactory edf = new JpaEntityFactory();
    private static final JpaVenueDao dao = JpaEntityDaoFactory.getInstance().getVenueDao();
    private static final String base_name = "Test Venue";

    @Test
    public void getAll() {
        JpaVenue entity;

        entity = edf.createVenue();
        entity.setName(base_name + " 1");
        Assert.assertNotNull(dao.insert(entity));

        entity = edf.createVenue();
        entity.setName(base_name + " 2");
        Assert.assertNotNull(dao.insert(entity));

        List<JpaVenue> entities = dao.getAll();
        Assert.assertNotNull(entities);
        Assert.assertTrue(entities.size() >= 2);
    }

    @Test
    public void find() {
        JpaVenue entity;

        entity = edf.createVenue();
        entity.setName(base_name);
        Assert.assertNotNull(dao.insert(entity));

        List<JpaVenue> finds = dao.find(entity);
        Assert.assertNotNull(finds);
        Assert.assertTrue(finds.size() == 1);
    }

    @Test
    public void insertGet() {
        JpaVenue entity;

        entity = edf.createVenue();
        entity.setName(base_name);

        Assert.assertNotNull(dao.insert(entity));
        Assert.assertNotNull(dao.get(entity.getVenueUid()));
    }

    @Test
    public void update() {
        JpaVenue venue;

        venue = edf.createVenue();
        venue.setName(base_name);

        Assert.assertNotNull(dao.insert(venue));

        final String new_name = venue.getName() + " UPDATED";
        venue.setName(new_name);

        Assert.assertNotNull(dao.update(venue));

        venue = dao.get(venue.getVenueUid());
        Assert.assertNotNull(venue);
        Assert.assertEquals(new_name, venue.getName());
    }

    @Test
    public void delete() {
        JpaVenue entity;

        entity = edf.createVenue();
        entity.setName(base_name);

        Assert.assertNotNull(dao.insert(entity));
        entity = dao.get(entity.getVenueUid());
        Assert.assertNotNull(entity);

        Assert.assertTrue(dao.delete(entity));
        entity = dao.get(entity.getVenueUid());
        Assert.assertNull(entity);
    }
}
