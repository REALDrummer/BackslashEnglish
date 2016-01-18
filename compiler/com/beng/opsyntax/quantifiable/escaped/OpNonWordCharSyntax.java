package com.beng.opsyntax.quantifiable.escaped;

import com.beng.parser.Parser;

public class OpNonWordCharSyntax extends OpEscapedCharacterSyntax {
	public OpNonWordCharSyntax(Parser input) {
		super(input, 'W', "non-word character");
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}

}
