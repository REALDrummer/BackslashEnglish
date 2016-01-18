package com.beng.op;

import com.beng.emulator.NASMCommand;
import com.beng.emulator.args.NASMArg;

public class Assemblage {
	private final NASMCommand[] lines;
	private final NASMArg result_value;

	public Assemblage(NASMCommand[] lines, NASMArg result_value) {
		this.lines = lines;
		this.result_value = result_value;
	}
	
	public Assemblage(NASMArg result_value) {
		lines = new NASMCommand[0];
		this.result_value = result_value;
	}

	public NASMCommand[] getLines() {
		return lines;
	}

	public NASMArg getResultValue() {
		return result_value;
	}
	
	public String assembleCommands() {
		StringBuilder builder = new StringBuilder();
		for (NASMCommand line : lines) {
			builder.append("\t" + line.toString() + "\n");
		}
		return builder.toString();
	}
}
