package com.itheima.dao;


import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void setSetmealAndCheckGroup(Map<String, Integer> map);

    void add(Setmeal setmeal);

    Page<Setmeal> findByCondition(String queryString);

    Setmeal findById(Integer id);

    void deleteAssociation(Integer id);

    void editById(Setmeal setmeal);

    List<Setmeal> findAll();
}
