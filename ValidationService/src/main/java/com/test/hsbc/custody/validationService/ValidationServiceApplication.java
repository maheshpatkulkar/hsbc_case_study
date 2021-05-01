package com.test.hsbc.custody.validationService;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import com.test.hsbc.custody.validationService.messageconvertor.TransactionMessageConvertor;

@SpringBootApplication
@EnableJms

public class ValidationServiceApplication {

	  @Bean
	  public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
	                          DefaultJmsListenerContainerFactoryConfigurer configurer) {
		  
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    configurer.configure(factory, connectionFactory);
	    factory.setMessageConverter( new TransactionMessageConvertor());
	    return factory;
	  }
	
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ValidationServiceApplication.class, args);
		
	}

}
