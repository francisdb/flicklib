package com.flicklib.api;

import com.flicklib.domain.MovieService;
import com.flicklib.domain.MovieType;

public class MovieSearchResult {

    private String url;
    private String idForSite;
    private String title;
    private String alternateTitle;
    private Integer year;
    private String description;

    private MovieService service;
    private MovieType type;

    /**
     * @return the idForSite
     */
    public String getIdForSite() {
        return idForSite;
    }

    /**
     * @param idForSite
     *            the idForSite to set
     */
    public void setIdForSite(String idForSite) {
        this.idForSite = idForSite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the alternateTitle
     */
    public String getAlternateTitle() {
        return alternateTitle;
    }

    /**
     * @param alternateTitle
     *            the alternateTitle to set
     */
    public void setAlternateTitle(String alternateTitle) {
        this.alternateTitle = alternateTitle;
    }

    /**
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public MovieService getService() {
        return service;
    }

    public void setService(MovieService service) {
        this.service = service;
    }

    public void setType(MovieType type) {
        this.type = type;
    }

    public MovieType getType() {
        return type;
    }

}
