package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 检查项
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * implements Serializable
 * 一个对象序列化的接口，一个类只有实现了Serializable接口，它的对象才是可序列化的。
 *
 */
public class CheckItem implements Serializable {
    private Integer id;//主键
    private String code;//项目编码
    private String name;//项目名称
    private String sex;//适用性别
    private String age;//适用年龄（范围），例如：20-50
    private Float price;//价格
    private String type;//检查项类型，分为检查和检验两种类型
    private String remark;//项目说明
    private String attention;//注意事项
}
