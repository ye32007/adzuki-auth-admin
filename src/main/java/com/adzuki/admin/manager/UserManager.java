package com.adzuki.admin.manager;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adzuki.admin.common.constant.CommonErrors;
import com.adzuki.admin.common.manager.AbstractManager;
import com.adzuki.admin.common.support.SecurityContext;
import com.adzuki.admin.common.utils.SecurityUtils;
import com.adzuki.admin.entity.User;
import com.adzuki.admin.entity.UserRole;
import com.adzuki.admin.exception.CommonException;
import com.adzuki.admin.mapper.UserRoleMapper;
import com.adzuki.admin.vo.LoginUser;

@Service
public class UserManager extends AbstractManager<User> {

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Transactional
	public void delete(Long id) {
		logger.info("delete id:{}", id);
		userRoleMapper.delete(UserRole.builder().userId(id).build());
		super.deleteById(id);
	}

	@Transactional
	public int save(User user) {
		logger.info("save user:{}", user);

		User dbUser = null;
		if (user.getId() != null) {
			dbUser = mapper.selectByPrimaryKey(user.getId());
			if (dbUser == null) {
				throw new CommonException("用户未找到或已删除！");
			}
		}
		// 用户名唯一验证
		if (usernameExist(user, dbUser)) {
			throw new CommonException("用户名已存在！");
		}
		// 手机号唯一验证
		if (StringUtils.isNotEmpty(user.getMobile())
				&& mobileExist(user, dbUser)) {
			throw new CommonException("手机号码已存在！");
		}
		// 邮箱唯一验证
		if (StringUtils.isNotEmpty(user.getEmail()) && emailExist(user, dbUser)) {
			throw new CommonException("邮箱地址已存在！");
		}
		// 新增用户
		if (user.getId() == null) {
			user.setLocked(false);
			user.setPassword("123456");
			user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
			super.save(user);

			// 根据用户类型，添加默认角色
			// UserRole ur = new UserRole();
			// ur.setRoleId(userRoleMap.get(user.getUserType()));
			// ur.setUserId(user.getId());
			// userRoleMapper.insert(ur);
			return 1;
		} else {
			// 修改用户
			dbUser.setUsername(user.getUsername());
			dbUser.setSex(user.getSex());
			dbUser.setChineseName(user.getChineseName());
			dbUser.setMobile(user.getMobile());
			dbUser.setNickname(user.getNickname());
			dbUser.setEmail(user.getEmail());
			dbUser.setMemo(user.getMemo());
			dbUser.setAppEnabled(user.getAppEnabled());
			return super.update(dbUser);
		}
	}

	public User findByUsername(String username) {
		return mapper.selectOne(User.builder().username(username).build());
	}

	@Transactional
	public void updateLocked(Long id, Boolean locked) {
		User dbUser = mapper.selectByPrimaryKey(id);
		if (dbUser == null) {
			throw new CommonException("用户未找到或已删除！");
		}
		// 用户身份
		LoginUser user = (LoginUser) SecurityContext.getAuthUser();
		if (!user.hasRole("ROLE_ADMIN")) {
			throw new CommonException("没有权限！");
		}
		mapper.updateByPrimaryKeySelective(User.builder().id(id).locked(locked).build());
	}

	@Transactional
	public void resetPassword(Long id, String newPassword) {
		User user = mapper.selectByPrimaryKey(id);
		if (user == null) {
			throw new CommonException(CommonErrors.DATA_NOT_FOUND, "用户未找到或已删除！");
		}
		mapper.updateByPrimaryKeySelective(User.builder().id(id).password(SecurityUtils.encryptPassword(newPassword)).build());
	}

	@Transactional
	public void updatePassword(Long id, String oldPassword, String newPassword) {
		User user = mapper.selectByPrimaryKey(id);
		if (user == null) {
			throw new CommonException(CommonErrors.DATA_NOT_FOUND, "用户未找到或已删除！");
		}
		if (!SecurityUtils.encryptPassword(oldPassword).equals(user.getPassword())) {
			throw new CommonException("原密码错误！");
		}
		mapper.updateByPrimaryKeySelective(User.builder().id(id).password(SecurityUtils.encryptPassword(newPassword)).build());
	}

	@Transactional
	public void updatePasswordExt(Long id, String oldPassword,
			String newPassword) {
		User user = mapper.selectByPrimaryKey(id);
		if (user == null) {
			throw new CommonException(CommonErrors.DATA_NOT_FOUND, "用户未找到或已删除！");
		}
		String ext = user.getExt();
		if (StringUtils.isEmpty(ext)) {
			ext = SecurityUtils.encryptPassword("0000");
		}
		if (!SecurityUtils.encryptPassword(oldPassword).equals(ext)) {
			throw new CommonException("原密码错误！");
		}
		mapper.updateByPrimaryKeySelective(User.builder().id(id).ext(SecurityUtils.encryptPassword(newPassword)).build());
	}

	public List<User> queryByIds4Channel(Long[] ids) {
		String temp = StringUtils.join(ids, ",");
		return super.findByIds(temp);
	}

	private boolean usernameExist(User user, User dbUser) {
		if (user.getId() == null || !Objects.equals(user.getUsername(), dbUser.getUsername())) {
			return mapper.selectCount(User.builder().username(user.getUsername()).build()) > 0;
		}
		return false;
	}

	private boolean mobileExist(User user, User dbUser) {
		if (user.getId() == null || !Objects.equals(user.getMobile(), dbUser.getMobile())) {
			return mapper.selectCount(User.builder().mobile(user.getMobile()).build()) > 0;
		}
		return false;
	}

	private boolean emailExist(User user, User dbUser) {
		if (user.getId() == null || !Objects.equals(user.getEmail(), dbUser.getEmail())) {
			return mapper.selectCount(User.builder().email(user.getEmail()).build()) > 0;
		}
		return false;
	}
}