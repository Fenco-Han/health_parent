package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;

    /**
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        // 1、套餐新增
        setmealDao.add(setmeal);
        if (null != checkGroupIds && checkGroupIds.length > 0) {
            // 2、将套餐与检查组关联关系入库
            setSetmealAndCheckGroup(setmeal.getId(), checkGroupIds);
        }
        // 将图片名称保存到redis
        savePic2Redis(setmeal.getImg());
    }

    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    /**
     * 分页查询
     * @param pageSize
     * @param currentPage
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer pageSize, Integer currentPage, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        // 1、根据setmealId删除原关联数据
        setmealDao.deleteAssociation(setmeal.getId());

        // 2、向中间表插入新的关联数据
        setSetmealAndCheckGroup(setmeal.getId(), checkGroupIds);

        // 3、更新套餐基础信息
        setmealDao.editById(setmeal);
    }

    /**
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkGroupIds) {
        if (checkGroupIds != null && checkGroupIds.length > 0) {
            for (Integer checkGroupId : checkGroupIds) {
                Map<String, Integer> map = new HashMap<>(128);
                map.put("SetmealId", setmealId);
                map.put("checkgroupId", checkGroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }
}
