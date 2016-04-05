package com.beng.op.args;

import com.beng.op.IncompleteArgumentException;
import com.beng.op.Op;
import com.beng.op.OpRunException;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCall;

public interface OpArg {
	public default Object tryRun(OpCall call) throws OpRunException {
		if (this instanceof FullyAppliedOpCall)
			return ((FullyAppliedOpCall) this).run();
		else
			throw new IncompleteArgumentException(call, this);
	}

	public default FullyAppliedOpCall checkFull(OpCall call) throws OpRunException {
		if (this instanceof FullyAppliedOpCall)
			return (FullyAppliedOpCall) this;
		else
			throw new IncompleteArgumentException(call, this);
	}

	public FullyAppliedOpCall getType();

	@Override
	public String toString();
}
