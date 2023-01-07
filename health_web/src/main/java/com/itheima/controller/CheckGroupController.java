package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkItemIds) {
        try {
            checkGroupService.add(checkGroup, checkItemIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkItemIds) {
        try {
            checkGroupService.edit(checkGroup, checkItemIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        CheckGroup checkGroup = checkGroupService.findById(id);
        if (null != checkGroup) {
            Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroup);
            return result;
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> list = checkGroupService.findAll();
        if (null != list && list.size() > 0) {
            Result  result = new Result (true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(list);
            return result;
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    @RequestMapping("/findCheckGroupIdsBySetmealId")
    public List<Integer> findCheckGroupIdsBySetmealId(@RequestParam Integer setmealId) {
        return checkGroupService.findCheckGroupIdsBySetmealId(setmealId);
    }
}
