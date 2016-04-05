package com.beng.emulator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.beng.emulator.args.NASMArg;
import com.beng.emulator.args.NASMLabel;
import com.beng.emulator.exceptions.EmulationException;
import com.beng.emulator.exceptions.StackStateException;
import com.beng.op.Assemblage;
import com.beng.output.OutputHandler;

public class NASMEmulator {
	private List<StaticMemoryBlock> static_memory = new LinkedList<>();
	private Map<NASMArg, MemoryBlock> state = new HashMap<NASMArg, MemoryBlock>();
	private int local_stack_size = 0;

	public NASMArg emulateOrOutput(Assemblage assemblage, OutputHandler output) {
		NASMLabel label = null;
		for (FullyAppliedNASMCommandCall line : assemblage.getLines()) {
			try {
				// try to emulate the Assembly command
				line.emulateOn(this);
			} catch (EmulationException exception) {
				// if the Assembly can't be emulated, put it in the final output
				label = output.outputAndLabel(line);
			}
		}

		/* if this was the last operation in an operator and no other return value was given, use the label of
		 * the operator */
		if (assemblage.getReturnValue() == null)
			return label;
		else
			return assemblage.getReturnValue();
	}

	public Map<NASMArg, MemoryBlock> getState() {
		return state;
	}

	public int getLocalStackSize() {
		return local_stack_size;
	}

	public void addLocalStackSize(int value, FullyAppliedNASMCommandCall call) throws EmulationException {
		int new_local_stack_size = local_stack_size + value;
		if (value < 0)
			throw new StackStateException(this, call,
					"A NASM command attempted to set the stack pointer below the base pointer! This cannot be!");
		else
			local_stack_size = new_local_stack_size;
	}

	public void resetLocalStack() {
		local_stack_size = 0;
	}
	
	public List<StaticMemoryBlock> getStaticMemory() {
		return static_memory;
	}
}
