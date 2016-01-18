package com.beng.opsyntax.quantifiable;

import com.beng.parser.Parser;

public class OpMetaquoteSyntax extends QuantifiableOpSyntax {
	String literal;

	public OpMetaquoteSyntax(Parser input) {
		input.parseOrErr("\\Q", "metaquote opening escape sequence");

		StringBuilder metaquote_interior = new StringBuilder();
		while (true) {
			byte next_character = input.readByte();
			if (next_character == -1 /* EOF */) {
				break;
			} else if (next_character == '\\' && input.peekByte() == 'E') {
				input.readByte(); // skip past the 'E'
				break; // finish parsing
			}

			// add the character to the string
			metaquote_interior.append(next_character);
		}

		literal = metaquote_interior.toString();
	}

	@Override
	public boolean canMatchNothing() {
		return literal.length() == 0;
	}

	@Override
	public String toString() {
		return "\\Q" + literal + "\\E";
	}
}
