package com.beng.emulator.exceptions;

import com.beng.emulator.FullyAppliedNASMCommandCall;
import com.beng.emulator.NASMEmulator;
import com.beng.emulator.args.NASMArg;

public class WritingImmutableException extends EmulationException {
	private final NASMArg immutable_argument;

	public WritingImmutableException(NASMEmulator emulator, FullyAppliedNASMCommandCall erroneous_call,
			NASMArg immutable_argument) {
		super(emulator, erroneous_call, "A NASM command call attempted to write to an immutable NASM argument: "
				+ immutable_argument + "!");

		this.immutable_argument = immutable_argument;
	}


}
