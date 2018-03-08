package com.week7challange.addisnews;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Articles {
    private long id;
   // private Source source;
    //private List<Sources> sources;
    private String author;
    private String title;
    private String description;
    private String url;
    private String publishedAt;
    public Articles() {
    }
    @JsonProperty("newsapi1")
    private List<Articles> articles;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

   /* public List<Source> getSource() {
        return source;
    }

    public void setSource(List<Source> source) {
        this.source = source;
    }*/

   /* public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }*/

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }
}
