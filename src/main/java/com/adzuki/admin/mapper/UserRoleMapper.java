package com.adzuki.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.adzuki.admin.common.mapper.CoreMapper;
import com.adzuki.admin.entity.UserRole;

public interface UserRoleMapper extends CoreMapper<UserRole> {
	
	@Select(value = "select role_id from admin_user_role where user_id = #{userId}")
	List<Long> listRoleIdsByUserId(@Param("userId") Long userId);
}