package com.example.BookPort.common.util;

import java.util.ArrayList;
import java.util.List;

import com.example.BookPort.common.dto.ErrorDetailsDto;
import com.example.BookPort.common.dto.MetaDataDto;
import com.example.BookPort.common.dto.RestResponseDto;
public class BookPortResponseBuilder {
	
	
	public static RestResponseDto buildSuccessReponse() {
		return buildSuccessReponse(null);
	}

	
	public static RestResponseDto buildSuccessReponse(Object payload) {
		return buildSuccessReponse(payload,  null);
	}
	
	public static RestResponseDto buildSuccessReponse(Object payload, MetaDataDto metaData) {
		RestResponseDto response = new RestResponseDto();
		response.setData(payload);
		response.setMetaData(metaData);
		response.setStatus("SUCCESS");
		return response;
	}
	
	public static RestResponseDto buildFailureResponse() {
		return buildFailureResponse("BK-ERR-500", "Something went wrong, Please try again later.");
	}
	
	public static RestResponseDto buildFailureResponse(List<ErrorDetailsDto> errors) {
		RestResponseDto response = new RestResponseDto();
		response.setErrors(errors);
		response.setStatus("FAILURE");
		return response;
	}
	
	public static RestResponseDto buildFailureResponse(String errCode, String msg) {
		return buildFailureResponse(errCode, msg, null);
	}
	
	public static RestResponseDto buildFailureResponse(ErrorDetailsDto e) {
		return buildFailureResponse(e.getCode(), e.getMessage(), e.getField());
	}
	
	public static RestResponseDto buildFailureResponse(String errCode, String msg, String field) {
		List<ErrorDetailsDto> errors = new ArrayList<ErrorDetailsDto>();
		errors.add(new ErrorDetailsDto(errCode, msg, field));
		RestResponseDto response = new RestResponseDto();
		response.setErrors(errors);
		response.setStatus("FAILURE");
		return response;
	}

}
