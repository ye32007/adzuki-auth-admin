package com.adzuki.admin.common.data;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer pageNum = 1; // 当前页数
	private Integer pageSize = 15; // 每页记录数

	@Override
	public String toString() {
		return "PageParam [pageNum=" + pageNum + ", pageSize=" + pageSize + "]";
	}
}
