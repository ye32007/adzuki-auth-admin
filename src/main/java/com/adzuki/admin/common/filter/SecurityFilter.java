package com.adzuki.admin.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import com.adzuki.admin.common.constant.CommonErrors;
import com.adzuki.admin.common.data.AuthUser;
import com.adzuki.admin.common.data.Result;
import com.adzuki.admin.common.support.IPUtil;
import com.adzuki.admin.common.support.SecurityContext;
import com.alibaba.fastjson.JSON;

public class SecurityFilter implements Filter {
	
	public Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

	public AntPathMatcher pathMatcher = new AntPathMatcher();
	public static String SESSION_USER_KEY = "loginUser";
	public static String LOGIN_URL="/login";
	public static String[] anonUrls = new String[] { "/checkstartup.html", "/resources/**", "/login", "/error", "/favicon.ico" };
	public static String[] anonIps = new String[] {};
	public static String[] loginUrls = new String[] { "/", "/index", "/home", "/logout", "/modifyPassword"};

	public void destroy() {

	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain filterChain)throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) arg0;
			HttpServletResponse response = (HttpServletResponse) arg1;
			
			// 1、是否为免验证的IP
			if (anonIps.length > 0 && checkAnonIps(request, response)) {
				filterChain.doFilter(arg0, arg1);
				return;
			}
			// 2、是否免验证的URL
			if (anonUrls.length > 0 && checkAnonUrls(request, response)) {
				filterChain.doFilter(arg0, arg1);
				return;
			}
			HttpSession session = request.getSession();
			AuthUser loginUser = (AuthUser) session.getAttribute(SecurityFilter.SESSION_USER_KEY);
			if (loginUser != null) {
				//登录既有的权限
				if (loginUrls.length > 0 && checkLoginUrls(request, response)) {
					SecurityContext.setAuthUser(loginUser);
					filterChain.doFilter(arg0, arg1);
					return;
				}
				//判断是否有权限
				if(loginUser.hasPermit(request.getServletPath())) {
					SecurityContext.setAuthUser(loginUser);
					filterChain.doFilter(arg0, arg1);
				} else {
					String result = JSON.toJSONString(Result.createFailResult(CommonErrors.NO_PERMISSION, "没有权限！"));
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(result);
				}
			} else {
				String ajax = request.getParameter("ajax");
				if(ajax != null && ajax.equals("true")) {
					String result = JSON.toJSONString(Result.createFailResult(CommonErrors.NOT_LOGIN, null, request.getContextPath() + SecurityFilter.LOGIN_URL));
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(result);
				} else {
					String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
					response.sendRedirect(basePath + SecurityFilter.LOGIN_URL);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SecurityContext.clearAuthUser();
		}
	}

	/**
	 * 可抓取远程参数
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	/***
	 * 不需要验证的IP
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean checkAnonIps(HttpServletRequest request, HttpServletResponse response) {
		String requestIp = IPUtil.getIpAddress(request);
		for (String ip : anonIps) {
			if (ip.equals(requestIp)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkAnonUrls(HttpServletRequest request, HttpServletResponse response) {
		String requestUri = request.getServletPath();
		for (String uri : anonUrls) {
			boolean flag = pathMatcher.match(uri, requestUri);
			if (flag) {
				return true;
			}
		}
		return false;
	}


	private boolean checkLoginUrls(HttpServletRequest request, HttpServletResponse response) {
		String requestUri = request.getServletPath();
		for (String uri : loginUrls) {
			boolean flag = pathMatcher.match(uri, requestUri);
			if (flag) {
				return true;
			}
		}
		return false;
	}

}
