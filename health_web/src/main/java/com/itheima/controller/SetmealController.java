package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds) {
        try {
            setmealService.add(setmeal, checkGroupIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.pageQuery(
                queryPageBean.getPageSize(),
                queryPageBean.getCurrentPage(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.edit(setmeal, checkgroupIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        Setmeal setmeal = setmealService.findById(id);
        if (null != setmeal) {
            Result result = new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS);
            result.setData(setmeal);
            return result;
        }
        return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        // 获取原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        // 获得.的位置索引
        int index = originalFilename.lastIndexOf(".");
        // 截取后缀
        String suffix = originalFilename.substring(index - 1);
        // 拼接一个已UUID动态构造的文件名，防止冲突
        String fileName = UUID.randomUUID() + suffix;
        try {
            // 调用七牛云提供SDK实现文件上传
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            // 讲文件名称保存到redis集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
