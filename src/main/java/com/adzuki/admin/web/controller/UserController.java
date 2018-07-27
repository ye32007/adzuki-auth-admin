package com.adzuki.admin.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.adzuki.admin.annotation.LogAspect;
import com.adzuki.admin.common.constant.CommonErrors;
import com.adzuki.admin.common.data.AuthUser;
import com.adzuki.admin.common.data.PageData;
import com.adzuki.admin.common.data.PageParam;
import com.adzuki.admin.common.data.Result;
import com.adzuki.admin.common.support.SecurityContext;
import com.adzuki.admin.entity.User;
import com.adzuki.admin.manager.UserManager;
import com.adzuki.admin.manager.UserRoleManager;
import com.adzuki.admin.web.query.UserQuery;
import com.adzuki.admin.web.vo.UserVo;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

@RequestMapping("user")
@Controller
public class UserController extends BaseController {
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserRoleManager userRoleManager;


    @RequestMapping("/list")
    public String list() {
        return "user/user_list";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result<?> get(Long id) {
        User user = userManager.findById(id);
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(user, vo);
        return Result.createSuccessResult(vo);
    }

    @LogAspect(type = LogAspect.LogType.Update_User, objectNames = User.class)
    @RequestMapping("/save")
    @ResponseBody
    public Result<?> save(User user) {
        logger.info("save user", user);
        userManager.save(user);
        return Result.createSuccessResult();
    }

    @LogAspect(type = LogAspect.LogType.Reset_Password, argNames = "id")
    @RequestMapping("/resetPassword")
    @ResponseBody
    public Result<?> resetPassword(Long id, String newPassword) {
        logger.info("resetPassword");
        if (id == null) {
            return Result.createFailResult(CommonErrors.PARAM_INVALID, "参数无效！");
        }
        userManager.resetPassword(id, StringUtils.isEmpty(newPassword) ? "123456" : newPassword);
        return Result.createSuccessResult();
    }

    @LogAspect(type = LogAspect.LogType.Update_Password)
    @RequestMapping("/modifyPassword")
    @ResponseBody
    public Result<?> modifyPassword(String oldPassword, String newPassword) {
        logger.info("modifyPassword");
        AuthUser user = SecurityContext.getAuthUser();
        userManager.updatePassword(user.getId(), oldPassword, newPassword);
        return Result.createSuccessResult();
    }

    @LogAspect(type = LogAspect.LogType.Locked_User, argNames = {"id", "locked"})
    @RequestMapping("/updateLocked")
    @ResponseBody
    public Result<?> updateLocked(Long id, Boolean locked) {
        logger.info("updateLocked id:{}, locked:{}", id, locked);
        userManager.updateLocked(id, locked);
        return Result.createSuccessResult();
    }

    @LogAspect(type = LogAspect.LogType.Delete_User, argNames = "id")
    @RequestMapping("/del")
    @ResponseBody
    public Result<?> delete(Long id) {
        logger.info("delete id:{}", id);
        User user = userManager.findById(id);
        user.setDeleted(Boolean.TRUE);
        userManager.update(user);
        return Result.createSuccessResult();
    }

    @RequestMapping("/query")
    @ResponseBody
    public Result<?> query(UserQuery query, PageParam pageParam) {
        logger.info("query params:{}", query);
        // 控制页面只能显示未删除的用户
        Condition condition = new Condition(User.class);
        Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("deleted", false);
        if(query.getLocked() != null){
        	criteria.andEqualTo("locked", query.getLocked());
        }
        if(StringUtils.isNotBlank(query.getChineseName())){
        	criteria.andLike("chineseName", query.getChineseName() + "%");
        }
        if(StringUtils.isNotBlank(query.getUsername())){
        	criteria.andLike("username", query.getUsername() + "%");
        }
        if(StringUtils.isNotBlank(query.getMobile())){
        	criteria.andLike("mobile", query.getMobile() + "%");
        }
        if(StringUtils.isNotBlank(query.getSex())){
        	criteria.andEqualTo("sex", query.getSex());
        }
        Result<PageInfo<User>> result = userManager.queryPage(condition, pageParam);
        List<UserVo> vos = result.getData().getList().stream().map(msg -> {
			return trans(msg);
		}).collect(Collectors.toList());
		PageData<UserVo> pageData = new PageData<>(result.getData().getTotal(), vos);
		return Result.createSuccessResult(pageData);
    }
    
    private UserVo trans(User user){
    	UserVo vo = new UserVo();
    	BeanUtils.copyProperties(user, vo);
    	return vo;
    }

    @RequestMapping("/role")
    public ModelAndView menu(Long userId) {
        ModelAndView mv = new ModelAndView("user/user_role");

        List<Long> idList = userRoleManager.listRoleIdsByUserId(userId);
        mv.addObject("ids", JSON.toJSONString(idList));

        return mv;
    }

    @LogAspect(type = LogAspect.LogType.Distribute_Role, argNames = {"userId", "ids"})
    @RequestMapping("/saveRole")
    @ResponseBody
    public Result<?> saveRole(Long userId, String ids) {
        logger.info("saveRole userId:{}, ids:{}", userId, ids);
        if (userId == null || ids == null) {
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

        userRoleManager.updateUserRole(userId, idArr);

        return Result.createSuccessResult();
    }
}
