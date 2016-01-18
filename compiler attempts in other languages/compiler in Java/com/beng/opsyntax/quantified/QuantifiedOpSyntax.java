package com.beng.opsyntax.quantified;

import com.beng.opsyntax.OpSyntax;
import com.beng.opsyntax.quantifiable.QuantifiableOpSyntax;
import com.beng.parser.Parser;

public abstract class QuantifiedOpSyntax extends OpSyntax {
	protected static final int NO_UPPER_BOUND = 0;

	final OpSyntax inner_group;

	protected QuantifiedOpSyntax(OpSyntax inner_group) {
		this.inner_group = inner_group;
	}

	public OpSyntax getInnerGroup() {
		return inner_group;
	}

	public abstract byte getLowerBound();

	public abstract byte getUpperBound();

	@Override
	public boolean canMatchNothing() {
		return getLowerBound() == 0 || inner_group.canMatchNothing();
	}

	public abstract String quantifierToString();

	@Override
	public String toString() {
		return inner_group.toString() + quantifierToString();
	}

	public static QuantifiedOpSyntax parseQuantifier(Parser input, QuantifiableOpSyntax inner_group) {
		byte quantifier_first_byte = input.peekByte();
		switch (quantifier_first_byte) {
		case '+':
			return new OpPlussedSyntax(inner_group, input);
		case '*':
			return new OpStarredSyntax(inner_group, input);
		case '?':
			return new OpOptionalSyntax(inner_group, input);
		case '{':
			return new OpRawQuantifiedSyntax(inner_group, input);
		default:
			return null; // error case: no quantifier
		}
	}
}