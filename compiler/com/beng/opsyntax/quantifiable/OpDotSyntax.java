package com.beng.opsyntax.quantifiable;

import com.beng.parser.Parser;

public class OpDotSyntax extends QuantifiableOpSyntax {
	public OpDotSyntax(Parser input) {
		input.parseOrErr(".", "op declaration dot metacharacter");
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}

	@Override
	public String toString() {
		return ".";
	}

}
