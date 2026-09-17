package com.example.BookPort.notification.service;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookPort.auth.entity.RegistrationRequestAudit;
import com.example.BookPort.auth.repository.RegistrationRequestAuditRepo;
import com.example.BookPort.common.constants.AppConstants;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.util.IdGenerator;
import com.example.BookPort.notification.constants.NotificationConstants;
import com.example.BookPort.notification.dto.EmailNotifLogDto;
import com.example.BookPort.notification.entity.EmailNotifLog;
import com.example.BookPort.notification.repository.EmailNotifLogRepo;

@Service
public class NotificationService {
	
	private Debugger d = new Debugger(this.getClass());
	
    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private EmailNotifLogRepo emailNotifLogRepo;
    
    @Autowired
    RegistrationRequestAuditRepo registrationRequestAuditRepo;
    
    public void sendRegistrationApprovedEmail(HashMap<String, Object> approveNotifRequest) {
    	
    	String name = (String) approveNotifRequest.get("name");
    	String username = (String) approveNotifRequest.get("username");
    	String email = (String) approveNotifRequest.get("email");
    	String userId = (String) approveNotifRequest.get("userId");
    	
        String subject = "Your BookPort registration has been approved";

        String body = """
                <html>
                    <body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif; color:#1f2937;">
                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; padding:32px 0;">
                            <tr>
                                <td align="center">
                                    <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:8px; overflow:hidden; border:1px solid #e5e7eb;">
                                        <tr>
                                            <td style="background-color:#2563eb; padding:24px 32px; color:#ffffff;">
                                                <h1 style="margin:0; font-size:24px; font-weight:700;">BookPort</h1>
                                                <p style="margin:6px 0 0; font-size:14px;">Registration Approved</p>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td style="padding:32px;">
                                                <p style="margin:0 0 16px; font-size:16px;">Hello %s,</p>

                                                <p style="margin:0 0 16px; font-size:15px; line-height:1.6;">
                                                    Good news! Your BookPort registration request has been approved.
                                                </p>

                                                <p style="margin:0 0 20px; font-size:15px; line-height:1.6;">
                                                    You can now log in using your registered username:
                                                </p>

                                                <div style="background-color:#f9fafb; border:1px solid #e5e7eb; border-radius:6px; padding:14px 16px; margin-bottom:24px;">
                                                    <span style="font-size:13px; color:#6b7280;">Username</span><br/>
                                                    <strong style="font-size:18px; color:#111827;">%s</strong>
                                                </div>

                                                <p style="margin:0 0 16px; font-size:15px; line-height:1.6;">
                                                    Welcome aboard. We are happy to have you in the BookPort community.
                                                </p>

                                                <p style="margin:24px 0 0; font-size:15px; line-height:1.6;">
                                                    Regards,<br/>
                                                    <strong>BookPort Team</strong>
                                                </p>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td style="background-color:#f9fafb; padding:16px 32px; border-top:1px solid #e5e7eb;">
                                                <p style="margin:0; font-size:12px; color:#6b7280;">
                                                    This is an automated email from BookPort. Please do not reply to this message.
                                                </p>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </body>
                </html>
                """.formatted(name, username);

        EmailNotifLogDto logDto = new EmailNotifLogDto();
        logDto.setProducerModule("AUTH");
        logDto.setReceiverEmail(email);
        logDto.setEmailType("REGISTRATION_APPROVED");
        logDto.setReceiverUserId(userId);
        logDto.setEmailSubject(subject);
        logDto.setEmailMessage(body);
        
        EmailNotifLog log = buildLog(logDto);
        

        emailSenderService.sendEmail(email, NotificationConstants.FROM_EMAIL, subject, body, log);
        emailNotifLogRepo.save(log);
    }

    public void sendRegistrationRejectedEmail(RegistrationRequestAudit user) {
        String subject = "Your BookPort registration has been rejected";

        String body = """
                <html>
                    <body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif; color:#1f2937;">
                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; padding:32px 0;">
                            <tr>
                                <td align="center">
                                    <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:8px; overflow:hidden; border:1px solid #e5e7eb;">
                                        <tr>
                                            <td style="background-color:#dc2626; padding:24px 32px; color:#ffffff;">
                                                <h1 style="margin:0; font-size:24px; font-weight:700;">BookPort</h1>
                                                <p style="margin:6px 0 0; font-size:14px;">Registration Rejected</p>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td style="padding:32px;">
                                                <p style="margin:0 0 16px; font-size:16px;">Hello %s,</p>

                                                <p style="margin:0 0 16px; font-size:15px; line-height:1.6;">
                                                    Thank you for your interest in joining BookPort.
                                                </p>

                                                <p style="margin:0 0 20px; font-size:15px; line-height:1.6;">
                                                    Unfortunately, your registration request could not be approved at this time.
                                                </p>

                                                <div style="background-color:#fef2f2; border:1px solid #fecaca; border-radius:6px; padding:14px 16px; margin-bottom:24px;">
                                                    <span style="font-size:13px; color:#991b1b;">Reason</span><br/>
                                                    <strong style="font-size:15px; color:#7f1d1d;">%s</strong>
                                                </div>

                                                <p style="margin:0 0 16px; font-size:15px; line-height:1.6;">
                                                    You may register again after correcting the issue mentioned above.
                                                </p>

                                                <p style="margin:24px 0 0; font-size:15px; line-height:1.6;">
                                                    Regards,<br/>
                                                    <strong>BookPort Team</strong>
                                                </p>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td style="background-color:#f9fafb; padding:16px 32px; border-top:1px solid #e5e7eb;">
                                                <p style="margin:0; font-size:12px; color:#6b7280;">
                                                    This is an automated email from BookPort. Please do not reply to this message.
                                                </p>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </body>
                </html>
                """.formatted(user.getName(), user.getRemark());

        EmailNotifLogDto logDto = new EmailNotifLogDto();
        logDto.setProducerModule("AUTH");
        logDto.setReceiverEmail(user.getEmail());
        logDto.setEmailType("REGISTRATION_FAILURE");
        logDto.setReceiverUserId(user.getRegistrationId());
        logDto.setEmailSubject(subject);
        logDto.setEmailMessage(body);

        EmailNotifLog log = buildLog(logDto);

        emailSenderService.sendEmail(user.getEmail(), NotificationConstants.FROM_EMAIL, subject, body, log);
        emailNotifLogRepo.save(log);
    }

    public void sendRegistrationEmail(HashMap<String, Object> registerNotificationRequest) {

        String name = (String) registerNotificationRequest.get("name");
        String registrationId = (String) registerNotificationRequest.get("registrationId");
        String email = (String) registerNotificationRequest.get("email");
        
      

        String subject = "BookPort registration request received";

        String body = """
                <html>
                    <body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif; color:#1f2937;">
                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; padding:32px 0;">
                            <tr>
                                <td align="center">
                                    <table width="600" cellpadding="0" cellspacing="0"
                                           style="background-color:#ffffff; border-radius:8px; overflow:hidden; border:1px solid #e5e7eb;">

                                        <tr>
                                            <td style="background-color:#2563eb; padding:24px 32px; color:#ffffff;">
                                                <h1 style="margin:0; font-size:24px; font-weight:700;">BookPort</h1>
                                                <p style="margin:6px 0 0; font-size:14px;">
                                                    Registration Request Received
                                                </p>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td style="padding:32px;">

                                                <p style="margin:0 0 16px; font-size:16px;">
                                                    Hello %s,
                                                </p>

                                                <p style="margin:0 0 16px; font-size:15px; line-height:1.6;">
                                                    Thank you for registering with <strong>BookPort</strong>.
                                                    We have successfully received your registration request.
                                                </p>

                                                <p style="margin:0 0 20px; font-size:15px; line-height:1.6;">
                                                    Your request is currently under review. Once it has been processed,
                                                    you will receive another email notifying you whether your request has
                                                    been approved or if additional information is required.
                                                </p>

                                                <div style="background-color:#f9fafb;
                                                            border:1px solid #e5e7eb;
                                                            border-radius:6px;
                                                            padding:14px 16px;
                                                            margin-bottom:24px;">

                                                    <span style="font-size:13px; color:#6b7280;">
                                                        Registration ID
                                                    </span><br/>

                                                    <strong style="font-size:18px; color:#111827;">
                                                        %s
                                                    </strong>

                                                </div>

                                                <p style="margin:0 0 16px; font-size:15px; line-height:1.6;">
                                                    Please keep this Registration ID for future communication regarding
                                                    your registration request.
                                                </p>

                                                <p style="margin:24px 0 0; font-size:15px; line-height:1.6;">
                                                    Regards,<br/>
                                                    <strong>BookPort Team</strong>
                                                </p>

                                            </td>
                                        </tr>

                                        <tr>
                                            <td style="background-color:#f9fafb;
                                                       padding:16px 32px;
                                                       border-top:1px solid #e5e7eb;">

                                                <p style="margin:0; font-size:12px; color:#6b7280;">
                                                    This is an automated email from BookPort.
                                                    Please do not reply to this message.
                                                </p>

                                            </td>
                                        </tr>

                                    </table>
                                </td>
                            </tr>
                        </table>
                    </body>
                </html>
                """
                .formatted(name, registrationId);

        EmailNotifLogDto logDto = new EmailNotifLogDto();
        
        logDto.setProducerModule("AUTH");
        logDto.setReceiverEmail(email);
        logDto.setEmailType("REGISTRATION_REQUEST");
        logDto.setReceiverUserId(registrationId);
        logDto.setEmailSubject(subject);
        logDto.setEmailMessage(body);
        
        d.dbg("logDto:"+ logDto.toString());

        EmailNotifLog log = buildLog(logDto);

        emailSenderService.sendEmail(
                email,
                NotificationConstants.FROM_EMAIL,
                subject,
                body,
                log);

        emailNotifLogRepo.save(log);
    }
    
    public void sendUpdateEmail(HashMap<String, Object> updateNotificationRequest) {

        String name = (String) updateNotificationRequest.get("name");
        String registrationId = (String) updateNotificationRequest.get("registrationId");
        String email = (String) updateNotificationRequest.get("email");
        
      

        String subject = "BookPort updation request received";

        String body = """
                <html>
                    <body style="margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, Helvetica, sans-serif; color:#1f2937;">
                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f6f8; padding:32px 0;">
                            <tr>
                                <td align="center">
                                    <table width="600" cellpadding="0" cellspacing="0"
                                           style="background-color:#ffffff; border-radius:8px; overflow:hidden; border:1px solid #e5e7eb;">

                                        <tr>
                                            <td style="background-color:#2563eb; padding:24px 32px; color:#ffffff;">
                                                <h1 style="margin:0; font-size:24px; font-weight:700;">BookPort</h1>
                                                <p style="margin:6px 0 0; font-size:14px;">
                                                    Updation Request Received
                                                </p>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td style="padding:32px;">

                                                <p style="margin:0 0 16px; font-size:16px;">
                                                    Hello %s,
                                                </p>

                                                <p style="margin:0 0 16px; font-size:15px; line-height:1.6;">
                                                    Thank you for registering with <strong>BookPort</strong>.
                                                    We have successfully received your updation request.
                                                </p>

                                                <p style="margin:0 0 20px; font-size:15px; line-height:1.6;">
                                                    Your request is currently under review. Once it has been processed,
                                                    you will receive another email notifying you whether your request has
                                                    been approved or if additional information is required.
                                                </p>

                                                <div style="background-color:#f9fafb;
                                                            border:1px solid #e5e7eb;
                                                            border-radius:6px;
                                                            padding:14px 16px;
                                                            margin-bottom:24px;">

                                                    <span style="font-size:13px; color:#6b7280;">
                                                        Registration ID
                                                    </span><br/>

                                                    <strong style="font-size:18px; color:#111827;">
                                                        %s
                                                    </strong>

                                                </div>

                                                <p style="margin:0 0 16px; font-size:15px; line-height:1.6;">
                                                    Please keep this Registration ID for future communication regarding
                                                    your registration request.
                                                </p>

                                                <p style="margin:24px 0 0; font-size:15px; line-height:1.6;">
                                                    Regards,<br/>
                                                    <strong>BookPort Team</strong>
                                                </p>

                                            </td>
                                        </tr>

                                        <tr>
                                            <td style="background-color:#f9fafb;
                                                       padding:16px 32px;
                                                       border-top:1px solid #e5e7eb;">

                                                <p style="margin:0; font-size:12px; color:#6b7280;">
                                                    This is an automated email from BookPort.
                                                    Please do not reply to this message.
                                                </p>

                                            </td>
                                        </tr>

                                    </table>
                                </td>
                            </tr>
                        </table>
                    </body>
                </html>
                """
                .formatted(name, registrationId);

        EmailNotifLogDto logDto = new EmailNotifLogDto();
        
        logDto.setProducerModule("AUTH");
        logDto.setReceiverEmail(email);
        logDto.setEmailType("UPDATION_REQUEST");
        logDto.setReceiverUserId(registrationId);
        logDto.setEmailSubject(subject);
        logDto.setEmailMessage(body);
        
        d.dbg("logDto:"+ logDto.toString());

        EmailNotifLog log = buildLog(logDto);

        emailSenderService.sendEmail(
                email,
                NotificationConstants.FROM_EMAIL,
                subject,
                body,
                log);

        emailNotifLogRepo.save(log);
    }
    
    private EmailNotifLog buildLog(
    		EmailNotifLogDto dto
    ) {
        EmailNotifLog log = new EmailNotifLog();
        
        IdGenerator idGenerator = new IdGenerator(AppConstants.Module.NOTIF_MODULE_ID);
	    String notifId = idGenerator.generateId();

        log.setNotificationId(notifId);
        log.setProducerModule(dto.getProducerModule());
        log.setReceiverEmail(dto.getReceiverEmail());
        log.setEmailType(dto.getEmailType());
        log.setReceiverUserId(dto.getReceiverUserId());
        log.setEmailSubject(dto.getEmailSubject());
        log.setEmailMessage(dto.getEmailMessage());
        log.setRetryCount(0);
        log.setCreatedAt(LocalDateTime.now());
        log.setNotifStatus(AppConstants.PENDING);

        return log;
    }


}
