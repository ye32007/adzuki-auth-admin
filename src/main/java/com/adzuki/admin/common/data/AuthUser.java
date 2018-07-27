package com.adzuki.admin.common.data;

import java.io.Serializable;

public interface AuthUser extends Serializable {
	
	Long getId();
	String getUsername();
	
	boolean hasPermit(String url);
	boolean hasRole(String code);

}
