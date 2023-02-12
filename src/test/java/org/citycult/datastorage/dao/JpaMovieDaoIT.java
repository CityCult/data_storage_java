package org.citycult.datastorage.dao;

import org.citycult.datastorage.entity.JpaEntityFactory;
import org.citycult.datastorage.entity.JpaMovie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertNotNull(dao.insert(entity));

        entity = edf.createMovie();
        entity.setTitle(base_name + " 2");
        Assertions.assertNotNull(dao.insert(entity));

        List<JpaMovie> entities = dao.getAll();
        Assertions.assertNotNull(entities);
        Assertions.assertTrue(entities.size() >= 2);
    }

    @Test
    public void find() {
        JpaMovie entity;

        entity = edf.createMovie();
        entity.setTitle(base_name);
        Assertions.assertNotNull(dao.insert(entity));

        List<JpaMovie> finds = dao.find(entity);
        Assertions.assertNotNull(finds);
        Assertions.assertEquals(1, finds.size());
    }

    @Test
    public void insertGet() {
        JpaMovie entity;

        entity = edf.createMovie();
        entity.setTitle(base_name);

        Assertions.assertNotNull(dao.insert(entity));
        Assertions.assertNotNull(dao.get(entity.getMovieUid()));
    }

    @Test
    public void update() {
        JpaMovie entity;

        entity = edf.createMovie();
        entity.setTitle(base_name);

        Assertions.assertNotNull(dao.insert(entity));

        final String new_name = entity.getTitle() + " UPDATED";
        entity.setTitle(new_name);

        Assertions.assertNotNull(dao.update(entity));

        entity = dao.get(entity.getMovieUid());
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(new_name, entity.getTitle());
    }

    @Test
    public void delete() {
        JpaMovie entity;

        entity = edf.createMovie();
        entity.setTitle(base_name);

        Assertions.assertNotNull(dao.insert(entity));
        entity = dao.get(entity.getMovieUid());
        Assertions.assertNotNull(entity);

        Assertions.assertTrue(dao.delete(entity));
        entity = dao.get(entity.getMovieUid());
        Assertions.assertNull(entity);
    }
}
