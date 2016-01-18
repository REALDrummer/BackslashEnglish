package com.beng.parser;

public class StringParserEntry extends ParserEntry {
	final String string;
	int position = 0;

	public StringParserEntry(Parser parser, String string) {
		super(parser);
		this.string = string;
	}

	@Override
	protected String locationalFormat() {
		return "At char " + Integer.toUnsignedString(position) + ", %s";
	}

	@Override
	public long getPosition() {
		return position;
	}

	@Override
	protected void seek(int relative_bytes, boolean print_debug) {
		position += relative_bytes;
	}

	@Override
	protected byte readByte(boolean print_debug) {
		return (byte) string.charAt(position++);
	}

}
