package com.beng.opsyntax.quantifiable.escaped;

import com.beng.parser.Parser;

public class OpWordBoundarySyntax extends OpEscapedCharacterSyntax {
	public OpWordBoundarySyntax(Parser input) {
		super(input, 'b', "word boundary");
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}
}
