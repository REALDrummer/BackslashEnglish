package com.beng.opsyntax.quantifiable.escaped;

import com.beng.parser.Parser;

public class OpNonDigitSyntax extends OpEscapedCharacterSyntax {
	public OpNonDigitSyntax(Parser input) {
		super(input, 'D', "non-digit");
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}

}
