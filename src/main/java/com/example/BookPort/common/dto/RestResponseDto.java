package com.example.BookPort.common.dto;

import java.util.List;

import com.example.BookPort.common.constants.AppConstants;

public class RestResponseDto {
	
	private Object data;
	
	private MetaDataDto metaData;
	
	private ResponseStatus status;
	
	private List<ErrorDetailsDto> errors;

	public Object getData() {
		return data;
	}

	public MetaDataDto getMetaData() {
		return metaData;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public List<ErrorDetailsDto> getErrors() {
		return errors;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public void setMetaData(MetaDataDto metaDataDto) {
		this.metaData = metaDataDto;
	}

	public void setStatus(String status) {
		this.status = AppConstants.SUCCESS.equals(status) ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE;
	}
	
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public void setErrors(List<ErrorDetailsDto> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "RestResponseDto [data=" + data + ", metaDataDto=" + metaData + ", status=" + status + ", errors="
				+ errors + "]";
	}
	

}
