package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

/**
 * 持久层Dao接口
 */
public interface CheckItemDao {

    public void add(CheckItem checkItem);

    Page<CheckItem> selectByQuery(String queryString);
}
