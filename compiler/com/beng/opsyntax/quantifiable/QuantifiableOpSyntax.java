package com.beng.opsyntax.quantifiable;

import com.beng.opsyntax.OpSyntax;
import com.beng.opsyntax.quantified.QuantifiedOpSyntax;
import com.beng.parser.Parser;

public abstract class QuantifiableOpSyntax extends OpSyntax {
	protected static OpSyntax parseQuantifiedOrPlain(QuantifiableOpSyntax inner_group, Parser input) {
		QuantifiedOpSyntax quantified = QuantifiedOpSyntax.parseQuantifier(input, inner_group);
		if (quantified == null)
			return inner_group;
		else
			return quantified;
	}
}
