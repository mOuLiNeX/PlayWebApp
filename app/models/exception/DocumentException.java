package models.exception;

public class DocumentException extends RuntimeException {

	public DocumentException(String msg) {
		super(msg);
	}

	public DocumentException(Exception cause) {
		super(cause);
	}

}
