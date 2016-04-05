package com.beng.op;

import com.beng.emulator.FullyAppliedNASMCommandCall;
import com.beng.emulator.args.NASMArg;
import com.beng.op.call.FullyAppliedOpCall;

public class Assemblage {
	private final FullyAppliedNASMCommandCall[] lines;
	private final NASMArg return_value;	// null value here means the NASM command returns itself as a label to be determined during assembly

	@SafeVarargs
	public Assemblage(FullyAppliedNASMCommandCall... lines) {
		this(null, lines);
	}
	
	@SafeVarargs
	public Assemblage(NASMArg return_value, FullyAppliedNASMCommandCall... lines) {
		this.lines = lines;
		this.return_value = return_value;
	}

	public FullyAppliedNASMCommandCall[] getLines() {
		return lines;
	}
	
	public NASMArg getReturnValue() {
		return return_value;
	}
}
