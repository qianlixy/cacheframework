package io.github.qianlixy.cache.exception;

public class SqlParseException extends RuntimeException {

	private static final long serialVersionUID = 6769724160813159324L;

	public SqlParseException() {
		super();
	}
	
	public SqlParseException(String message) {
		super(message);
	}

	public SqlParseException(Throwable cause) {
		super(cause);
	}

}
