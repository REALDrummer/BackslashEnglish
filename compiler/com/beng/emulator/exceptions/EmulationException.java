package com.beng.emulator.exceptions;

import com.beng.emulator.FullyAppliedNASMCommandCall;
import com.beng.emulator.NASMEmulator;

public class EmulationException extends Exception {
	private final NASMEmulator emulator;
	private final FullyAppliedNASMCommandCall erroneous_call;

	public EmulationException(NASMEmulator emulator, FullyAppliedNASMCommandCall erroneous_call, String message) {
		super(message);

		this.emulator = emulator;
		this.erroneous_call = erroneous_call;
	}

	public EmulationException(NASMEmulator emulator, FullyAppliedNASMCommandCall erroneous_call, String message,
			Throwable cause) {
		super(message, cause);

		this.emulator = emulator;
		this.erroneous_call = erroneous_call;
	}
}
