package com.week7challange.addisnews;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class News {
    private long id;
    private String status;
    private int totalResults;
    private Articles[] articles;

    public News() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
/*  public News(Articles[] articles) {
        this.articles = articles;
    }*/

    /*@JsonProperty("newsapi")
            private List<News> news;*/
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public Articles[] getArticles() {
        return articles;
    }

    public void setArticles(Articles[] articles) {
        this.articles = articles;
    }
}
