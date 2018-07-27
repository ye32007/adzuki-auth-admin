package com.adzuki.admin.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.util.AntPathMatcher;

import com.adzuki.admin.common.data.AuthUser;
import com.adzuki.admin.entity.Menu;
import com.adzuki.admin.entity.Role;
import com.adzuki.admin.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUser implements AuthUser {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date createTime;
	private String username;
	private String mobile;
	private String email;
	private Integer companyId;
	private String headImg;
	private String nickname;
	private String chineseName;
	private String sex;
	private String birthday;
	private String location;
	private String registerApp;
	private String dataRange;

	private List<Role> roles = new ArrayList<Role>();
	private List<Menu> menus = new ArrayList<Menu>();
	private List<String> permits = new ArrayList<String>();
	
	public LoginUser(User user) {
        this.id = user.getId();
        this.createTime = user.getCreatedTime();
        this.username = user.getUsername();
        this.mobile = user.getMobile();
        this.email = user.getEmail();
        this.companyId = user.getCompanyId();
        this.headImg = user.getHeadImg();
        this.nickname = user.getNickname();
        this.chineseName = user.getChineseName();
        this.sex = user.getSex();
        this.birthday = user.getBirthday();
        this.location = user.getLocation();
        this.registerApp = user.getRegisterApp();
        this.dataRange = user.getDataRange();
    }

	public boolean hasRole(String roleCode) {
		for (Role role : roles) {
			if (role.getCode().equals(roleCode)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPermit(String uri) {
		AntPathMatcher pathMatcher = new AntPathMatcher();
		for (String permit : permits) {
			if (pathMatcher.match(permit, uri)) {
				return true;
			}
		}
		return false;
	}

}
