package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
            return checkGroupService.pageQuery(queryPageBean);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            //新增失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //新增成功
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkGroup);
        }catch (Exception e){
            e.printStackTrace();
            // 查询
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try {
            List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItemIds);
        }catch (Exception e){
            e.printStackTrace();
            // 查询
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            //更新失败
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        //更新成功
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/deleteCheckGroup")
    public Result deleteCheckGroup(Integer id){
        try {
            checkGroupService.deleteCheckGroup(id);
        }catch (Exception e){
            e.printStackTrace();
            //删除失败
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        //删除成功
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> checkGroups = checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkGroups);
        }catch (Exception e){
            e.printStackTrace();
            // 查询
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
