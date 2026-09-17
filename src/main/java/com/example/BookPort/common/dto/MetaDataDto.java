package com.example.BookPort.common.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/*
 * Sample Request:
 * Endpoint: /auth/v1/reject
 * Body:
 * 
{
    "registrationId": "2620601051789257000",
    "remark": "The uploaded government ID is not valid.",
    "status": "REJECT"
}
 * 
 * 
 */

public class MetaDataDto{
	
	private int page;

	private int size;

	private long totalElements;

	private int totalPages;
	
	public MetaDataDto() {
		
	}

	public MetaDataDto(int page, int size, long totalElements, int totalPages) {
		super();
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
	}

	public int getPage() {
		return page;
	}

	public int getSize() {
		return size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public String toString() {
		return "MetaDataDto [page=" + page + ", size=" + size + ", totalElements=" + totalElements + ", totalPages="
				+ totalPages + "]";
	}
}
