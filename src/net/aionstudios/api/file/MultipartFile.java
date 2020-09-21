package net.aionstudios.api.file;

import java.io.InputStream;

/**
 * An object representation of file uploads to the server.
 * 
 * @author Winter Roberts
 *
 */
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

	/**
	 * @return The name of the field or object title assigned to the uploaded file.
	 */
	public String getFieldName() {
		return fieldName;
	}
	
	/**
	 * @return The name of the uploaded file.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return The content type the uploaded file.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @return An input stream which will read the uploaded file.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @return The size (in bytes) of the uploaded file.
	 */
	public long getSize() {
		return size;
	}
	
}
