package com.beng.emulator.args;

import com.beng.op.Assemblable;
import com.beng.op.Assemblage;
import com.beng.op.Op;

public interface NASMArg extends Op, Assemblable {
	public static final short NO_SIZE = -1;
	
	public default Assemblage assemble() {
		return new Assemblage(this);
	}

	public abstract short getSizeInBits();
	
	public abstract boolean isMutable();

	@Override
	public abstract String toString();
}
