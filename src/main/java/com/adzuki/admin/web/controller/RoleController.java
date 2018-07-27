package com.adzuki.admin.web.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.adzuki.admin.annotation.LogAspect;
import com.adzuki.admin.common.constant.CommonErrors;
import com.adzuki.admin.common.data.PageData;
import com.adzuki.admin.common.data.PageParam;
import com.adzuki.admin.common.data.Result;
import com.adzuki.admin.entity.Role;
import com.adzuki.admin.manager.RoleManager;
import com.adzuki.admin.manager.RoleMenuManager;
import com.adzuki.admin.web.query.RoleQuery;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

@RequestMapping("role")
@Controller
public class RoleController extends BaseController {
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RoleMenuManager roleMenuManager;

    @RequestMapping("/list")
    public String list() {
        return "role/role_list";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result<?> get(Long id) {
        logger.info("get id:{}", id);
        Role role = roleManager.findById(id);
        return Result.createSuccessResult(role);
    }

    @LogAspect(type = LogAspect.LogType.Delete_Role, argNames = {"id"})
    @RequestMapping("/del")
    @ResponseBody
    public Result<?> del(Long id) {
        logger.info("delete id:{}", id);
        roleManager.delete(id);
        return Result.createSuccessResult();
    }

    @LogAspect(type = LogAspect.LogType.Update_Role, objectNames = {Role.class})
    @RequestMapping("/save")
    @ResponseBody
    public Result<?> save(Role role) {
        logger.info("save role:{}", role);
        if (role.getId() == null) {
            role.setLocked(false);
            role.setSystem(false);
            role.setAssignable(true);
        }
        roleManager.save(role);
        return Result.createSuccessResult();
    }

    @LogAspect(type = LogAspect.LogType.Locked_Role, argNames = {"id"})
    @RequestMapping("/updateLocked")
    @ResponseBody
    public Result<?> updateLocked(Long id, Boolean locked) {
        logger.info("updateLocked id:{}, locked:{}", id, locked);
        roleManager.updateLocked(id, locked);
        return Result.createSuccessResult();
    }

    @RequestMapping("/query")
    @ResponseBody
    public Result<?> query(RoleQuery query ,PageParam pageParam) {
    	Condition condition = new Condition(Role.class);
        Criteria criteria = condition.createCriteria();
        if(StringUtils.isNoneBlank(query.getCode())){
        	criteria.andLike("code", query.getCode() + "%");
        }
        if(StringUtils.isNoneBlank(query.getName())){
        	criteria.andLike("name", query.getName() + "%");
        }
        if(query.getLocked() != null){
        	criteria.andEqualTo("locked", query.getLocked());
        }
        Result<PageInfo<Role>> result = roleManager.queryPage(condition, pageParam);
        PageData<Role> pageData = new PageData<>(result.getData().getTotal(), result.getData().getList());
        return Result.createSuccessResult(pageData);
    }

    @RequestMapping("/query4alloc")
    @ResponseBody
    public Result<?> query4alloc() {
        logger.info("query4alloc");
        Condition condition = new Condition(Role.class);
        condition.createCriteria().andEqualTo("assignable", true).andEqualTo("locked", false);
        List<Role> roleList = roleManager.findByCondition(condition);
        return Result.createSuccessResult(roleList);
    }

    @RequestMapping("/menu")
    public ModelAndView menu(Long roleId) {
        ModelAndView mv = new ModelAndView("role/role_menu");
        List<Long> idList = roleMenuManager.listMenuIdsByRoleId(roleId);
        mv.addObject("ids", JSON.toJSONString(idList));
        return mv;
    }

    @LogAspect(type = LogAspect.LogType.Distribute_Menu, argNames = {"roleId", "ids"})
    @RequestMapping("/saveMenu")
    @ResponseBody
    public Result<?> saveMenu(Long roleId, String ids) {
        logger.info("saveMenu roleId:{}, ids:{}", roleId, ids);
        if (roleId == null || ids == null) {
            return Result.createFailResult(CommonErrors.PARAM_INVALID, "参数无效！");
        }
        Long[] idArr = null;
        if (StringUtils.isNotEmpty(ids)) {
            String[] arr = ids.split(",");
            idArr = new Long[arr.length];
            for (int i = 0; i < arr.length; i++) {
                idArr[i] = Long.parseLong(arr[i]);
            }
        }
        roleMenuManager.updateRoleMenu(roleId, idArr);
        return Result.createSuccessResult();
    }
}
