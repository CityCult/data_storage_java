package org.citycult.datastorage.dao;

import junit.framework.Assert;
import org.citycult.datastorage.entity.JpaEntityFactory;
import org.citycult.datastorage.entity.JpaMovie;
import org.junit.Test;

import java.util.List;

/**
 * @author cpieloth
 */
public class JpaMovieDaoIT {

    private static final JpaEntityFactory edf = new JpaEntityFactory();
    private static final JpaMovieDao dao = JpaEntityDaoFactory.getInstance().getMovieDao();
    private static final String base_name = "Test Movie";

    @Test
    public void getAll() {
        JpaMovie entity;

        entity = edf.createMovie();
        entity.setTitle(base_name + " 1");
        Assert.assertNotNull(dao.insert(entity));

        entity = edf.createMovie();
        entity.setTitle(base_name + " 2");
        Assert.assertNotNull(dao.insert(entity));

        List<JpaMovie> entities = dao.getAll();
        Assert.assertNotNull(entities);
        Assert.assertTrue(entities.size() >= 2);
    }

    @Test
    public void find() {
        JpaMovie entity;

        entity = edf.createMovie();
        entity.setTitle(base_name);
        Assert.assertNotNull(dao.insert(entity));

        List<JpaMovie> finds = dao.find(entity);
        Assert.assertNotNull(finds);
        Assert.assertTrue(finds.size() == 1);
    }

    @Test
    public void insertGet() {
        JpaMovie entity;

        entity = edf.createMovie();
        entity.setTitle(base_name);

        Assert.assertNotNull(dao.insert(entity));
        Assert.assertNotNull(dao.get(entity.getMovieUid()));
    }

    @Test
    public void update() {
        JpaMovie entity;

        entity = edf.createMovie();
        entity.setTitle(base_name);

        Assert.assertNotNull(dao.insert(entity));

        final String new_name = entity.getTitle() + " UPDATED";
        entity.setTitle(new_name);

        Assert.assertNotNull(dao.update(entity));

        entity = dao.get(entity.getMovieUid());
        Assert.assertNotNull(entity);
        Assert.assertEquals(new_name, entity.getTitle());
    }

    @Test
    public void delete() {
        JpaMovie entity;

        entity = edf.createMovie();
        entity.setTitle(base_name);

        Assert.assertNotNull(dao.insert(entity));
        entity = dao.get(entity.getMovieUid());
        Assert.assertNotNull(entity);

        Assert.assertTrue(dao.delete(entity));
        entity = dao.get(entity.getMovieUid());
        Assert.assertNull(entity);
    }
}
