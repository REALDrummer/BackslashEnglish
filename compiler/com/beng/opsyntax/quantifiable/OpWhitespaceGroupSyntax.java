package com.beng.opsyntax.quantifiable;

import com.beng.op.OpDefinition;
import com.beng.op.call.OpCall;
import com.beng.op.parseexceptions.OpParseException;
import com.beng.op.recallentries.RecallEntry;
import com.beng.opsyntax.LiteralMatchingSyntax;
import com.beng.opsyntax.OpSyntax;
import com.beng.parser.Parser;
import com.beng.parser.ParserErrorType;

public class OpWhitespaceGroupSyntax implements QuantifiableOpSyntax, LiteralMatchingSyntax {
	private static final OpSyntax EQUIVALENT = OpSyntax.parseDeclaration("(\\b|\\s)\\s*");

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
	public OpCall parseCall(Parser input, OpDefinition op_to_parse, RecallEntry recall_entry) throws OpParseException {
		return EQUIVALENT.parseCall(input, op_to_parse, recall_entry);
	}

	@Override
	public String toString(OpCall call, RecallEntry recall_entry) {
		return EQUIVALENT.toString(call, recall_entry);
	}
}
