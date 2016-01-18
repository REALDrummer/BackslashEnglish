package com.beng.emulator.args;

public final class NASMArgRestriction {
	public static final NASMArgRestriction GENERAL_REGISTER = new NASMArgRestriction(
			argument -> argument instanceof NASMGeneralRegister),
			ONLY_EFFECTIVE_ADDRESS = new NASMArgRestriction(
					argument -> argument instanceof NASMEffectiveAddress),
			ONLY_XMM_REGISTER = new NASMArgRestriction(argument -> argument instanceof NASMXMMRegister),
			LESS_THAN_EQUAL_32_BIT = new NASMArgRestriction(
					argument -> argument.getSize() <= 32 || argument.getSize() == -1),
			LESS_THAN_EQUAL_16_BIT = new NASMArgRestriction(
					argument -> argument.getSize() <= 16 || argument.getSize() == -1),
			LESS_THAN_EQUAL_8_BIT = new NASMArgRestriction(
					argument -> argument.getSize() <= 8 || argument.getSize() == -1);

	private final NASMArgChecker checker;

	private static interface NASMArgChecker {
		boolean checksOut(NASMArg argument);
	}

	private NASMArgRestriction(NASMArgChecker checker) {
		this.checker = checker;
	}

	public boolean fits(NASMArg argument) {
		return checker.checksOut(argument);
	}

	protected NASMArgChecker getChecker() {
		return checker;
	}
}