package com.adzuki.admin.common.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class Result<T> implements Serializable {
	private static final long serialVersionUID = 737534186172728433L;

	public static final int SUCCESS = 0;
	public static final int FAIL = 1;

	private int code; // 结果代码
	private String msg; // 结果消息
	private Date timestamp;
	private T data; // 结果数据

	public Result() {
		this.timestamp = new Date();
	}

	public Result(int code) {
		this.code = code;
		this.timestamp = new Date();
	}

	public Result(int code, String msg) {
		this.code = code;
		this.msg = msg;
		this.timestamp = new Date();
	}

	public Result(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.timestamp = new Date();
	}

	@SuppressWarnings("unchecked")
	public Result(Map<String, Object> mapResult) {
		if (mapResult == null || mapResult.get("code") == null) {
			this.code = FAIL;
			this.msg = "result is empty!";
			this.timestamp = new Date();
			return;
		}

		this.code = (Integer) mapResult.get("code");
		this.msg = (String) mapResult.get("msg");
		this.data = (T) mapResult.get("data");
		this.timestamp = new Date();
	}

	public boolean success() {
		if (Objects.equals(this.code, SUCCESS)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean fail() {
		return !success();
	}

	/***
	 * 创建成功结果
	 * 
	 * @return
	 */
	public static <T> Result<T> createSuccessResult() {
		return new Result<T>(Result.SUCCESS, null, null);
	}

	/***
	 * 创建成功结果
	 * 
	 * @param t
	 *            结果数据
	 * @return
	 */
	public static <T> Result<T> createSuccessResult(T t) {
		return new Result<T>(Result.SUCCESS, null, t);
	}

	/***
	 * 创建失败结果
	 * 
	 * @param msg
	 *            错误消息
	 * @return
	 */
	public static <T> Result<T> createFailResult(String msg) {
		return new Result<T>(Result.FAIL, msg, null);
	}

	/***
	 * 创建失败结果
	 * 
	 * @param code
	 *            错误代码
	 * @param msg
	 *            错误消息
	 * @return
	 */
	public static <T> Result<T> createFailResult(int code, String msg) {
		return new Result<T>(code, msg, null);
	}

	/***
	 * 创建失败结果
	 * 
	 * @param code
	 *            错误代码
	 * @param msg
	 *            错误消息
	 * @param data
	 *            结果数据
	 * @return
	 */
	public static <T> Result<T> createFailResult(int code, String msg, T data) {
		return new Result<T>(code, msg, data);
	}

	@Override
	public String toString() {
		return "Result [code=" + code + ", msg=" + msg + ", timestamp="
				+ timestamp + ", data=" + data + "]";
	}

	// ------get set ------------------
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
