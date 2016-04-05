package com.beng.emulator.exceptions;

import com.beng.emulator.FullyAppliedNASMCommandCall;
import com.beng.emulator.NASMEmulator;
import com.beng.emulator.args.NASMArg;

public class WrongArgTypeException extends EmulationException {
	public WrongArgTypeException(NASMEmulator emulator, FullyAppliedNASMCommandCall call,
			int erroneous_argument_index, Class<?> expected_type) {
		super(emulator, call,
				"I expected this NASM command call's [" + erroneous_argument_index
						+ "] argument to be a(n) " + expected_type + ", but instead I got a "
						+ call.getArguments()[erroneous_argument_index].getClass() + "!");
	}
}
