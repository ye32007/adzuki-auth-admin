package com.adzuki.admin.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adzuki.admin.common.manager.AbstractManager;
import com.adzuki.admin.entity.RoleMenu;
import com.adzuki.admin.mapper.RoleMenuMapper;

@Service
public class RoleMenuManager extends AbstractManager<RoleMenu> {
	
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	
	@Transactional
	public void deleteByMenuId(@Param("menuId") Long menuId) {
		mapper.delete(RoleMenu.builder().menuId(menuId).build());
	}

	@Transactional
	public void deleteByRoleId(@Param("roleId") Long roleId) {
		mapper.delete(RoleMenu.builder().roleId(roleId).build());
	}

	@Transactional
	public void updateRoleMenu(Long roleId, Long... menuIds) {
		this.deleteByRoleId(roleId);
		if (menuIds != null && menuIds != null && menuIds.length > 0) {
			List<RoleMenu> list = new ArrayList<RoleMenu>();
			for (int i = 0; i < menuIds.length; i++) {
				list.add(RoleMenu.builder().roleId(roleId).menuId(menuIds[i]).build());
			}
			super.save(list);
		}
	}
	
	public List<Long> listMenuIdsByRoleId(Long roleId) {
		return roleMenuMapper.listMenuIdsByRoleId(roleId);
	}
}