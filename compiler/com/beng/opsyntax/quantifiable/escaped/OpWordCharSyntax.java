package com.beng.opsyntax.quantifiable.escaped;

import com.beng.parser.Parser;

public class OpWordCharSyntax extends OpEscapedCharacterSyntax {
	public OpWordCharSyntax(Parser input) {
		super(input, 'w', "word character");
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}

}
