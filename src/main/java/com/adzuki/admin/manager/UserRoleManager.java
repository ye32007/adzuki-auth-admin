package com.adzuki.admin.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adzuki.admin.common.manager.AbstractManager;
import com.adzuki.admin.entity.UserRole;
import com.adzuki.admin.mapper.UserRoleMapper;

@Service
public class UserRoleManager extends AbstractManager<UserRole> {
	
	@Autowired
	private UserRoleMapper userRoleMapper;
	
	@Transactional
    void deleteByUserId(Long userId) {
    	mapper.delete(UserRole.builder().userId(userId).build());
    }
	
    @Transactional
	void deleteByRoleId(@Param("roleId") Long roleId) {
    	mapper.delete(UserRole.builder().roleId(roleId).build());
	}

	@Transactional
	public void updateUserRole(Long userId, Long... roleIds) {
		deleteByUserId(userId);
		if (roleIds != null && roleIds != null && roleIds.length > 0) {
			List<UserRole> list = new ArrayList<UserRole>();
			for (int i = 0; i < roleIds.length; i++) {
				list.add(UserRole.builder().userId(userId).roleId(roleIds[i]).build());
			}
			super.save(list);
		}
	}
	
	public List<Long> listRoleIdsByUserId(Long userId) {
		return userRoleMapper.listRoleIdsByUserId(userId);
	}
}