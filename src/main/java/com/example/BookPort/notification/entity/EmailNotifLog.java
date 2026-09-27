package com.example.BookPort.notification.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="email_notification_logs")
public class EmailNotifLog {

	@Id
	@Column(name="notification_id")
	private String notificationId;
	
	
	@Column(name="producer_module")
	private String producerModule;
	
	@Column(name="receiver_email")
	private String receiverEmail;
	
	@Column(name="email_type")
	private String emailType;
	
	@Column(name="receiver_user_id")
	private String receiverUserId;
	
	@Column(name="email_subject")
	private String emailSubject;
	
	@Column(name="email_message")
	private String emailMessage;
	
	@Column(name="notif_status")
	private String notifStatus;
	
	@Column(name="retry_count")
	private int retryCount;

	@Column(name="created_at")
	private LocalDateTime createdAt;

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

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

	public String getNotifStatus() {
		return notifStatus;
	}

	public void setNotifStatus(String notifStatus) {
		this.notifStatus = notifStatus;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "EmailNotifLog [notificationId=" + notificationId + ", producerModule=" + producerModule
				+ ", receiverEmail=" + receiverEmail + ", emailType=" + emailType + ", receiverUserId=" + receiverUserId
				+ ", emailSubject=" + emailSubject + ", emailMessage=" + emailMessage + ", notifStatus=" + notifStatus
				+ ", retryCount=" + retryCount + ", createdAt=" + createdAt + "]";
	}
	
}
