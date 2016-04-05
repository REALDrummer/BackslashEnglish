package com.beng.op.call;

import com.beng.SourceFileLocation;

public class OpCallException extends Exception {
	private static final long serialVersionUID = 2206805616867645425L;

	private final SourceFileLocation location;

	public OpCallException(String message, SourceFileLocation location) {
		super(message);

		this.location = location;
	}

	public OpCallException(Throwable cause, SourceFileLocation location) {
		super(cause);

		this.location = location;
	}

	public OpCallException(String message, Throwable cause, SourceFileLocation location) {
		super(message, cause);

		this.location = location;
	}

	public SourceFileLocation getLocation() {
		return location;
	}

	@Override
	public String getMessage() {
		return "At " + location + ", " + super.getMessage();
	}

}
