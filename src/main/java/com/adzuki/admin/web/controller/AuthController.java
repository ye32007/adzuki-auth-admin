package com.adzuki.admin.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Condition;
import com.adzuki.admin.annotation.LogAspect;
import com.adzuki.admin.common.constant.CommonErrors;
import com.adzuki.admin.common.data.AuthUser;
import com.adzuki.admin.common.data.Result;
import com.adzuki.admin.common.filter.SecurityFilter;
import com.adzuki.admin.common.support.SecurityContext;
import com.adzuki.admin.common.utils.SecurityUtils;
import com.adzuki.admin.entity.Menu;
import com.adzuki.admin.entity.Role;
import com.adzuki.admin.entity.User;
import com.adzuki.admin.manager.MenuManager;
import com.adzuki.admin.manager.RoleManager;
import com.adzuki.admin.manager.UserManager;
import com.adzuki.admin.vo.LoginUser;

@Controller
public class AuthController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private MenuManager menuManager;
	
    /***
     * 登录页面
     * @return
     */
	@RequestMapping(value = "/login", method=RequestMethod.GET)
    public String login(){
        return "auth/login";
    }
	
	/***
	 * 登录操作
	 * @param uname		用户名
	 * @param pword		密码
	 * @param remenber	记住密码
	 * @param session	会话对象
	 * @return 登录结果
	 */
	@LogAspect(type=LogAspect.LogType.Login)
	@RequestMapping(value = "/login", method=RequestMethod.POST)
	@ResponseBody
    public Result<?> authLogin(String uname, String pword, Integer remenber, HttpSession session){
		logger.info("login uname:{}", uname);
		if(StringUtils.isEmpty(uname) || StringUtils.isEmpty(pword)) {
			return Result.createFailResult(CommonErrors.PARAM_INVALID, "参数无效！");
		}
		//获取用户信息
		User user = userManager.findByUsername(uname);
		if(user == null) {
			return Result.createFailResult("用户名或密码错误！");
		}
		if(!SecurityUtils.encryptPassword(pword).equals(user.getPassword())) {
			return Result.createFailResult("用户名或密码错误！");
		}
		if(user.getLocked()) {
			return Result.createFailResult("帐号已被锁定！");
		}
		if(user.getDeleted()) {
			return Result.createFailResult("帐号已被删除！");
		}
		//获取用户角色和菜单
		LoginUser loginUser = new LoginUser(user);
		List<Role> roles = roleManager.findByUserId(loginUser.getId());
		loginUser.setRoles(roles);
		
		List<Long> idList = new ArrayList<>();
		for (Role role : roles) {
			if(!role.getLocked()) {
				idList.add(role.getId());
			}
		}
		if(roles.size() > 0) {
			List<Long> ids = new ArrayList<>();
			for (int i = 0; i < roles.size(); i++) {
				ids.add(roles.get(i).getId());
			}
			List<Menu> menus = menuManager.findByRoleIds(ids);
			loginUser.setMenus(createMenuTree(menus));
			loginUser.setPermits(createPermit(menus));
		}
		logger.info("{} roles:{}", uname, roles);
		
		//用户登录信息存放到session
		session.setAttribute(SecurityFilter.SESSION_USER_KEY, loginUser);

		return Result.createSuccessResult();
    }

	/***
	 * 修改密码页面
	 * @return
	 */
	@RequestMapping(value = "/password")
    public String password(){
        return "auth/password";
    }
	
	/***
	 * 修改密码操作
	 * @param oldPassword	旧密码
	 * @param newPassword	新密码
	 * @return
	 */
	@LogAspect(type=LogAspect.LogType.Update_Password)
	@RequestMapping(value = "/modifyPassword")
	@ResponseBody
    public Result<?> modifyPassword(String oldPassword, String newPassword){
		logger.info("modifyPassword");
		if(StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
			return Result.createFailResult(CommonErrors.PARAM_INVALID, "参数无效！");
		}
		
		AuthUser loginUser = SecurityContext.getAuthUser();
		userManager.updatePassword(loginUser.getId(), oldPassword, newPassword);
		logger.info("modifyPassword username:{}", loginUser.getUsername());
		
		return Result.createSuccessResult();
	}
	
	/***
	 * 退出登录
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "logout")
	@ResponseBody
    public Result<?> logout(HttpSession session){
		logger.info("logout");
		session.removeAttribute("loginUser");
		return Result.createSuccessResult();
    }
	
	/***
	 * 构建菜单数结构
	 * @param menuList 无结构菜单列表
	 * @return
	 */
	private List<Menu> createMenuTree(List<Menu> menuList) {
    	Map<Long, Menu> menuMap = new HashMap<Long,Menu>();
    	
    	List<Menu> treeMenus = new ArrayList<Menu>();
    	for (Menu menu : menuList) {
			Menu parent = menuMap.get(menu.getPid());
			
			if(parent == null) {
				treeMenus.add(menu);
			} else {
				if(parent.getChildrens() == null) {
					parent.setChildrens(new ArrayList<Menu>());
				}
				parent.getChildrens().add(menu);
			}
			menuMap.put(menu.getId(), menu);
		}
    	
    	return treeMenus;
	}
	
	/***
	 * 根据菜单项构造权限列表
	 * @param menus 菜单列表
	 * @return
	 */
	private List<String> createPermit(List<Menu> menus) {
    	List<String> permits = new ArrayList<String>();
    	
    	for (Menu menu : menus) {
    		if(StringUtils.isBlank(menu.getPermit())) {
    			continue;
    		}
    		String[] permitArr = menu.getPermit().split(";|\n");
    		for (String permit : permitArr) {
				if(StringUtils.isNotBlank(permit)) {
					permits.add(permit);
				}
			}
		}
    	return permits;
	}
	
	@RequestMapping(value = "/forget", method = RequestMethod.GET)
	public String forget() {
		return "auth/forget";
	}

	@LogAspect(type=LogAspect.LogType.Forget_Password)
	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> forgetPassword(String username, String mobile) {
		logger.info("forgetPassword");
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(mobile)) {
			return Result.createFailResult(CommonErrors.PARAM_INVALID, "参数无效！");
		}
		Condition condition = new Condition(User.class);
		condition.createCriteria().orEqualTo("username", username).orEqualTo("mobile", mobile);
		List<User> users = userManager.findByCondition(condition);
		if (users == null || users.size() == 0) {
			return Result.createFailResult("用户名不存在！");
		}
		User user = users.get(0);
		Integer newPassword = (int) ((Math.random() * 9 + 1) * 100000);
		userManager.resetPassword(user.getId(), newPassword.toString());
		return Result.createSuccessResult(newPassword);
	}
}
