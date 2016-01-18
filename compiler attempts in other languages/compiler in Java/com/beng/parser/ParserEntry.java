package com.beng.parser;

import java.util.Stack;

import com.beng.BEngCompiler;
import com.beng.SU;
import com.beng.opsyntax.quantifiable.OpLiteralCharSyntax;

abstract class ParserEntry {
	protected final Parser parser;

	private final int initial_column_number;
	private final int initial_line_number;

	protected int chars_read_this_line = 0;
	// the progressed line number is tracked by the size of line_lengths

	protected Stack<Integer> line_lengths = new Stack<>();

	public ParserEntry(Parser parser, int line_number, int column_number, long position) {
		this.parser = parser;
		initial_column_number = column_number;
		initial_line_number = line_number;

		// check initial line and column number values
		if (initial_column_number <= 0)
			BEngCompiler.err(ParserErrorType.BAD_COLUMN_NUMBER, "I was asked to parse from column "
					+ initial_column_number + "; column numbers begin at 1!");
		else if (initial_line_number <= 0)
			BEngCompiler.err(ParserErrorType.BAD_LINE_NUMBER, "I was asked to parse from line "
					+ initial_column_number + "; line numbers begin at 1!");
	}

	public ParserEntry(Parser parser) {
		this(parser, 1, 1, 0);
	}

	protected abstract String locationalFormat();

	// parsing
	public void parseOrErr(String to_parse, String parse_purpose) {
		if (!parse(to_parse))
			parser.err(ParserErrorType.PARSE_MISCALL, "I attempted to parse " + SU.aOrAn(parse_purpose)
					+ ", but I did not get the \"" + to_parse + "\" that I expected!");
	}

	public boolean parse(String to_parse) {
		long old_input_position = getPosition();

		for (byte next_byte : to_parse.getBytes()) {
			if (next_byte != readByte()) {
				seekTo(old_input_position);
				return false;
			}
		}

		return true;
	}

	public char parseEscapedChar() {
		parseOrErr("\\", "escaped character");

		byte escape_char = readByte();
		switch (escape_char) {
		case 'n':
			return '\n';
		case 't':
			return '\t';
		case 'v':
			return '\u000B';
		case 'f':
			return '\f';
		case 'r':
			return '\r';
		case '0':
			return '\0';
		// in the future, add u, U, and x
		case '\\':
			return '\\';
		default:
			parser.err(ParserErrorType.BAD_ESCAPED_CHARACTER,
					"I found an invalid \"\\" + escape_char + "\" escape character.");
			return '\0';
		}
	}

	public abstract long getPosition();

	public void seek(int relative_bytes) {
		seek(relative_bytes, true);
	}

	protected abstract void seek(int relative_bytes, boolean print_debug);

	public void seekTo(long absolute_position) {
		seek((int) (getPosition() - absolute_position));
	}

	/** This method reads the next <b>byte</b> from the input file currently being read by this parser. Note that
	 * this may be a part of a large character in UTF-8 or other such encodings.
	 * 
	 * @return the byte read or -1 if there is an end of file. Note that in ASCII and UTF-8 encodings, no bytes may
	 *         be -1 (0xFF), so returning -1 as an error code is not an issue. */
	public byte readByte() {
		return readByte(true);
	}

	protected abstract byte readByte(boolean print_debug);

	public void ungetByte() {
		ungetByte(true);
	}

	private void ungetByte(boolean print_debug) {
		if (print_debug)
			parser.verboseDebug("ungetting byte...");
		seek(-1, false);
	}

	public byte peekByte() {
		byte peeked_byte = readByte(false);
		if (peeked_byte != -1)
			ungetByte(false);

		parser.verboseDebug("peeked at " + OpLiteralCharSyntax.toPrintable((char) peeked_byte));
		return peeked_byte;
	}

	public int getLineNumber() {
		return line_lengths.size() + initial_line_number;
	}

	public int getColumnNumber() {
		return chars_read_this_line + (line_lengths.isEmpty() ? initial_column_number : 1);
	}
}