package com.beng.emulator;

import com.beng.emulator.exceptions.EmulationException;
import com.beng.op.Assemblage;
import com.beng.op.OpRunException;
import com.beng.op.call.FullyAppliedOpCall;

public class StackMemoryBlock extends MemoryBlock {
	private int base_pointer_offset;

	public StackMemoryBlock(NASMEmulator emulator, int size_in_bytes, FullyAppliedNASMCommandCall call) {
		super(emulator, size_in_bytes);

		base_pointer_offset = emulator.getLocalStackSize();
		try {
			emulator.addLocalStackSize(size_in_bytes, call);
		} catch (EmulationException e) {
			throw new RuntimeException(
					"There was a problem trying to allocate a stack memory block of size "
							+ size_in_bytes + " " + base_pointer_offset
							+ " bytes away from the base pointer!");
		}
	}

	@Override
	public Assemblage assembleWith(FullyAppliedOpCall call) throws OpRunException {
		return new Assemblage(new NASMStackRelativeEffectiveAddress(getSize(), base_pointer_offset));
	}
}
