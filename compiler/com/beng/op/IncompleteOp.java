package com.beng.op;

import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCall;
import com.beng.op.call.OpCallException;
import java.util.ArrayList;
import java.util.List;

import com.beng.BEngCompiler;
import com.beng.SourceFileLocation;
import com.beng.op.args.GivenOpArg;

public interface IncompleteOp extends Op {
	public OpCall callWith(List<GivenOpArg> arguments, SourceFileLocation location) throws OpCallException;

	public default FullyAppliedOpCall callNullary() {
		try {
			return (FullyAppliedOpCall) callWith(new ArrayList<GivenOpArg>(), null);
		} catch (OpCallException exception) {
			BEngCompiler.err(BEngCompiler.INTERNAL_ERROR,
					"The compiler attempted to call an op as nullary that wasn't nullary!");
			return null;
		}
	}
}
