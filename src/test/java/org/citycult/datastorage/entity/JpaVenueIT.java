package org.citycult.datastorage.entity;

import junit.framework.Assert;
import org.citycult.datastorage.dao.JpaEntityDaoFactory;
import org.citycult.datastorage.dao.JpaVenueDao;
import org.junit.Test;

/**
 * @author cpieloth
 */
public class JpaVenueIT {

    @Test
    public void equalsHashCode() throws Exception {
        JpaVenue v1 = new JpaEntityFactory().createVenue();
        v1.setName("Test venue");
        v1.setCity("City");
        v1.setStreet("street");

        JpaVenueDao dao = JpaEntityDaoFactory.getInstance().getVenueDao();
        JpaVenue v2 = dao.insert(v1);
        Assert.assertNotNull(v2);
        Assert.assertTrue(v1.equals(v2));
        Assert.assertTrue(v1.hashCode() == v2.hashCode());

        v2 = dao.get(v1.getVenueUid());
        Assert.assertNotNull(v2);
        Assert.assertTrue(v1.equals(v2));
        Assert.assertTrue(v1.hashCode() == v2.hashCode());

    }
}
