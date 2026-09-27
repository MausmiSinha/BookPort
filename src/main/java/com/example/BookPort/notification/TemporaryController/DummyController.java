package com.example.BookPort.notification.TemporaryController;

import java.util.HashMap;

import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.notification.entity.EmailNotifLog;
import com.example.BookPort.notification.service.EmailSenderService;

@RestController
@RequestMapping("/dummy")
public class DummyController {

	
	Debugger d = new Debugger(this.getClass());
	
	@Autowired
	private EmailSenderService senderService;
	
	@PostMapping
	public ResponseEntity<String> dummyMail(@RequestBody HashMap<String, String> body){
		d.dbg("Inside dummyMail: Request = [ ToEmail: "+body.get("toEmail")+", subject: "+body.get("subject")+", body: "+body.get("body"));
		String toEmail = body.get("toEmail");
		String subject = body.get("subject");
		String emailBody = body.get("body");
		
		try {
			
			senderService.sendEmail(toEmail, "bookport8@gmail.com", subject, emailBody, new EmailNotifLog());
		} catch(Exception e) {
			d.dbg(e.getMessage(),Level.ERROR, e);
			return new ResponseEntity<>("Failure", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Success", HttpStatus.BAD_REQUEST);
	}
}
