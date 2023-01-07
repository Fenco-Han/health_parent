package com.itheima.service;


import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    public void add(Setmeal setmeal, Integer[] checkGroupIds);

    public PageResult pageQuery(Integer pageSize, Integer currentPage, String queryString);

    public Setmeal findById(Integer id);

    public void edit(Setmeal setmeal, Integer[] checkGroupIds);

    public List<Setmeal> findAll();
}
