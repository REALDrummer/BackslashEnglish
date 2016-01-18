package com.beng.opsyntax.quantifiable.escaped;

import com.beng.parser.Parser;

public class OpDigitSyntax extends OpEscapedCharacterSyntax {
	public OpDigitSyntax(Parser input) {
		super(input, 'd', "digit");
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}
}
