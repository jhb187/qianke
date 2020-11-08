package com.qianke.exception;

/**
 * 
 * 异常基类
 */
public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -6412861495820342841L;

	/** 外部错误编码 */
	private String errorCode = "999999";
	/** 默认错误信息 */
	protected String defaultMessage = "未定义异常";
	/** 错误信息参数 */
	private String[] args;

	public BaseException() {
		super();
	}

	public BaseException(String errorCode, String defaultMessage) {
		super();
		this.errorCode = errorCode;
		this.defaultMessage = defaultMessage;
	}
	
	public BaseException(String errorCode, String defaultMessage, String extMessage) {
		super();
		this.errorCode = errorCode;
		this.defaultMessage = defaultMessage;
		this.defaultMessage += "( " + extMessage + " )";
	}

	public BaseException(String errorCode, String defaultMessage, String[] args) {
		super();
		this.errorCode = errorCode;
		this.defaultMessage = defaultMessage;
		this.args = args;
	}
	
	public BaseException(String errorCode, String defaultMessage, String[] args, String extMessage) {
		super();
		this.errorCode = errorCode;
		this.defaultMessage = defaultMessage;
		this.defaultMessage += "( " + extMessage + " )";
		this.args = args;
	}


	@Override
	public String getMessage() {
		String message = this.defaultMessage;
		for (int i = 0; this.getArgs () != null && i < this.getArgs ().length; i++) {
            message = message.replaceFirst("\\{\\d{1}\\}", this.getArgs ()[i]);
        }
		return message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

}
