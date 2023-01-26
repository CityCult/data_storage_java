package org.citycult.datastorage.entity;

import org.citycult.datastorage.util.ToStringHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "venue", schema = org.citycult.datastorage.dao.Constants.DB_PRODUCTIVE_SCHEMA)
public class JpaVenue {

    @Id
    @Column(name = "venue_uid")
    private String venueUid;
    @Column(name = "name")
    private String name;
    @Column(name = "street")
    private String street = Constants.NO_STREET;
    @Column(name = "postcode")
    private String postcode = Constants.NO_POSTCODE;
    @Column(name = "city")
    private String city = Constants.NO_CITY;
    @Column(name = "description")
    private String description = Constants.NO_DESCRIPTION;
    @Column(name = "website")
    private String website = Constants.NO_WEBSITE;

    public JpaVenue() {
        this(Constants.NO_NAME);

    }

    public JpaVenue(String name) {
        venueUid = UUID.randomUUID().toString();
        this.name = name;
    }

    public UUID getVenueUid() {
        return UUID.fromString(venueUid);
    }

    public void setVenueUid(UUID venueUid) {
        this.venueUid = venueUid.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        ToStringHelper str = ToStringHelper.add(this, "venue_uid",  venueUid);
        str.add("name", name);
        str.add("street", street);
        str.add("postcode", postcode);
        str.add("city", city);
        str.add("website", website);
        str.addShort("description", description);
        return str.toString();
    }

    @Override
    public int hashCode() {
        return this.getVenueUid().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JpaVenue) {
            final JpaVenue e = (JpaVenue) obj;
            // TODO(cpieloth): May check for name, street, city, postcode too
            return this.getVenueUid().equals(e.getVenueUid());
        } else
            return false;
    }

}
