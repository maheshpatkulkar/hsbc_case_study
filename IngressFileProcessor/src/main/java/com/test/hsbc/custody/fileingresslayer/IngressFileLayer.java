package com.test.hsbc.custody.fileingresslayer;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IngressFileLayer {
	
    static Logger logger = LoggerFactory.getLogger(LoggerFactory.class);


	public static void main(String[] args) throws Exception {
		args = new String[2];
		args[0] = "C://Mahesh//code//hsbc_case_study//staging";
		args[1] = "[input_].*.csv";
		
		if (args.length != 2) {
			throw new Exception("Please passed file path and file selector");
		}
		String filePath = args[0];
		String fileSelector = args[1];		
		
		try (CamelContext camel = new DefaultCamelContext()) {
		    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		    camel.addComponent("jms",JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));			

			Runtime.getRuntime().addShutdownHook(new Thread()
		    {
			  @Override
		      public void run()
		      {
		        logger.info("Shutdown Hook is running !");
				camel.stop();
		      }
		    });			

			camel.addRoutes(new CamelFileRoute(filePath,fileSelector));
			camel.start();
			while (true) {
				Thread.sleep(10_000);
			}
		}
		catch (Exception ex) { 
			logger.error("Error initializing aapplication",ex);
		}
	} 
}
