package com.beng.opsyntax;

import com.beng.parser.Parser;

public class OpWordBoundarySyntax extends OpSyntax {
	public OpWordBoundarySyntax(Parser input) {
		input.parseOrErr("\\b", "word boundary");
	}

	public static OpWordBoundarySyntax parse(Parser input) {
		return new OpWordBoundarySyntax(input);
	}

	@Override
	public boolean canMatchNothing() {
		return true;
	}

	@Override
	public String toString() {
		return "\\b";
	}
}
