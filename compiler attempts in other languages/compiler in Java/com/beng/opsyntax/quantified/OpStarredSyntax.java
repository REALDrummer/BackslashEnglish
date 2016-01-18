package com.beng.opsyntax.quantified;

import com.beng.opsyntax.OpSyntax;
import com.beng.parser.Parser;

public class OpStarredSyntax extends QuantifiedOpSyntax {
	public OpStarredSyntax(OpSyntax inner_group, Parser input) {
		super(inner_group);

		input.parseOrErr("*", "star quantifier");
	}

	@Override
	public boolean canMatchNothing() {
		return true;
	}

	@Override
	public byte getLowerBound() {
		return 0;
	}

	@Override
	public byte getUpperBound() {
		return NO_UPPER_BOUND;
	}

	@Override
	public String quantifierToString() {
		return "*";
	}
}
