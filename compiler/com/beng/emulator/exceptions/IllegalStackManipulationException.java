package com.beng.emulator.exceptions;

import com.beng.emulator.FullyAppliedNASMCommandCall;
import com.beng.emulator.NASMEmulator;

/** This {@link EmulationException} represents an error that occurs when the stack pointer (
 * {@link com.beng.emulator.args.NASMGeneralRegister#RSP rsp}) or base pointer (
 * {@link com.beng.emulator.args.NASMGeneralRegister#RBP rbp}) is manipulated in a way not allowed by the
 * {@link NASMEmulator}. These restrictions exist in the early version of this compiler in order to simplify the
 * manipulation and emulation of the stack; this {@link EmulationException} will probably be eliminated in future
 * versions of this compiler. */
public class IllegalStackManipulationException extends EmulationException {
	private static final long serialVersionUID = -6672425703013425948L;

	private static final String MESSAGE_FOOTER = "\nThe stack may only be incremeted or decremented by constant values.";

	public IllegalStackManipulationException(NASMEmulator emulator, FullyAppliedNASMCommandCall erroneous_call,
			String message) {
		super(emulator, erroneous_call, message + MESSAGE_FOOTER);
	}
}
