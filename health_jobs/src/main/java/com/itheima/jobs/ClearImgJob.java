package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        // 计算redis中两个set的差值，获取垃圾图片名称
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        Iterator<String> iterator = sdiff.iterator();
        while (iterator.hasNext()) {
            String pic = iterator.next();
            // 删除图片服务器中的图片
            QiniuUtils.deleteFileFromQiniu(pic);
            // 删除redis中的数据(删除多余图片)
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, pic);
        }
    }
}
