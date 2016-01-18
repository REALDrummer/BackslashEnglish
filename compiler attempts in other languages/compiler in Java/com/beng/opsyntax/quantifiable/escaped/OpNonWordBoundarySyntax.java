package com.beng.opsyntax.quantifiable.escaped;

import com.beng.parser.Parser;

public class OpNonWordBoundarySyntax extends OpEscapedCharacterSyntax {
	public OpNonWordBoundarySyntax(Parser input) {
		super(input, 'B', "non-word boundary");
	}

	@Override
	public boolean canMatchNothing() {
		return true;
	}

}
