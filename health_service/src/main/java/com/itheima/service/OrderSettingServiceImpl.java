package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if (null != orderSettingList && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                // 检查此日期是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    // 已经存在，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    // 不存在，执行添加操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingsByMonth(String date) {
        String dateBegin = date + "-1";
        String dateEnd = date + "-31";
        HashMap map = new HashMap();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        List<OrderSetting> list = orderSettingDao.getOrderSettingsByMonth(map);
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            HashMap orderSettingMap = new HashMap();
            orderSettingMap.put("date", orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number", orderSetting.getNumber());
            orderSettingMap.put("reservations", orderSetting.getReservations());
            data.add(orderSettingMap);
        }
        return data;
    }


    /**
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            orderSettingDao.add(orderSetting);
        }
    }
}
