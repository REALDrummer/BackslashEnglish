package com.beng.emulator.args;

import java.util.Arrays;

import com.beng.op.OpRunException;
import com.beng.op.StandardOp;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCallException;

public class NASMLabel implements NASMArg {
	private final String label;

	public NASMLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public FullyAppliedOpCall getReturnType() {
		try {
			return StandardOp.POINTER.callWith(Arrays.asList(StandardOp.NASM_COMMAND), null)
					.checkFull(null);
		} catch (OpRunException | OpCallException exception) {
			throw new RuntimeException(
					"There was a problem trying to construct a NASM command pointer type for the return type of a NASMLabel!");
		}
	}

	@Override
	public short getSizeInBits() {
		return 64;
	}

	@Override
	public String toString() {
		return label;
	}
}
