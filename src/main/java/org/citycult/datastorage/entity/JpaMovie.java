package org.citycult.datastorage.entity;

import org.citycult.datastorage.util.ToStringHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;


@Entity
@Table(name = "movie", schema = org.citycult.datastorage.dao.Constants.DB_PRODUCTIVE_SCHEMA)
public class JpaMovie {

    @Id
    @Column(name = "movie_uid")
    private String movieUid;

    @Column(name = "title")
    private String title;

    @Column(name = "genre")
    private String genre = Constants.NO_GENRE;

    @Column(name = "year")
    private Integer year = Constants.NO_YEAR;

    @Column(name = "runtime")
    private Integer runtime = Constants.NO_RUNTIME;

    @Column(name = "description")
    private String description = Constants.NO_DESCRIPTION;

    public JpaMovie() {
        this(Constants.NO_TITLE);
    }

    public JpaMovie(String title) {
        this.movieUid = UUID.randomUUID().toString();
        this.title = title;
    }

    public UUID getMovieUid() {
        return UUID.fromString(movieUid);
    }

    public void setMovieUid(UUID movieUid) {
        this.movieUid = movieUid.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        ToStringHelper str = ToStringHelper.add(this, "movie_uid", movieUid);
        str.add("title", title);
        str.add("genre", genre);
        str.add("year", year);
        str.add("runtime", runtime);
        str.addShort("description", description);
        return str.toString();
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }
}
