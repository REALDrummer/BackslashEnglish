package com.beng.emulator;

import com.beng.emulator.args.NASMInteger;
import com.beng.op.Assemblage;
import com.beng.op.OpRunException;
import com.beng.op.call.FullyAppliedOpCall;

import static com.beng.emulator.NASMCommand.*;

public class StaticMemoryBlock extends MemoryBlock {
	public StaticMemoryBlock(NASMEmulator emulator, int size_in_bytes) {
		super(emulator, size_in_bytes);
	}

	@Override
	public Assemblage assembleWith(FullyAppliedOpCall call) throws OpRunException {
		return new Assemblage(new FullyAppliedNASMCommandCall(RESB, null, null, new NASMInteger(getSize())));
	}

}
