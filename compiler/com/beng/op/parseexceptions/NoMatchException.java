package com.beng.op.parseexceptions;

import com.beng.op.Op;
import com.beng.opsyntax.OpSyntax;

public class NoMatchException extends OpParseException {
	private OpSyntax non_matching_syntax;

	public NoMatchException(Op op_to_parse, OpSyntax non_matching_syntax) {
		super(op_to_parse);

		this.non_matching_syntax = non_matching_syntax;
	}

	public OpSyntax getNonMatchingSyntax() {
		return non_matching_syntax;
	}

	@Override
	public String getMessage() {
		return "I found no match for the \"" + non_matching_syntax.toString() + "\" group in "
				+ op_to_parse.toString() + "!";
	}
}
