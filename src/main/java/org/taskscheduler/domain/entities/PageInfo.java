package org.taskscheduler.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;


//wrapper for Page<T>
@JsonPropertyOrder({"total", "size", "pageCount", "elements"})
public class PageInfo<T>  {

    private Page<T> page;

    public PageInfo (Page<T> page) {
        this.page = page;

    }

    @JsonIgnore
    public boolean isFirst() {
        return this.page.isFirst();
    }


    @JsonIgnore
    public boolean isLast() {
        return this.page.isLast();
    }


    public boolean hasNext() {
        return this.page.hasNext();
    }


    public boolean hasPrevious() {
        return this.page.hasPrevious();
    }


    public Pageable nextPageable() {
        return this.page.nextPageable();
    }

    public Pageable previousPageable() {
        return this.page.previousPageable();
    }



    @Override
    public String toString() {
       return this.page.toString();
    }

    public Long getTotal() { return this.page.getTotalElements(); }


    public int getPageCount() {
        return this.page.getTotalPages();
    }

    public int getSize() {
        return this.page.getSize();
    }

    public List<T> getElements() {
        return this.page.getContent();
    }

}
