package com.beng.emulator.args;

import com.beng.op.Assemblage;

public class NASMXMMRegister implements NASMArg {

	@Override
	public Assemblage assemble() {
		return new Assemblage(this);
	}

	@Override
	public short getSize() {
		return 128;
	}

}
