package com.example.BookPort.common.dto;

import java.util.List;

public class FetchAuthUsersRequestDto {
	private List<String> userIds;

    public FetchAuthUsersRequestDto() {
    }

    public FetchAuthUsersRequestDto(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

	@Override
	public String toString() {
		return "FetchAuthUsersRequestDto [userIds=" + userIds + "]";
	}
    
    

}
