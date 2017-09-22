package io.github.qianlixy.cache.exception;

public class CacheOperationException extends RuntimeException {

	private static final long serialVersionUID = -3133920125682001828L;

	public CacheOperationException() {
		super();
	}
	
	public CacheOperationException(String message) {
		super(message);
	}

	public CacheOperationException(Throwable cause) {
		super(cause);
	}

}
