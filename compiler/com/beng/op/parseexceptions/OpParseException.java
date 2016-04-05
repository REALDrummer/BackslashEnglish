package com.beng.op.parseexceptions;

import com.beng.op.Op;

public class OpParseException extends Exception {
	private static final long serialVersionUID = -8932405281512332319L;

	final Op op_to_parse;
	final char match_priority;

	public OpParseException(Op op_to_parse, char match_priority) {
		this.op_to_parse = op_to_parse;
		this.match_priority = match_priority;
	}

	public OpParseException(Op op_to_parse) {
		this.op_to_parse = op_to_parse;
		match_priority = 0;
	}

	public Op getOpToParse() {
		return op_to_parse;
	}

	public char getMatchPriority() {
		return match_priority;
	}
}
