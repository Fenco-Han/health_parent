package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    void add(CheckGroup checkGroup);

    Page<CheckGroup> findByCondition(String queryString);

    CheckGroup findById(Integer id);
    Map findCheckGroupById();

    void deleteAssociation(Integer id);

    void editById(CheckGroup checkGroup);

    List<CheckGroup> findAll();

    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);
}
