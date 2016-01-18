package com.beng.emulator.args;

import com.beng.op.Assemblage;

public abstract interface NASMArg {
	public abstract Assemblage assemble();

	public abstract short getSize();

	public abstract String toString();
}
