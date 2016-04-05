package com.beng.emulator;

import com.beng.SourceFileLocation;
import com.beng.emulator.args.NASMArg;
import com.beng.emulator.exceptions.EmulationException;
import com.beng.op.OpDefinition;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.recallentries.RecallEntry;

public class FullyAppliedNASMCommandCall extends FullyAppliedOpCall {
	private final NASMArg[] arguments;

	public FullyAppliedNASMCommandCall(NASMCommand declaration, SourceFileLocation location,
			RecallEntry recall_entry, NASMArg... arguments) {
		super(declaration, location, recall_entry);
		
		this.arguments = arguments;
	}
	
	public NASMArg[] getArguments() {
		return arguments;
	}
	
	@Override
	public NASMCommand getDefinition() {
		return (NASMCommand) super.getDefinition();
	}

	public boolean emulateOn(NASMEmulator emulator) throws EmulationException {
		return ((NASMCommand) getDefinition()).emulateOn(emulator, this);
	}
}
