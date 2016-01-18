package com.beng.opsyntax.quantifiable;

import com.beng.opsyntax.OperatorDefinitionErrorType;
import com.beng.parser.Parser;

public class OpLiteralCharSyntax extends QuantifiableOpSyntax {
	final char character;

	public OpLiteralCharSyntax(Parser input) {
		byte last_character = input.readByte();
		switch (last_character) {
		case '\\':
			switch (input.peekByte()) {
			// literal escapes of special characters, e.g. '\<'
			case '<':
			case '`':
			case '{':
			case '[':
			case '(':
			case '"':
			case '}':
			case ']':
			case ')':
			case '*':
			case '+':
			case '?':
			case '\\':
			case '|':
			case ':':
			case ' ':
			case '.':
				character = (char) input.readByte();
				break;
			// escaped C-style plain characters, e.g. '\t'
			case 'n':
				character = '\n';
				break;
			case 'f':
				character = '\f';
				break;
			case 't':
				character = '\t';
				break;
			case 'r':
				character = '\r';
				break;
			// unicode/hex code escapes: not supported yet!
			case 'u':
			case 'U':
			case 'x':
				input.err(OperatorDefinitionErrorType.BAD_ESCAPED_CHARACTER,
						"I found a hex code point escape. These are not supported yet, but will be supported in the future. Sorry!");
				character = '\0';
				break;
			default:
				input.err(OperatorDefinitionErrorType.BAD_ESCAPED_CHARACTER,
						"there was an escaped character that I didn't recognize!\n"
								+ "If you wanted to just use the backslash as is (a literal backslash), you can put a backslash "
								+ "before it or surround it with a metaquote region (\"\\Q\" and \"\\E\").");
				character = '\0';
				break;
			}
			break;

		default:
			character = (char) last_character;
			break;
		}
	}

	public OpLiteralCharSyntax(char charater) {
		this.character = charater;
	}
	
	public static OpLiteralCharSyntax[] fromString(String string) {
		OpLiteralCharSyntax[] results = new OpLiteralCharSyntax[string.length()];
		for (int i = 0; i < results.length; i++) {
			results[i] = new OpLiteralCharSyntax(string.charAt(i));
		}
		return results;
	}
	
	@Override
	public boolean canMatchNothing() {
		return false;
	}

	public char getChar() {
		return character;
	}
	
	@Override
	public String toString() {
		return toPrintable(character, false, false, false, true);
	}

	public static String toPrintable(char character) {
		return toPrintable(character, true);
	}

	public static String toPrintable(char character, boolean single_quoted) {
		return toPrintable(character, single_quoted, false);
	}

	public static String toPrintable(char character, boolean single_quoted, boolean negative_1_EOF) {
		return toPrintable(character, single_quoted, negative_1_EOF, false);
	}

	public static String toPrintable(char character, boolean single_quoted, boolean negative_1_EOF,
			boolean with_hexcode) {
		return toPrintable(character, single_quoted, negative_1_EOF, with_hexcode, false);
	}

	public static String toPrintable(char character, boolean single_quoted, boolean negative_1_EOF,
			boolean with_hexcode, boolean in_op_declaration) {
		String char_printable;
		switch (character) {
		case '\n':
			char_printable = "\\n";
			break;
		case '\t':
			char_printable = "\\t";
			break;
		case '\f':
			char_printable = "\\t";
			break;
		case '\r':
			char_printable = "\\r";
			break;
		// literal escapes of special characters, e.g. '\<'
		case '<':
		case '`':
		case '{':
		case '[':
		case '(':
		case '}':
		case ']':
		case ')':
		case '*':
		case '+':
		case '?':
		case '\\':
		case '|':
		case ':':
		case ' ':
		case '.':
			char_printable = (in_op_declaration ? "\\" : "") + character;
			break;
		case (char) -1:
			if (negative_1_EOF) {
				char_printable = "the end of the file";
				break;
			} // else fallthrough
		default:
			if (Character.isJavaIdentifierPart(character)
					|| character < 128 && !Character.isWhitespace(character))
				char_printable = String.valueOf(character);
			else
				char_printable = "\\u" + String.format("%04x", (int) character);
		}

		if (single_quoted)
			char_printable = "'" + char_printable + "'";

		if (!char_printable.startsWith("\\u") && with_hexcode)
			char_printable += " (hex code 0x" + Integer.toHexString(character).toUpperCase() + ")";

		return char_printable;
	}
}
