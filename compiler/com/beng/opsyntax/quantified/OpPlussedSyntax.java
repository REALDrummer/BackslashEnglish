package com.beng.opsyntax.quantified;

import java.util.List;

import com.beng.SourceFileLocation;
import com.beng.op.OpDefinition;
import com.beng.op.args.GivenOpArg;
import com.beng.op.call.OpCall;
import com.beng.op.call.OpCallException;
import com.beng.op.recallentries.RecallEntry;
import com.beng.opsyntax.OpSyntax;
import com.beng.parser.Parser;

public class OpPlussedSyntax extends QuantifiedOpSyntax {
	public OpPlussedSyntax(OpSyntax inner_group, Parser input) {
		super(inner_group);

		input.parseOrErr("+", "plus quantifier");
	}

	@Override
	public byte getLowerBound() {
		return 1;
	}

	@Override
	public byte getUpperBound() {
		return NO_UPPER_BOUND;
	}

	@Override
	public String quantifierToString() {
		return "+";
	}

}
