package com.adzuki.admin.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adzuki.admin.common.manager.AbstractManager;
import com.adzuki.admin.entity.Menu;
import com.adzuki.admin.entity.RoleMenu;
import com.adzuki.admin.exception.CommonException;
import com.adzuki.admin.mapper.MenuMapper;
import com.adzuki.admin.mapper.RoleMenuMapper;

@Service
public class MenuManager extends AbstractManager<Menu> {
	
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	
	
	@Transactional
	public int save(Menu menu) {
		logger.info("save menu:{}", menu);
		if(menu.getId() == null) {
			if(menu.getSort() == null) {
				mapper.insertSelective(menu);
				Long sort = menu.getId() * 10 + 1;
				menu.setSort(sort.intValue());
				mapper.updateByPrimaryKey(menu);
				return 1;
			} else {
				return super.save(menu);
			}
		} else {
			Menu dbMenu = mapper.selectByPrimaryKey(menu.getId());
			if (dbMenu == null) {
				throw new CommonException("菜单不存在或已删除！");
			}
			return super.update(menu);
		}
	}
	
	@Transactional
    public void delete(Long id) {
        logger.info("delete id:{}", id);
        if(mapper.selectCount(Menu.builder().pid(id).build()) > 0) {
			throw new CommonException("存在子菜单，不能删除！");
        }
        super.deleteById(id);
        roleMenuMapper.delete(RoleMenu.builder().menuId(id).build());
    }
    
    public List<Menu> findByRoleIds(List<Long> roleIds) {
    	return menuMapper.findByRoleIds(roleIds);
    }
}