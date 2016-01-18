package com.beng.opsyntax;

import com.beng.opsyntax.quantifiable.OpLiteralCharSyntax;
import com.beng.parser.Parser;
import com.beng.parser.ParserErrorType;

public class OpWhitespaceGroupSyntax extends OpSyntax {
	public OpWhitespaceGroupSyntax(Parser input) {
		if (!Character.isWhitespace((char) input.peekByte()))
			input.err(ParserErrorType.PARSE_MISCALL,
					"I attempted to parse a whitespace character, but I got "
							+ OpLiteralCharSyntax.toPrintable((char) input.peekByte(), true)
							+ " instead!");

		while (Character.isWhitespace((char) input.peekByte()))
			input.readByte();
	}

	@Override
	public boolean canMatchNothing() {
		return true;
	}

	@Override
	public String toString() {
		return " ";
	}

}
