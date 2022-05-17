package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

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
}
