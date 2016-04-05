package com.beng.op;

import com.beng.op.call.FullyAppliedOpCall;

public interface Assemblable {
	public abstract Assemblage assembleWith(FullyAppliedOpCall call) throws OpRunException;

}
