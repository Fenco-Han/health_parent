package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    public void add(CheckGroup checkGroup, Integer[] checkItemIds);

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    public void edit(CheckGroup checkGroup, Integer[] checkItemIds);

    public CheckGroup findById(Integer id);

    public List<CheckGroup> findAll();

    public List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);
}
