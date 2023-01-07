package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceimpl implements CheckGroupService{
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组
     * @param checkGroup
     * @param checkItemIds
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        // 1、添加检查组
        checkGroupDao.add(checkGroup);
        // 2、设置检查组和检查项关联关系
        setCheckGroupAndCheckItem(checkGroup.getId(), checkItemIds);
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        // 1、根据检查组id删除中间表数据（清理关联关系）
        checkGroupDao.deleteAssociation(checkGroup.getId());
        // 2、向中间表插入新的关联关系
        setCheckGroupAndCheckItem(checkGroup.getId(), checkItemIds);
        // 3、更新检查组基本信息
        checkGroupDao.editById(checkGroup);
    }


    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId) {
        return checkGroupDao.findCheckGroupIdsBySetmealId(setmealId);    }


    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkItemIds) {
        if (checkItemIds != null && checkItemIds.length > 0) {
            for (Integer checkItemId : checkItemIds) {
                Map<String,Integer> map = new HashMap<>(128);
                map.put("checkgroup_id", checkGroupId);
                map.put("checkItem_id", checkItemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
