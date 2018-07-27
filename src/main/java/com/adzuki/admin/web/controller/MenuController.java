package com.adzuki.admin.web.controller;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Condition;
import com.adzuki.admin.annotation.LogAspect;
import com.adzuki.admin.common.data.Result;
import com.adzuki.admin.entity.Menu;
import com.adzuki.admin.manager.MenuManager;

@RequestMapping("menu")
@Controller
public class MenuController extends BaseController {

    @Autowired
    private MenuManager menuManager;

    @RequestMapping("/list")
    public String list(){
        return "menu/menu_list";
    }
    
    @RequestMapping("/get")
    @ResponseBody
    public Result<?> get(Long id){
    	Menu menu = menuManager.findById(id);
        return Result.createSuccessResult(menu);
    }
    
    @LogAspect(type=LogAspect.LogType.Delete_Menu, argNames={"id"})
    @RequestMapping("/del")
    @ResponseBody
    public Result<?> del(Long id){
    	menuManager.delete(id);
        return Result.createSuccessResult();
    }
    
    @LogAspect(type=LogAspect.LogType.Update_Menu, objectNames=Menu.class)
    @RequestMapping("/save")
    @ResponseBody
    public Result<?> save(Menu menu){
    	menuManager.save(menu);
        return Result.createSuccessResult();
    }   

    @RequestMapping("/queryAll")
    @ResponseBody
    public List<Menu> queryAll() {
    	Condition condition = new Condition(Menu.class);
    	condition.orderBy("level").asc().orderBy("sort").asc();
    	List<Menu> menuList = new ArrayList<>();
    	List<Menu> list = menuManager.findByCondition(condition);
    	for (Menu menu : list) {
			if(StringUtils.isNotBlank(menu.getMemo())) {
				menu.setName(menu.getName() + " 【" + menu.getMemo() + "】");
			}
		}
		Menu root = new Menu();
		root.setId(0L);
		root.setName("菜单管理");
		root.setLevel(0);
		root.setIcon("fa-tasks");
		root.setFolder(true);
		
    	menuList.add(root);
    	menuList.addAll(list);
    	
        return menuList;
    }
}
