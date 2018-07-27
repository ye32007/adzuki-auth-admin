package com.adzuki.admin.web.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVo {
	
	private Long id;
	private String username;
	private String mobile;
	private Boolean mobileVerified;
	private String email;
	private Boolean emailVerified;
	private Integer companyId;
	private String headImg;
	private String nickname;
	private String chineseName;
	private String sex;
	private String birthday;
	private String location;
	private String memo;
	private Boolean appEnabled;
	private Boolean locked;
	private String registerApp;
	private String dataRange;
	private String ext;
	private Date createdTime;

}
