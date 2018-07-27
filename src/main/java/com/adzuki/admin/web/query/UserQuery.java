package com.adzuki.admin.web.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserQuery {

	private String username;
	private String chineseName;
	private String sex;
	private String mobile;
	private Boolean locked;

}
