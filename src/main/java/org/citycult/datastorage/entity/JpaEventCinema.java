package org.citycult.datastorage.entity;

import org.citycult.datastorage.dao.Constants;
import org.citycult.datastorage.util.ToStringHelper;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "event_cinema", schema = Constants.DB_PRODUCTIVE_SCHEMA)
@DiscriminatorValue("2")
public class JpaEventCinema extends JpaEvent {

    @JoinColumn(name = "movie_uid")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private JpaMovie movie;

    public JpaEventCinema() {
        this("", new Date(0), null);
    }

    public JpaEventCinema(String name, Date startDate, JpaMovie movie) {
        this(name, new Timestamp(startDate.getTime()), movie);
    }

    public JpaEventCinema(String name, Timestamp startDate, JpaMovie movie) {
        super(name, startDate);
        setCategoryId(Category.CINEMA.id);
        setMovie(movie);
    }

    public JpaMovie getMovie() {
        return movie;
    }

    public void setMovie(JpaMovie movie) {
        this.movie = movie;
    }

    @Override
    public String toString() {
        ToStringHelper str = ToStringHelper.add(this, "super", super.toString());
        if (movie != null) {
            str.add("movie.uid", movie.getMovieUid());
            str.add("movie.title", movie.getTitle());
        }
        return str.toString();
    }
}
