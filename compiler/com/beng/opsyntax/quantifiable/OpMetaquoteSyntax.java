package com.beng.opsyntax.quantifiable;

import com.beng.op.Op;
import com.beng.op.parseexceptions.NoMatchException;
import com.beng.op.parseexceptions.OpParseException;
import com.beng.op.recallentries.RecallEntry;
import com.beng.opsyntax.OpArguments;
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
	public OpArguments parseCall(Parser input, Op op_to_parse, RecallEntry QR_entry) throws OpParseException {
		for (char literal_char : literal.toCharArray()) {
			if (input.peekByte() != literal_char)
				throw new NoMatchException(op_to_parse, this);
		}

		return new OpArguments(null);
	}

	@Override
	public String toString() {
		return "\\Q" + literal + "\\E";
	}

	@Override
	public String toString(OpArguments arguments) {
		return toString();
	}
}
