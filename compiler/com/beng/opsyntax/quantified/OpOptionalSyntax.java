package com.beng.opsyntax.quantified;

import com.beng.opsyntax.quantifiable.QuantifiableOpSyntax;
import com.beng.parser.Parser;

public class OpOptionalSyntax extends QuantifiedOpSyntax {
	public OpOptionalSyntax(QuantifiableOpSyntax inner_group, Parser input) {
		super(inner_group);

		input.parseOrErr("?", "optional quantifier");
	}

	@Override
	public byte getLowerBound() {
		return 0;
	}

	@Override
	public byte getUpperBound() {
		return 1;
	}

	@Override
	public String quantifierToString() {
		return "?";
	}

}
