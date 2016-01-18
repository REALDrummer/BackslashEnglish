package com.beng.opsyntax.quantifiable.escaped;

import com.beng.parser.Parser;

public class OpNonWhitespaceCharSyntax extends OpEscapedCharacterSyntax {
	public OpNonWhitespaceCharSyntax(Parser input) {
		super(input, 'S', "non-whitespace character");
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}

}
