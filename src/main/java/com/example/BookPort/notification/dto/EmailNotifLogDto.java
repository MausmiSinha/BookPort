package com.example.BookPort.notification.dto;

public class EmailNotifLogDto {
	
	private String producerModule;
    private String receiverEmail;
    private String emailType;
    private String receiverUserId;
    private String emailSubject;
    private String emailMessage;
	public String getProducerModule() {
		return producerModule;
	}
	public void setProducerModule(String producerModule) {
		this.producerModule = producerModule;
	}
	public String getReceiverEmail() {
		return receiverEmail;
	}
	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}
	public String getEmailType() {
		return emailType;
	}
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	public String getReceiverUserId() {
		return receiverUserId;
	}
	public void setReceiverUserId(String receiverUserId) {
		this.receiverUserId = receiverUserId;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getEmailMessage() {
		return emailMessage;
	}
	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}
	@Override
	public String toString() {
		return "EmailNotifLogDto [producerModule=" + producerModule + ", receiverEmail=" + receiverEmail
				+ ", emailType=" + emailType + ", receiverUserId=" + receiverUserId + ", emailSubject=" + emailSubject
				+ ", emailMessage=" + emailMessage + "]";
	}
    
    

}
