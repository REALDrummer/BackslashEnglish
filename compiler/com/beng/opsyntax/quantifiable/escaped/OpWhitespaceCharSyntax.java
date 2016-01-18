package com.beng.opsyntax.quantifiable.escaped;

import com.beng.parser.Parser;

public class OpWhitespaceCharSyntax extends OpEscapedCharacterSyntax {
	public OpWhitespaceCharSyntax(Parser input) {
		super(input, 's', "whitespace character");
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}

}
