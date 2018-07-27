package com.adzuki.admin.common.support;

import com.adzuki.admin.common.data.AuthUser;

public class SecurityContext {

	private static ThreadLocal<String> token = new ThreadLocal<String>();
	private static ThreadLocal<AuthUser> user = new ThreadLocal<AuthUser>();

	public static void setAuthToken(String authToken) {
		token.set(authToken);
	}

	public static void setAuthUser(AuthUser authUser) {
		user.set(authUser);
	}

	public static String getAuthToken() {
		return token.get();
	}

	public static AuthUser getAuthUser() {
		return user.get();
	}

	public static void clearAuthUser() {
		user.remove();
	}

}
