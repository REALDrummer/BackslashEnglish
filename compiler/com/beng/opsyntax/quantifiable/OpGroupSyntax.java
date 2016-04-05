package com.beng.opsyntax.quantifiable;

import com.beng.op.args.OpArg;
import com.beng.opsyntax.OpSyntax;
import com.beng.opsyntax.grouped.OpOrSyntax;
import com.beng.parser.Parser;

public class OpGroupSyntax extends QuantifiableOpSyntax {
	OpSyntax inner_group;

	public OpGroupSyntax(Parser input) {
		input.parseOrErr("(", "opening parenthesis of capturing group");

		inner_group = new OpOrSyntax(input);
		if (((OpOrSyntax) inner_group).getGroups().length == 1)
			inner_group = ((OpOrSyntax) inner_group).getGroups()[0];

		input.parseOrErr(")", "closing parenthesis of a capturing group");
	}
	
	@Override
	public OpArg[] parse(Parser input) {
		return inner_group.parse(input);
	}

	@Override
	public boolean canMatchNothing() {
		return inner_group.canMatchNothing();
	}

	@Override
	public String toString() {
		return "(" + inner_group.toString() + ")";
	}

}
