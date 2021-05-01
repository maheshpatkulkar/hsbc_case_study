package com.test.hsbc.custody.fileingresslayer;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CamelFileRoute extends RouteBuilder  {

    static Logger logger = LoggerFactory.getLogger(CamelFileRoute.class);
    private String filePath;
    private String fileSelector;
    
    public CamelFileRoute(String filePath,String fileSelector) {
    	this.filePath = filePath;
    	this.fileSelector = fileSelector;
    }

	@Override
	public void configure() throws Exception {
		from("file://"+ this.filePath + "?delay=1000&preMove=.staging&move=.completed&doneFileName=ReadyFile.txt&include=" + this.fileSelector)		
		.split()
		.tokenize("\n")
		.to("jms:queue:inbound.queue");
	}

}
