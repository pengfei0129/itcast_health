package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;
import java.util.Set;

/**
 * 自定义Job，实现定时清理垃圾图片
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool ;

    public void clearImg(){
        // 根据Redis中保存的两个set集合进行计算，获得垃圾图片的名称集合
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        // 删除七牛云上的垃圾图片和Redis中的垃圾图片名称
        if (sdiff != null){
            sdiff.forEach(e -> {
                QiniuUtils.deleteFileFromQiniu(e);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,e);
            });
        }
    }
}
