package com.example.BookPort.common.kafka.producer;

import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.BookPort.common.dto.IntraRequest;
import com.example.BookPort.common.logging.Debugger;

@Service
public class KafkaProducerService {
	
	private Debugger d = new Debugger(this.getClass());
	
	@Autowired
	public KafkaTemplate<String, IntraRequest> kafkaTemplate;
	
	public void postEvent(String topic, IntraRequest event) {
		d.dbg("Inside postEvent.");
		d.dbg("Posting to topic: "+ topic);
		d.dbg("Event: "+ event);
		try {
			kafkaTemplate.send(topic, event);
			d.dbg("Wohoo!! Event posted.");
			
		} catch(Exception e) {
			d.dbg("Something went wrong. ", Level.ERROR, e);
		}
		
		
	}
	
	

}
