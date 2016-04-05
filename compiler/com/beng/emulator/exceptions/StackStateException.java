package com.beng.emulator.exceptions;

import com.beng.emulator.FullyAppliedNASMCommandCall;
import com.beng.emulator.NASMEmulator;

/** This {@link EmulationException} occurs when a command attempts to manipulate the {@link NASMEmulator}'s stack into
 * an illegal state, e.g. decrementing the stack pointer ({@link com.beng.emulator.args.NASMGeneralRegister#RSP rsp})
 * below the base pointer ({@link com.beng.emulator.args.NASMGeneralRegister#RBP rbp}). */
public class StackStateException extends EmulationException {
	private static final long serialVersionUID = -7405337063940473526L;

	public StackStateException(NASMEmulator emulator, FullyAppliedNASMCommandCall erroneous_call, String message) {
		super(emulator, erroneous_call, message);
	}
}
