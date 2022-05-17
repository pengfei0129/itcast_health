package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

// 服务接口
public interface CheckItemService {

    public void add(CheckItem checkItem);

    PageResult pageQuery(QueryPageBean queryPageBean);
}
