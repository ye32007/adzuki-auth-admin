package com.adzuki.admin.web.query;

import lombok.Data;

@Data
public class RoleQuery {

	private String code;
	private String name;
	private Boolean locked;
}
