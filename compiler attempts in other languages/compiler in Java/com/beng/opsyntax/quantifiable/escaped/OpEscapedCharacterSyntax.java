package com.beng.opsyntax.quantifiable.escaped;

import com.beng.opsyntax.quantifiable.QuantifiableOpSyntax;
import com.beng.parser.Parser;

public abstract class OpEscapedCharacterSyntax extends QuantifiableOpSyntax {
	final byte escaped_character;

	public OpEscapedCharacterSyntax(Parser input, char escaped_character, String escaped_char_name) {
		input.parseOrErr("\\" + escaped_character, escaped_char_name);
		
		this.escaped_character = (byte) escaped_character;
	}

	@Override
	public String toString() {
		return "\\" + (char) escaped_character;
	}

}
