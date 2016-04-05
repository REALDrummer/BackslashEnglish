package com.beng.opsyntax;

import com.beng.op.OpDefinition;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCall;
import com.beng.op.parseexceptions.NoMatchException;
import com.beng.op.parseexceptions.OpParseException;
import com.beng.op.recallentries.RecallEntry;
import com.beng.opsyntax.quantifiable.escaped.OpEscapedCharacterSyntax;
import com.beng.opsyntax.quantifiable.escaped.OpWordCharSyntax;
import com.beng.parser.Parser;

public class OpWordBoundarySyntax extends OpEscapedCharacterSyntax implements LiteralMatchingSyntax {
	public OpWordBoundarySyntax(Parser input) {
		super(input, 'b', "word boundary");
	}

	@Override
	public boolean canMatchNothing() {
		return true;
	}

	@Override
	public OpCall<?> parseCall(Parser input, OpDefinition op_to_parse, RecallEntry recall_entry)
			throws OpParseException {
		/* first, go back and get the character before this to check whether or not it's a word character */
		input.ungetByte();

		/* now make sure that either the first character is not a word character and the next one is or vice versa */
		char first_character = (char) input.readByte();
		if (OpWordCharSyntax.isWordCharacter(first_character) == OpWordCharSyntax
				.isWordCharacter((char) input.peekByte()))
			throw new NoMatchException(op_to_parse, this);

		/* remember not to parse over the character we peeked at; \b does not consume characters */
		return new FullyAppliedOpCall<OpDefinition>(op_to_parse, input.getLocation(), null);
	}

	@Override
	public String toString() {
		return "\\b";
	}
	
	@Override
	public String toString(OpCall call, RecallEntry recall_entry) {
		/* word boundaries have no length! */
		return "";
	}
}
