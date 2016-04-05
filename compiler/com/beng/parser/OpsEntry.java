package com.beng.parser;

import com.beng.op.Op;
import com.beng.op.StandardOp;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.parseexceptions.OpParseException;

public interface OpsEntry {
	public default Op parseOp(Parser input) throws OpParseException {
		return parse(input, StandardOp.THING);
	}

	public Op parse(Parser input, FullyAppliedOpCall type) throws OpParseException;
}
