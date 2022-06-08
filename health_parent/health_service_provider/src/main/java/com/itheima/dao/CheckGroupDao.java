package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    public Page<CheckGroup> selectByCondition(String queryString) ;

    public int add(CheckGroup checkGroup);

    public int setCheckGroupAndCheckItem(Map<String, Integer> map);

    public CheckGroup findById(Integer id);

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    public int edit(CheckGroup checkGroup);

    public int deleteCheckGroupAndCheckItemByCheckGroupId(Integer checkGroupId);

    public void deleteCheckGroupById(Integer checkGroupId);

    public List<CheckGroup> findAll();
}
