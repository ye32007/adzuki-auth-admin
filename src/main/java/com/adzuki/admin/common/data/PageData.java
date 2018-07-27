package com.adzuki.admin.common.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageData<T> implements Serializable {
	private static final long serialVersionUID = -8689661131925501144L;
	
	private long total;			//总条数
	private List<T> rows;		//结果对象
	private Map<String,Object> extend = new HashMap<String,Object>();
	
	public PageData() {}

	public PageData(long total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}
	
	public PageData(long total, List<T> rows, Map<String,Object> extend) {
		this.total = total;
		this.rows = rows;
		this.extend = extend;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static PageData<?> createEmpty() {
		return new PageData(0, new ArrayList());
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}

	@Override
	public String toString() {
		return "PageData [total=" + total + ", rows=" + rows + ",extend=" + extend + "]";
	}
}
