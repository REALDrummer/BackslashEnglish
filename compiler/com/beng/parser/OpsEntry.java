package com.beng.parser;

import com.beng.op.Op;
import com.beng.op.StandardOp;
import com.beng.op.SysOp;
import com.beng.op.parseexceptions.OpParseException;

public interface OpsEntry {
	public default Op parseOp(Parser input) throws OpParseException {
		return parse(input, SysOp.THING);
	}

	public Op parse(Parser input, Op type) throws OpParseException;
}
