package com.adzuki.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.adzuki.admin.common.mapper.CoreMapper;
import com.adzuki.admin.entity.RoleMenu;

public interface RoleMenuMapper extends CoreMapper<RoleMenu> {
	
	@Select(value = "select menu_id from admin_role_menu where role_id = #{roleId} ")
	List<Long> listMenuIdsByRoleId(@Param("roleId") Long roleId);
}