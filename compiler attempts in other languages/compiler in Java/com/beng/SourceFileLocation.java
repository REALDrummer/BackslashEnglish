package com.beng;

public class SourceFileLocation {
	private final String input_file_path;
	private final int line_number;
	private final int column_number;

	public SourceFileLocation(String input_file_path, int line_number, int column_number) {
		this.input_file_path = input_file_path;
		this.line_number = line_number;
		this.column_number = column_number;
	}

	public String getInputFilePath() {
		return input_file_path;
	}

	public int getLineNumber() {
		return line_number;
	}

	public int getColumnNumber() {
		return column_number;
	}
}
