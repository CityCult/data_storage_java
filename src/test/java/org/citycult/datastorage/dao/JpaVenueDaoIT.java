package org.citycult.datastorage.dao;

import org.citycult.datastorage.entity.JpaEntityFactory;
import org.citycult.datastorage.entity.JpaVenue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertNotNull(dao.insert(entity));

        entity = edf.createVenue();
        entity.setName(base_name + " 2");
        Assertions.assertNotNull(dao.insert(entity));

        List<JpaVenue> entities = dao.getAll();
        Assertions.assertNotNull(entities);
        Assertions.assertTrue(entities.size() >= 2);
    }

    @Test
    public void find() {
        JpaVenue entity;

        entity = edf.createVenue();
        entity.setName(base_name);
        Assertions.assertNotNull(dao.insert(entity));

        List<JpaVenue> finds = dao.find(entity);
        Assertions.assertNotNull(finds);
        Assertions.assertEquals(1, finds.size());
    }

    @Test
    public void insertGet() {
        JpaVenue entity;

        entity = edf.createVenue();
        entity.setName(base_name);

        Assertions.assertNotNull(dao.insert(entity));
        Assertions.assertNotNull(dao.get(entity.getVenueUid()));
    }

    @Test
    public void update() {
        JpaVenue venue;

        venue = edf.createVenue();
        venue.setName(base_name);

        Assertions.assertNotNull(dao.insert(venue));

        final String new_name = venue.getName() + " UPDATED";
        venue.setName(new_name);

        Assertions.assertNotNull(dao.update(venue));

        venue = dao.get(venue.getVenueUid());
        Assertions.assertNotNull(venue);
        Assertions.assertEquals(new_name, venue.getName());
    }

    @Test
    public void delete() {
        JpaVenue entity;

        entity = edf.createVenue();
        entity.setName(base_name);

        Assertions.assertNotNull(dao.insert(entity));
        entity = dao.get(entity.getVenueUid());
        Assertions.assertNotNull(entity);

        Assertions.assertTrue(dao.delete(entity));
        entity = dao.get(entity.getVenueUid());
        Assertions.assertNull(entity);
    }
}
