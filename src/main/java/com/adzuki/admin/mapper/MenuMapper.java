package com.adzuki.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.adzuki.admin.common.mapper.CoreMapper;
import com.adzuki.admin.entity.Menu;

public interface MenuMapper extends CoreMapper<Menu> {
	
	@Select("<script> select * from admin_menu where id in (select menu_id from admin_role_menu where role_id in"
			+ " <foreach item='item' index='index' collection='roleIds' open='(' separator=', ' close=')'> "
			+ " #{item} "
			+ "</foreach>"
			+ ")</script>")
	List<Menu> findByRoleIds(@Param("roleIds") List<Long> roleIds);
	
}