package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 持久层Dao接口
 */
public interface CheckItemDao {

    /**
     * 插入一条新数据
     * @param checkItem
     */
    public void add(CheckItem checkItem);

    /**
     * 根据条件查询CheckItem（code和name）
     * @param queryString
     * @return
     */
    public Page<CheckItem> selectByQuery(String queryString);

    /**
     * 根据检查项id  查询关联检查组数目
     * @param id
     * @return
     */
    public long findCountByCheckItemId(Integer id);

    /**
     * 根据id删除检查项
     * @param id
     */
    public void deleteById(Integer id);

    public CheckItem findById(Integer id);

    public void edit(CheckItem checkItem);

    public List<CheckItem> findAll();
}
