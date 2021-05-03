package com.test.hsbc.custody.persistenceservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;




@Entity
@Table(name = "STAGING_TABLE" )
public class Staging {
	
 	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
 	
 	@Column (name = "file_id")
 	private int fileId;
 	
 	@Column (name = "file_name")
 	private String fileName;
 	
 	@Column (name = "name")
 	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Staging [id=" + id + ", fileId=" + fileId + ", fileName=" + fileName + ", message=" + message + "]";
	}
 	
 	
 	

}



	



