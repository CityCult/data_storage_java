package org.citycult.datastorage.dao;

import org.citycult.datastorage.dao.helper.JpaEventCategoryDao;
import org.citycult.datastorage.entity.JpaEventNightlife;
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
public class JpaEventNightlifeDao {

    private static final Logger log = LoggerFactory.getLogger(JpaEventNightlifeDao.class);

    private final JpaEventCategoryDao<JpaEventNightlife> dao;

    private static final JpaEventNightlifeDao instance = new JpaEventNightlifeDao();

    public static JpaEventNightlifeDao getInstance() {
        return instance;
    }

    private JpaEventNightlifeDao() {
        dao = new JpaEventCategoryDao<JpaEventNightlife>(JpaEventNightlife.class, log);
    }

    public JpaEventNightlife get(UUID uid) {
        return dao.get(uid);
    }

    public List<JpaEventNightlife> getForVenue(JpaVenue venue) {
        return dao.getForVenue(venue);
    }

    public List<JpaEventNightlife> getForVenue(JpaVenue venue, DateHelper.DateRange range) {
        return dao.getForVenue(venue, range);
    }

    public List<JpaEventNightlife> getDate(DateHelper.DateRange range) {
        return dao.getDate(range);
    }
    public List<JpaEventNightlife> getDate(Date start, Date end) {
        return dao.getDate(start, end);
    }

    public List<JpaEventNightlife> getAll() {
        return dao.getAll();
    }

    public List<JpaEventNightlife> find(JpaEventNightlife obj) {
        return dao.find(obj);
    }

    public JpaEventNightlife insert(JpaEventNightlife obj) {
        return dao.insert(obj);
    }

    public JpaEventNightlife update(JpaEventNightlife obj) {
        return dao.update(obj);
    }

    public boolean delete(JpaEventNightlife obj) {
        return dao.delete(obj);
    }
}
