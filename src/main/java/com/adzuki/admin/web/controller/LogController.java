package com.adzuki.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.adzuki.admin.common.data.PageData;
import com.adzuki.admin.common.data.PageParam;
import com.adzuki.admin.common.data.Result;
import com.adzuki.admin.entity.OperLog;
import com.adzuki.admin.manager.OperLogManager;
import com.adzuki.admin.web.query.LogQuery;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("log")
public class LogController extends BaseController {

    @Autowired
    private OperLogManager operLogManager;

    @RequestMapping(value = {"list"})
    public String list() {
        return "operlog/list";
    }

    @RequestMapping(value = {"queryPage"})
    @ResponseBody
    public Result<?> queryPage(LogQuery query, PageParam pageParam) {
        Condition condition = new Condition(OperLog.class);
        Criteria criteria = condition.createCriteria();
        Result<PageInfo<OperLog>> result = operLogManager.queryPage(condition, pageParam);
        PageData<OperLog> pageData = new PageData<>(result.getData().getTotal(), result.getData().getList());
        return Result.createSuccessResult(pageData);
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result<?> get(Long id) {
        logger.info("查询日志信息，id={}", id);
        OperLog log = operLogManager.findById(id);
        logger.info("查询日志信息返回结果：{}", JSON.toJSONString(log));
        return Result.createSuccessResult(log);
    }

}
