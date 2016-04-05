package com.beng.op;

import com.beng.ErrorCode;

public enum OpError implements ErrorCode {
	RUN_INCOMPLETE_OP;
	
	private static final int CODE_START_VALUE = 0x04000000;

	public int getErrorCode() {
		return CODE_START_VALUE + ordinal();
	}
}
