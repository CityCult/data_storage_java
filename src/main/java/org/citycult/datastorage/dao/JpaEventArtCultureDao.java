package org.citycult.datastorage.dao;

import org.citycult.datastorage.dao.helper.JpaEventCategoryDao;
import org.citycult.datastorage.entity.JpaEventArtCulture;
import org.citycult.datastorage.entity.JpaVenue;
import org.citycult.datastorage.util.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author cpieloth
 */
public class JpaEventArtCultureDao {

    private static final Logger log = LoggerFactory.getLogger(JpaEventArtCultureDao.class);

    private final JpaEventCategoryDao<JpaEventArtCulture> dao;

    private static final JpaEventArtCultureDao instance = new JpaEventArtCultureDao();

    public static JpaEventArtCultureDao getInstance() {
        return instance;
    }

    private JpaEventArtCultureDao() {
        dao = new JpaEventCategoryDao<JpaEventArtCulture>(JpaEventArtCulture.class, log);
    }

    public JpaEventArtCulture get(UUID uid) {
        return dao.get(uid);
    }

    public List<JpaEventArtCulture> getForVenue(JpaVenue venue) {
        return dao.getForVenue(venue);
    }

    public List<JpaEventArtCulture> getForVenue(JpaVenue venue, DateHelper.DateRange range) {
        return dao.getForVenue(venue, range);
    }

    public List<JpaEventArtCulture> getDate(DateHelper.DateRange range) {
        return dao.getDate(range);
    }

    public List<JpaEventArtCulture> getDate(Date start, Date end) {
        return dao.getDate(start, end);
    }

    public List<JpaEventArtCulture> getAll() {
        return dao.getAll();
    }

    public List<JpaEventArtCulture> find(JpaEventArtCulture obj) {
        return dao.find(obj);
    }

    public JpaEventArtCulture insert(JpaEventArtCulture obj) {
        return dao.insert(obj);
    }

    public JpaEventArtCulture update(JpaEventArtCulture obj) {
        return dao.update(obj);
    }

    public boolean delete(JpaEventArtCulture obj) {
        return dao.delete(obj);
    }
}
