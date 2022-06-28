package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer; // 将要用到的freeMarker的bean注册进来（spring-service.xml中配置了模板位置与字符集）

    @Value("${out_put_path}")
    private String outPutPath; // 从属性文件中读取要生成的html对应的目录

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 先插入setmeal表，再插入setmeal_checkgroup 表
        setmealDao.add(setmeal);
        if (checkgroupIds != null && checkgroupIds.length > 0){
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
        // 将图片名保存到Redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

        // 新增套餐后需要重新生成静态页面
        // generateMobileStaticHtml();
    }

    public void generateMobileStaticHtml(){
        // 生成静态页面之前查询数据
        List<Setmeal> setmealList = setmealDao.findAll();
        // 需要生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        // 需要生成套餐详情静态页面
        generateMobileSetmealDetailHtml(setmealList);
    }

    // 需要生成套餐详情静态页面(可能有多个)
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        Map map = new HashMap();
        // 为模板提供数据，用于生成静态页面    此处key setmealList  对应模板文件中的数据吗，名字必须一致
        map.put("setmealList",setmealList);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    // 需要生成套餐列表静态页面
    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("setmeal", this.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    dataMap);
        }
    }


    // 生成静态页面
    public void generateHtml(String templateName, String htmlPageName,Map<String, Object> dataMap){
        Configuration configuration = freeMarkerConfigurer.getConfiguration(); // 获得配置对象
        Writer out = null ;
        try {
            Template template = configuration.getTemplate(templateName);
            out = new FileWriter(new File(outPutPath + "/" + htmlPageName)); // 构造输出流
            // 输出文件
            template.process(dataMap,out);
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        String queryString = queryPageBean.getQueryString();
        if (queryString != null){
            queryString = queryString.trim();
            queryString = "%" + queryString + "%" ;
        }
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        // 根据传入的setmeal更新setmeal表中内容
        setmealDao.updeteById(setmeal);
        // 删除原先checkgroupIds
        setmealDao.deleteSetmealAndCheckGroupBySetmealId(setmeal.getId());
        // 插入新的checkgroupIds
        if (checkgroupIds != null && checkgroupIds.length > 0){
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
    }

    @Override
    public void deleteSetmeal(Integer id) {
        // 删除关联表
        setmealDao.deleteSetmealAndCheckGroupBySetmealId(id);
        // 删除数据项
        setmealDao.deleteSetmealById(id);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmeal_id",id);
            map.put("checkgroup_id",checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }
}
