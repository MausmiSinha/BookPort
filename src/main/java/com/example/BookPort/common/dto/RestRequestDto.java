package com.example.BookPort.common.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class RestRequestDto implements Serializable{
	
	@NotBlank(message = "Action cannot be blank")
	@NotNull(message = "Action is mandatory")
	private String action;
	
	@NotNull(message = "Data is mandatory")
	private Object data;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "RestRequestDto [action=" + action + ", data=" + data + "]";
	}
}
