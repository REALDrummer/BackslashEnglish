package com.beng.emulator.args;

import com.beng.emulator.args.NASMArg;
import com.beng.op.Assemblage;
import com.beng.op.OpRunException;
import com.beng.op.StandardOp;
import com.beng.op.SysOp;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.opsyntax.OpSyntax;

public class NASMInteger implements NASMArg {
	private final int value;

	public NASMInteger(int value) {
		this.value = value;
	}

	@Override
	public FullyAppliedOpCall getReturnType() {
		return StandardOp.INTEGER;
	}

	@Override
	public short getSizeInBits() {
		return NO_SIZE;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public OpSyntax getSyntax() {
		return SysOp.INTEGER.getSyntax();
	}

	@Override
	public Assemblage assembleWith(FullyAppliedOpCall call) throws OpRunException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMutable() {
		return false;
	}
}
