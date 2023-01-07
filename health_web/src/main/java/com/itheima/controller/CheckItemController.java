package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    // 新增
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    // 分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
                );
        return pageResult;
    }

    // 删除检查项
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkItemService.delete(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    // 编辑检查项
    @RequestMapping("edit")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    // 根据id查询检查项
    @RequestMapping("findById")
    public Result findById(Integer id) {
        CheckItem checkItem = checkItemService.findById(id);
        if (checkItem != null) {
            Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS);
            result.setData(checkItem);
            return result;
        }
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }

    // 查询所有检查项
    @RequestMapping("findAll")
    public Result findAll() {
        List<CheckItem> checkItemList = checkItemService.findAll();
        if (null != checkItemList && checkItemList.size() > 0) {
            Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS);
            result.setData(checkItemList);
            return result;
        }
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }

    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemIdsByCheckGroupId(@RequestParam Integer checkGroupId) {
        return checkItemService.findCheckItemIdsByCheckGroupId(checkGroupId);
    }
}
