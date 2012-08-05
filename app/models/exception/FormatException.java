package models.exception;

import java.text.ParseException;

public class FormatException extends Exception {

	public FormatException(String msg) {
		super(msg);
	}

	public FormatException(Exception cause) {
		super(cause);
	}

}
