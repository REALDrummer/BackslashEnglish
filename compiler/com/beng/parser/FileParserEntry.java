package com.beng.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.beng.BEngCompiler;
import com.beng.SourceFileLocation;
import com.beng.opsyntax.quantifiable.OpLiteralCharSyntax;

public class FileParserEntry extends ParserEntry {
	private final RandomAccessFile input;
	private final String input_file_path;

	public FileParserEntry(Parser parser, File input_file, int line_number, int column_number, long position) {
		super(parser, line_number, column_number, position);

		input_file_path = input_file.getAbsolutePath();

		// check the input file
		if (!input_file.exists())
			BEngCompiler.err(ParserErrorType.FILE_NOT_FOUND,
					"The input file given \"" + input_file_path + "\" doesn't exist!");
		else if (input_file.isDirectory())
			BEngCompiler.err(ParserErrorType.FILE_IO_ERROR,
					"The input file given \"" + input_file_path + "\" is a folder, not a file!");
		else if (!input_file.canRead())
			BEngCompiler.err(ParserErrorType.FILE_IO_ERROR,
					"The input file given \"" + input_file_path + "\" is not readable to me!");

		RandomAccessFile __input; // this exists because of a bug in the Java compiler concerning finality
		try {
			__input = new RandomAccessFile(input_file_path, "r");
		} catch (FileNotFoundException exception) {
			parser.err(ParserErrorType.FILE_NOT_FOUND,
					"I could not find the file \"" + input_file_path + "\" to be parsed!");
			__input = null;
		}
		input = __input;

		try {
			if (position != 0)
				input.seek(position);
		} catch (IOException exception) {
			parser.err(ParserErrorType.FILE_IO_ERROR, "there was a problem seeking to byte #"
					+ Long.toUnsignedString(position) + " in " + input_file_path);
		}
	}

	public FileParserEntry(Parser parser, File input_file) {
		this(parser, input_file, 1, 1, 0);
	}

	@Override
	protected String locationalFormat() {
		return "At char " + Integer.toUnsignedString(getColumnNumber()) + " on line "
				+ Integer.toUnsignedString(getLineNumber()) + " of " + getInputFilePath() + ", %s";
	}

	public SourceFileLocation getLocation() {
		return new SourceFileLocation(input_file_path, getLineNumber(), getColumnNumber());
	}

	@Override
	public long getPosition() {
		try {
			return input.getFilePointer();
		} catch (IOException e) {
			parser.err(ParserErrorType.FILE_IO_ERROR,
					"there was an IO error while attempting to get the position of the file!");
			return -1;
		}
	}

	public String getInputFilePath() {
		return input_file_path;
	}

	protected void seek(int relative_bytes, boolean print_debug) {
		if (relative_bytes == 0)
			return;

		if (print_debug)
			parser.verboseDebug("seeking " + relative_bytes + "B from position " + getPosition() + "...");

		if (relative_bytes > 0)
			/* since we have to keep track of line breaks and line lengths as we go, there is not much more efficiency to be gained from simply reading byte by byte */
			for (int i = 0; i < relative_bytes; i++)
				readByte();
		else {
			int relative_bytes_remaining = -relative_bytes;

			// recalculate the line number and column number
			final String BACKWARD_SEEK_ERROR_MESSAGE = "this parser attempted to seek "
					+ relative_bytes_remaining + "B backward from line " + getLineNumber()
					+ ", char " + getColumnNumber() + " backward past its starting point!";

			if (relative_bytes_remaining <= chars_read_this_line)
				chars_read_this_line -= relative_bytes_remaining;
			else {
				if (print_debug)
					parser.vvDebug("chars read this line = " + chars_read_this_line);
				relative_bytes_remaining -= chars_read_this_line;
				chars_read_this_line = 0;

				while (!line_lengths.isEmpty()
						&& relative_bytes_remaining >= chars_read_this_line + 1) {
					if (print_debug)
						parser.vvDebug("seeked back 1 " + (chars_read_this_line + 1)
								+ "-char line");
					relative_bytes_remaining -= chars_read_this_line + 1 /* the '\n' itself */;
					chars_read_this_line = line_lengths.pop();
				}

				if (line_lengths.isEmpty() && chars_read_this_line <= relative_bytes_remaining)
					parser.err(ParserErrorType.BACKWARD_SEEK, BACKWARD_SEEK_ERROR_MESSAGE);
				else
					chars_read_this_line -= relative_bytes_remaining;
			}

			// perform the seek operation on the RandomAccessFile
			try {
				input.seek(getPosition() + relative_bytes);
			} catch (IOException exception) {
				parser.err(ParserErrorType.FILE_IO_ERROR,
						"I couldn't seek back " + relative_bytes + "!");
			}
		}
	}

	protected byte readByte(boolean print_debug) {
		if (print_debug)
			parser.vvDebug("reading byte...");
		byte read_byte;
		try {
			read_byte = (byte) input.read();
		} catch (IOException e) {
			parser.err(ParserErrorType.FILE_IO_ERROR,
					"there was a problem trying to read the next byte from " + input_file_path
							+ "!");
			return -1;
		}

		if (print_debug)
			parser.verboseDebug("read " + OpLiteralCharSyntax.toPrintable((char) read_byte, true));
		if (read_byte != -1)
			if (read_byte == '\n') {
				parser.vvDebug("found line break; updating line count...");
				line_lengths.push(chars_read_this_line);
				parser.vvDebug("line lengths = " + line_lengths.toString());
				chars_read_this_line = 0;
			} else {
				chars_read_this_line++;
			}

		return read_byte;
	}
}
