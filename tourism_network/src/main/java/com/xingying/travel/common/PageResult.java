package com.xingying.travel.common;

import java.util.List;

/**
 * @Title: 返回分页结果
 * @author: 陈宏松
 * @create: 2018-12-18 14:34
 * @version: 1.0.0
 **/
public class PageResult<T> {

    private Long total;
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
