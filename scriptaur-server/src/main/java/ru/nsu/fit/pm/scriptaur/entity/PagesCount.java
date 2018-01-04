package ru.nsu.fit.pm.scriptaur.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PagesCount {
    int pagesCount;

    public int getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }
}
