package com.adzuki.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.adzuki.admin.common.mapper.CoreMapper;
import com.adzuki.admin.entity.Role;

public interface RoleMapper extends CoreMapper<Role> {
	
	
	@Select(value = "select * from admin_role where id in (select role_id from admin_user_role where user_id = #{userId})")
	List<Role> findByUserId(@Param("userId") Long userId);
	
}