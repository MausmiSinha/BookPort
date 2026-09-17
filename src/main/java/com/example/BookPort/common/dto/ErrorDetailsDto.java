package com.example.BookPort.common.dto;

public class ErrorDetailsDto {
	
	private String code;      
	
    private String message;    
    
    private String field;      
    
    public ErrorDetailsDto() {
    	
    }

	public ErrorDetailsDto(String code, String message, String field) {
		super();
		this.code = code;
		this.message = message;
		this.field = field;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getField() {
		return field;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public String toString() {
		return "ErrorDetailsDto [code=" + code + ", message=" + message + ", field=" + field + "]";
	}

}
