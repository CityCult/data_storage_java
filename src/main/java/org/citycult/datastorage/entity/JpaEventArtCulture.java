package org.citycult.datastorage.entity;

import org.citycult.datastorage.dao.Constants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;


@Entity
@Table(name = "event", schema = Constants.DB_PRODUCTIVE_SCHEMA)
@DiscriminatorValue("6")
public class JpaEventArtCulture extends JpaEvent {

    public JpaEventArtCulture() {
        this("", new Date(0));
    }

    public JpaEventArtCulture(String name, Date startDate) {
        this(name, new Timestamp(startDate.getTime()));
    }

    public JpaEventArtCulture(String name, Timestamp startDate) {
        super(name, startDate);
        setCategoryId(Category.ARTCULTURE.id);
    }
}

