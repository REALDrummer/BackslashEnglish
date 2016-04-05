package com.beng.op;

import com.beng.op.args.GivenOpArg;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.opsyntax.OpSyntax;

public interface Op extends GivenOpArg {
	@Override
	public default Op getType() {
		return getReturnType();
	}

	public FullyAppliedOpCall getReturnType();

	public OpSyntax getSyntax();

	@Override
	public String toString();
}
