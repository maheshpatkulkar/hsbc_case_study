package com.test.hsbc.custody.regressiontest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hsbc.custody.common.entities.Transaction;

public class EndToEndTestApplication {
	
	static RestTemplate restTemplate = new RestTemplate();
	static ObjectMapper objectMapper = new ObjectMapper();


	public static void main(String[] args) throws Exception {
		
		
		/*List<MediaType> list = new ArrayList<>();
		list.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		headers.setAccept(list);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8090/app/transaction/input_4.csv/", HttpMethod.GET,
				entity, String.class);
		System.out.print("output " + response.getBody());*/
		
		testAllSuccessScenario(copyFiletoStaging("input_4.csv"));
		
		testAllOneFailedScenario(copyFiletoStaging("input_1.csv"));
	
	}
	
	private static String copyFiletoStaging(String sourceFile) throws URISyntaxException {
		
		URL res = EndToEndTestApplication.class.getClassLoader().getResource(sourceFile);
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		
		String fileName = "input_" + getFile() + ".csv";
		
		File source = new File(absolutePath);
		File dest = new File("C:\\Mahesh\\code\\hsbc_case_study\\staging\\" + fileName );
		File readyFile = new File("C:\\Mahesh\\code\\hsbc_case_study\\staging\\ReadyFile.txt");
		
		try {
		    Files.copy(source.toPath(), dest.toPath());
		    Files.createFile(readyFile.toPath());
		} catch (IOException e) {
		    e.printStackTrace();
		}		
		return fileName;
		
	}	
	
	
	public static String getFile() {
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyyMMddHHmmss")
				  .appendValue(ChronoField.MILLI_OF_SECOND, 3)
				  .toFormatter();
		return LocalDateTime.now().format(formatter);
	}
	
	private static  void testAllSuccessScenario(String fileName) throws Exception {
		
		Thread.sleep(10000);
		
		List<MediaType> list = new ArrayList<>();
		list.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		headers.setAccept(list);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8090/app/transaction/" + fileName + "/", HttpMethod.GET,
				entity, String.class);

		Map<String,Integer> map = objectMapper.readValue(response.getBody(), Map.class);
		Integer count = map.get("COMPLETED");
		
		if (count != 11) {
			System.out.println("Test case failed");
			throw new Exception();
		}  else {
			System.out.println("testAllSuccessScenario");
		}
		
	}
	
	private static  void testAllOneFailedScenario(String fileName) throws Exception {
		
		Thread.sleep(10000);		
		
		List<MediaType> list = new ArrayList<>();
		list.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		headers.setAccept(list);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:8090/app/transaction/" + fileName + "/", HttpMethod.GET,
				entity, String.class);

		Map<String,Integer> map = objectMapper.readValue(response.getBody(), Map.class);
		Integer count = map.get("FAILED");
		
		if (count != 1) {
			System.out.println("Test case failed");
			throw new Exception();
		}  else {
			System.out.println("testAllOneFailedScenario");
		}
  
		
	}	
	
	private static  void testPostForConnectivityAppGatewayDiscoveryPersistenceService() throws JsonProcessingException {
		Transaction transaction = new Transaction();
		transaction.setBook("book");
		transaction.setCounterParty("c");		
		transaction.setExecPrice("p");		
		transaction.setExecutionQuantity("q");		
		transaction.setFileId("a");		
		transaction.setInstrumentId("1");		
		transaction.setOrderPrice("s");
		transaction.setSide("d");
		transaction.setTradeDate("12May22" );
		transaction.setTradeId("2");
		
		
		HttpEntity<String> request = getHttpEntity("1234","RECEIVED",objectMapper.writeValueAsString(transaction));
		ResponseEntity<String> response =
				restTemplate.postForEntity("http://localhost:8090/app/transaction/", request, String.class);

		

	}
	
	
	private static HttpEntity<String> getHttpEntity (String fileId, String status,String jsonString) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);	
		headers.add("fileId", fileId);
		headers.add("status", status);

		return new HttpEntity<String>(jsonString, headers);
	}


}
