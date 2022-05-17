package com.itheima.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
/**
 * 封装查询条件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QueryPageBean implements Serializable{
    private Integer currentPage;    //页码
    private Integer pageSize;       //每页记录数
    private String queryString;     //查询条件
}