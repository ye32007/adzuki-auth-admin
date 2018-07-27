package com.adzuki.admin.common.data;

import java.io.Serializable;

public class Code implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	private String extend;
	
	public Code() {}
	
	public Code(String code, String name, String extend) {
		super();
		this.code = code;
		this.name = name;
		this.extend = extend;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	@Override
	public String toString() {
		return "Code [code=" + code + ", name=" + name + ", extend=" + extend + "]";
	}
}
