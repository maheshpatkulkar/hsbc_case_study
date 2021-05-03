package com.test.hsbc.custody.fileingresslayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CamelFileRoute extends RouteBuilder  {

    static Logger logger = LoggerFactory.getLogger(CamelFileRoute.class);
    private String filePath;
    private String fileSelector;
    private String fileId;
    
	public String getFileId() {
		return this.fileId;
	}

	private static AtomicLong atomicLong = new AtomicLong();
    
    public CamelFileRoute(String filePath,String fileSelector) {
    	this.filePath = filePath;
    	this.fileSelector = fileSelector;
    }

	@Override
	public void configure() throws Exception {
		setFileId();
		
		from("file://"+ this.filePath + "?delay=1000&preMove=.staging&move=.completed&doneFileName=ReadyFile.txt&include=" + this.fileSelector)
		.setHeader("fileId", method(new FileIdGenerator(),"getFileId()") )
		.split()
		.tokenize("\n")
		// .setHeader("fileId", method(new FileIdGenerator(),"getFileId()") )
	//	.transform(body().append(",").append(getFileId()))
		.log(" " + body())
		.to("jms:queue:inbound.queue");
		
	}

	public void setFileId() {
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMddHHmmss")
				  .appendValue(ChronoField.MILLI_OF_SECOND, 3)
				  .toFormatter();
		this.fileId = LocalDateTime.now().format(formatter) + "" + atomicLong.incrementAndGet();
	}
}
