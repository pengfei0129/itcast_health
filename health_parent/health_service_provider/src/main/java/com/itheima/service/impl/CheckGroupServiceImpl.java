package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao ;


    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        String queryString = queryPageBean.getQueryString();
        if (queryString != null){
            queryString = queryString.trim();
            queryString = "%" + queryString + "%" ;
        }
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 插入checkGroup
        checkGroupDao.add(checkGroup);
        // 插入中间关系表
        Integer checkGroupId = checkGroup.getId();
        setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 先更新checkGroup
        checkGroupDao.edit(checkGroup);
        // 删除中间关系表
        Integer checkGroupId = checkGroup.getId();
        checkGroupDao.deleteCheckGroupAndCheckItemByCheckGroupId(checkGroupId);
        // 插入新的中间关系表
        setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    @Override
    public void deleteCheckGroup(Integer checkGroupId) {
        // 先删除中间关系表，后删除checkGroup表
        int row = checkGroupDao.deleteCheckGroupAndCheckItemByCheckGroupId(checkGroupId);
        if (row != -1){
            checkGroupDao.deleteCheckGroupById(checkGroupId);
        }else {
            new Exception("删除中间关系表失败。");
        }
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkGroupId",checkGroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
