package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /** 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除检查项
     * @param id
     */
    @Override
    public void delete(Integer id) {
        // 查询当前检查项是否和检查组关联
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            // 当前检查项被引用，不能删除
            throw new RuntimeException("当前检查项被引用，不能删除");
        }
        checkItemDao.deleteById(id);
    }

    /**
     *  编辑检查组
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.editById(checkItem);
    }

    /**
     * 根据ID查询检查项
     * @param id
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * @param checkGroupId
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer checkGroupId) {
        return checkItemDao.findCheckItemIdsByCheckGroupId(checkGroupId);
    }
}

