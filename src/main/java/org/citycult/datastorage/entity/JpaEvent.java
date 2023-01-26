package org.citycult.datastorage.entity;

import org.citycult.datastorage.util.ToStringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "event", schema = org.citycult.datastorage.dao.Constants.DB_PRODUCTIVE_SCHEMA)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "category_id", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("1")
public class JpaEvent {

    private static final Logger logger = LoggerFactory.getLogger(JpaEvent.class);

    @Id
    @Column(name = "event_uid")
    protected String eventUid;

    @Column(name = "name")
    protected String name;

    @Column(name = "startdate")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date startDate = null;

    @Column(name = "has_starttime")
    protected boolean hasStartTime = false;

    @Column(name = "enddate")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date endDate = null;

    @Column(name = "has_endtime")
    protected boolean hasEndTime = false;

    @JoinColumn(name = "venue_uid")
    @ManyToOne(cascade = CascadeType.PERSIST)
    protected JpaVenue venue = null;

    @Column(name = "description")
    protected String description = Constants.NO_DESCRIPTION;

    @Column(name = "price")
    protected Double price = Constants.UNKNOWN_PRICE;

    // protected Category category;
    @Column(name = "category_id")
    protected Integer categoryId;

    @Column(name = "source")
    protected String source = Constants.NO_SOURCE;

    @Column(name = "createddate")
    protected Timestamp createdDate = null;

    public JpaEvent() {
        this(Constants.NO_NAME, new Date(0));
    }

    public JpaEvent(String name, Date startDate) {
        this(name, new Timestamp(startDate.getTime()));
    }

    public JpaEvent(String name, Timestamp startDate) {
        this.eventUid = UUID.randomUUID().toString();
        this.name = name;
        this.startDate = startDate;
        this.createdDate = new Timestamp(System.currentTimeMillis());
        setCategoryId(Category.MISC.id);
    }

    public UUID getEventUid() {
        return UUID.fromString(eventUid);
    }

    public void setEventUid(UUID eventUid) {
        this.eventUid = eventUid.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public JpaVenue getVenue() {
        return venue;
    }

    public void setVenue(JpaVenue venue) {
        this.venue = (JpaVenue) venue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    protected void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String toString() {
        ToStringHelper str = ToStringHelper.add(this, "event_uid", eventUid);
        str.add("name", name);
        str.add("cateogryId", categoryId);
        str.add("hasStartTime", hasStartTime);
        str.add("startDate", startDate);
        str.add("hasEndTime", hasEndTime);
        str.add("endDate", endDate);
        str.add("price", price);
        str.add("source", source);
        str.addShort("description", description);
        str.add("venue", venue);
        return str.toString();
    }

    public Category getCategory() {
        for (Category c : Category.values()) {
            if (c.id.equals(this.categoryId))
                return c;
        }
        logger.error("Unknown categoryId in use: " + categoryId);
        return Category.MISC;
    }

    public void setStartDateTime(Date startDate) {
        setStartDate(startDate);
        hasStartTime = true;
    }

    public boolean hasStartTime() {
        return hasStartTime;
    }

    public void setStartTimeFlag(boolean flag) {
        hasStartTime = flag;
    }

    public void setEndDateTime(Date endDate) {
        setEndDate(endDate);
        hasEndTime = true;
    }

    public boolean hasEndTime() {
        return hasEndTime;
    }

    public void setEndTimeFlag(boolean flag) {
        hasEndTime = flag;
    }

    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = new Timestamp(createdDate.getTime());
    }

}
