package com.adzuki.admin.manager;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adzuki.admin.common.constant.CommonErrors;
import com.adzuki.admin.common.manager.AbstractManager;
import com.adzuki.admin.entity.Role;
import com.adzuki.admin.entity.RoleMenu;
import com.adzuki.admin.entity.UserRole;
import com.adzuki.admin.exception.CommonException;
import com.adzuki.admin.mapper.RoleMapper;
import com.adzuki.admin.mapper.RoleMenuMapper;
import com.adzuki.admin.mapper.UserRoleMapper;

@Service
public class RoleManager extends AbstractManager<Role> {

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	
	public List<Role> findByUserId(Long userId) {
		return roleMapper.findByUserId(userId);
	}
	
	@Transactional
    public int save(Role role) {
        logger.info("save role:{}", role);
        Role dbRole = null;
        if (role.getId() != null) {
            dbRole = mapper.selectByPrimaryKey(role.getId());
            if (dbRole == null) {
                throw new CommonException(CommonErrors.DATA_NOT_FOUND, "角色不存存或已删除！");
            }
            if (dbRole.getSystem()) {
                throw new CommonException("系统角色，不能修改！");
            }
        }
        //角色代码唯一校验
        if (codeExist(role, dbRole)) {
            throw new CommonException("角色代码已存在！");
        }
        //角色名称唯一校验
        if (nameExist(role, dbRole)) {
            throw new CommonException("角色名称已存在！");
        }
        if(role.getId() == null){
        	return super.save(role);
        }else{
        	return super.update(role);
        }
    }


    @Transactional
    public void delete(Long id) {
        logger.info("delete id:{}", id);
        super.deleteById(id);
        roleMenuMapper.delete(RoleMenu.builder().roleId(id).build());
        userRoleMapper.delete(UserRole.builder().roleId(id).build());
    }

    @Transactional
    public void updateLocked(Long id, Boolean locked) {
        Role dbRole = mapper.selectByPrimaryKey(id);
        if (dbRole == null) {
            throw new CommonException(CommonErrors.DATA_NOT_FOUND, "角色不存存或已删除！");
        }
        if (dbRole.getSystem()) {
            throw new CommonException("系统角色，不能锁定/解锁！");
        }
        mapper.updateByPrimaryKeySelective(Role.builder().id(id).locked(locked).build());
    }

    private boolean codeExist(Role role, Role dbRole) {
        if (role.getId() == null || !Objects.equals(role.getCode(), dbRole.getCode())) {
            return mapper.selectCount(Role.builder().code(role.getCode()).build()) > 0;
        }
        return false;
    }

    private boolean nameExist(Role role, Role dbRole) {
        if (role.getId() == null || !Objects.equals(role.getName(), dbRole.getName())) {
        	return mapper.selectCount(Role.builder().name(role.getName()).build()) > 0;
        }

        return false;
    }
}