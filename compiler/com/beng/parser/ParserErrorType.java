package com.beng.parser;

import com.beng.ErrorCode;

public enum ParserErrorType implements ErrorCode {
	FILE_NOT_FOUND, FILE_IO_ERROR, BACKWARD_SEEK, PARSE_MISCALL, BAD_SMALL_UNICODE_ESCAPE, BAD_LARGE_UNICODE_ESCAPE,
	BAD_HEXCODE_ESCAPE, BAD_ESCAPED_CHARACTER, BAD_COLUMN_NUMBER, BAD_LINE_NUMBER;

	private static final int CODE_START_VALUE = 0x02000000;

	public int getErrorCode() {
		return CODE_START_VALUE + ordinal();
	}
}