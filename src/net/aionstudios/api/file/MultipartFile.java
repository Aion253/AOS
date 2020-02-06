package net.aionstudios.api.file;

import java.io.InputStream;

public class MultipartFile {
	
	private String fieldName;
	private String fileName;
	private String contentType;
	private InputStream inputStream;
	private long size;
	
	public MultipartFile(String fieldName, String fileName, String contentType, InputStream inputStream, long size) {
		this.fieldName = fieldName;
		this.fileName = fileName;
		this.contentType = contentType;
		this.inputStream = inputStream;
		this.size = size;
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public String getFileName() {
		return fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public long getSize() {
		return size;
	}
	
}
