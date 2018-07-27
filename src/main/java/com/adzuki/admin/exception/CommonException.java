package com.adzuki.admin.exception;

public class CommonException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//具体异常码
    protected int code = 1;
    //异常信息
    protected String msg;

    public CommonException(int code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.msg = String.format(msgFormat, args);
    }

    public CommonException() {
        super();
    }

    public String getMessage() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    /**
     * 实例化异常
     * @param msgFormat
     * @param args
     * @return
     */
    public CommonException newInstance(String msgFormat, Object... args) {
        return new CommonException(this.code, msgFormat, args);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
    }

    public CommonException(Throwable cause) {
        super(cause);
        this.msg = this.getMessage();
    }

    public CommonException(String message) {
        super(message);
        this.msg = message;
    }


}
