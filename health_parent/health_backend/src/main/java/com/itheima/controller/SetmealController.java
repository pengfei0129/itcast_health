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

//    @Autowired
//    private JedisPool jedisPool ;

    // 文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        // 获取原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index - 1);
        String fileName =  UUID.randomUUID().toString() + suffix;
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes() ,fileName);
            // 图片上传成功
            Result result =  new Result(true , MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
            result.setData(fileName);
            // 将上传图片名称存入Redis，基于Redis的Set集合
//            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal , Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult result = setmealService.findPage(queryPageBean);
        return new PageResult(result.getTotal(),result.getRows());
    }
}
