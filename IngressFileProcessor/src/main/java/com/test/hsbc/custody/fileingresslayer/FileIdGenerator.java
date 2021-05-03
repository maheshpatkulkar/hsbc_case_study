package com.test.hsbc.custody.fileingresslayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.concurrent.atomic.AtomicLong;

public class FileIdGenerator {

	private static AtomicLong atomicLong = new AtomicLong();		

	
	public String getFileId() {
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMddHHmmss")
				  .appendValue(ChronoField.MILLI_OF_SECOND, 3)
				  .toFormatter();
		return LocalDateTime.now().format(formatter) + "" + atomicLong.incrementAndGet();
	}
	

}
