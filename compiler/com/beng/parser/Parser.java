package com.beng.parser;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.util.Stack;

import com.beng.BEngCompiler;
import com.beng.ErrorCode;
import com.beng.SourceFileLocation;
import com.beng.WarningType;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCall;
import com.beng.op.types.Type;

public class Parser implements Readable {
	Stack<ParserEntry> context = new Stack<>();

	public Parser(File input_file) {
		context.push(new FileParserEntry(this, input_file));
	}

	public Parser(String string) {
		context.push(new StringParserEntry(this, string));
	}

	protected ParserEntry getCurrentEntry() {
		return context.peek();
	}
	
	public static OpCall parseOpInContext(String op, FullyAppliedOpCall type) {
		// TODO: this is horrible practice for a number of reasons, but it is a temporary solution
		BEngCompiler.parser.context.push(new StringParserEntry(BEngCompiler.parser, op));
		OpCall call = BEngCompiler.parser.parseOp(type);
		BEngCompiler.parser.context.pop();
		return call;
	}

	public SourceFileLocation getLocation() {
		FileParserEntry last_file_entry = null;
		for (int i = context.size() - 1; i >= 0; i--) {
			if (context.get(i) instanceof FileParserEntry) {
				last_file_entry = (FileParserEntry) context.get(i);
				break;
			}
		}

		if (last_file_entry != null)
			return last_file_entry.getLocation();
		else
			return null;
	}

	/** This method will attempt to read the given literal string from the file from its current point.
	 * 
	 * @param to_parse is the <tt>String</tt> to parse from the given file.
	 * 
	 * @return <b>true</b> if this method was successfully able to parse <b><tt>to_parse</b></tt>; <b>false</b>
	 *         otherwise. */
	public boolean parse(String to_parse) {
		return getCurrentEntry().parse(to_parse);
	}

	/** This method will attempt to read the given literal string from the file from its current point. If the given
	 * string is not present, an error of type {@link ParserErrorType#PARSE_MISCALL} will be thrown with an error
	 * message detailing the string to parse and the given parse purpose.
	 * 
	 * @param to_parse is the <tt>String</tt> to parse from the given file.
	 * @param parse_purpose is the <tt>String</tt> very briefly describing the purpose of parsing <b>
	 *                <tt>to_parse</b></tt>. This will be used in the error message if the parsing was
	 *                unsuccessful. */
	public void parseOrErr(String to_parse, String parse_purpose) {
		getCurrentEntry().parseOrErr(to_parse, parse_purpose);
	}

	public OpCall parseOp(FullyAppliedOpCall type) {
		// TODO
		return null;
	}

	public BigInteger parseInteger() {
		StringBuilder builder = new StringBuilder();

		// parse an optional sign
		if (peekByte() == '-')
			// parse the negative sign into the String
			builder.append(readByte());
		else if (peekByte() == '+')
			// skip over the '+'; BigInteger parsing does not accept '+'
			readByte();

		// make sure that there is at least one digit
		if (!Character.isDigit(peekByte()))
			return null;

		// parse the base-10 digits into the String to be passed into the BigInteger
		while (Character.isDigit(peekByte()))
			builder.append((char) readByte());

		return new BigInteger(builder.toString());
	}

	public char parseEscapedChar() {
		return getCurrentEntry().parseEscapedChar();
	}

	/** This method "peeks" at the next byte in the file, reading it and then immediately ungetting it.
	 * 
	 * @return the next byte in the file or -1 if the end of the file was reached.
	 * 
	 * @see {@link #readByte()} and {@link #ungetByte()} */
	public byte peekByte() {
		return getCurrentEntry().peekByte();
	}

	@Override
	public int read(CharBuffer buffer) throws IOException {
		int chars_read = 0;
		while (buffer.hasRemaining()) {
			byte read_byte = readByte();
			if (read_byte == -1)
				break;

			buffer.put((char) read_byte);
			chars_read++;
		}

		return chars_read;
	}

	/** This method reads the next byte from the current file. One byte often represents a single character,
	 * especially in code, which is heavily ASCII-compatible, but does not always. This method works like an
	 * iterator's <tt>next()</tt>, so once this method is called once, its next call with retrieve the byte after
	 * the one received last.
	 * 
	 * @return the <b>byte</b> read from the file or -1 if an end of file was reached. */
	public byte readByte() {
		return getCurrentEntry().readByte();
	}

	/** This method seeks one byte backward in the current file. This causes a call to {@link #readByte()} to repeat
	 * the same <b>byte</b> that it did one call earlier. If this method attempts to seek back behind the beginning
	 * of the file, an error of type {@link ParserErrorType#BACKWARD_SEEK} will be thrown. */
	public void ungetByte() {
		getCurrentEntry().ungetByte();
	}

	// messaging utils
	public void err(ErrorCode exit_status, String message) {
		BEngCompiler.err(exit_status, String.format(getCurrentEntry().locationalFormat(), message));
	}

	public void warn(WarningType warning_type, String message) {
		final String WARN_MESSAGE_FORMAT = "\u001B[1;33mWARNING: %s\u001B[0m\n";

		if (BEngCompiler.strictest)
			err(warning_type, message);
		else
			String.format(WARN_MESSAGE_FORMAT,
					String.format(getCurrentEntry().locationalFormat(), message));
	}

	public void debug(String message) {
		final String DEBUG_MESSAGE_FORMAT = "%s\n";

		if (BEngCompiler.debug)
			System.out.println(String.format(DEBUG_MESSAGE_FORMAT,
					String.format(getCurrentEntry().locationalFormat(), message)));
	}

	public void verboseDebug(String message) {
		final String VERBOSE_MESSAGE_FORMAT = "\t%s\n";

		if (BEngCompiler.verbose)
			System.out.println(String.format(VERBOSE_MESSAGE_FORMAT,
					String.format(getCurrentEntry().locationalFormat(), message)));
	}

	public void vvDebug(String message) {
		final String VERBOSE_MESSAGE_FORMAT = "\t\t%s\n";

		if (BEngCompiler.vv)
			System.out.println(String.format(VERBOSE_MESSAGE_FORMAT,
					String.format(getCurrentEntry().locationalFormat(), message)));
	}
}
