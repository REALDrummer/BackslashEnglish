package com.beng.emulator;

import java.util.Arrays;

import com.beng.emulator.args.NASMEffectiveAddress;
import com.beng.emulator.args.NASMGeneralRegister;
import com.beng.emulator.args.NASMInteger;
import com.beng.op.Assemblage;
import com.beng.op.Op;
import com.beng.op.OpRunException;
import com.beng.op.StandardOp;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCallException;
import com.beng.opsyntax.OpSyntax;

public class NASMStackRelativeEffectiveAddress extends NASMEffectiveAddress {
	private final int memsize;
	private final int base_pointer_offset;

	private static FullyAppliedOpCall basePointerPlus(int offset) {
		try {
			return StandardOp.ADD
					.callWith(Arrays.asList(NASMGeneralRegister.RBP, new NASMInteger(offset)), null)
					.checkFull(null);
		} catch (OpRunException | OpCallException e) {
			throw new RuntimeException(
					"Something went wrong attempting to add RBP to a constant integer to calculate a stack relative address!");
		}
	}

	public NASMStackRelativeEffectiveAddress(int memsize, int base_pointer_offset) {
		super(memsize, basePointerPlus(base_pointer_offset));

		this.memsize = memsize;
		this.base_pointer_offset = base_pointer_offset;
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public OpSyntax getSyntax() {
		String size_modifer = getSizeModifier();
		if (size_modifer != null)
			return OpSyntax.parseDeclaration(size_modifer + "\\[rbp+" + base_pointer_offset + "\\]");
		else
			return OpSyntax.parseDeclaration("\\[rbp+" + base_pointer_offset + "\\]");
	}

	@Override
	public Op getType() {
		return StandardOp.EFFECTIVE_ADDRESS;
	}

}
