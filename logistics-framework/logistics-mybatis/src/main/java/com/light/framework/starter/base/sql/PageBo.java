package com.light.framework.starter.base.sql;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


public class PageBo<E>
        implements Serializable {
    private long total;
    private int pages;
    private int page = 1;

    private int pageSize = 10;
    private int size;

    public PageBo(long total, int pages, List<E> list) {
        this.total = total;
        this.pages = pages;
        this.list = list;
    }


    private List<E> list = Collections.emptyList();

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return this.pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<E> getList() {
        return this.list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
}