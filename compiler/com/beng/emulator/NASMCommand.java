package com.beng.emulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beng.BEngCompiler;
import com.beng.emulator.args.NASMArg;
import com.beng.emulator.args.NASMGeneralRegister;
import com.beng.emulator.args.NASMInteger;
import com.beng.emulator.args.NASMLabel;
import com.beng.emulator.exceptions.EmulationException;
import com.beng.emulator.exceptions.IllegalStackManipulationException;
import com.beng.emulator.exceptions.WritingImmutableException;
import com.beng.emulator.exceptions.WrongArgTypeException;
import com.beng.op.Assemblable;
import com.beng.op.Assemblage;
import com.beng.op.OpRunException;
import com.beng.op.StandardOp;
import com.beng.op.SysOp;
import com.beng.op.call.FullyAppliedOpCall;

import static com.beng.emulator.args.NASMGeneralRegister.*;

/** @author connor
 *
 *         This class represents one command from the NASM Assembly language represented as an {@link Op} in the
 *         \English language. */
public abstract class NASMCommand extends SysOp<NASMArg> implements Assemblable {
	public static final NASMCommand ADD = new NASMCommand("add", 2, false) {
		@Override
		public boolean emulateOn(NASMEmulator emulator, FullyAppliedNASMCommandCall call)
				throws EmulationException {
			// make sure the destination is mutable
			if (!call.getArguments()[0].isMutable())
				throw new WritingImmutableException(emulator, call, call.getArguments()[0]);

			/* adjust the emulator's stack size manually as needed; if this is impossible throw an EmulationException */
			if (call.getArguments()[0] instanceof NASMGeneralRegister
					&& ((NASMGeneralRegister) call.getArguments()[0]).getFullRegister() == RSP)
				if (call.getArguments()[1] instanceof NASMInteger)
					emulator.addLocalStackSize(((NASMInteger) call.getArguments()[1]).getValue(),
							call);
				else if (emulator.getState().get(call.getArguments()[1]).hasFinalValue())
					emulator.addLocalStackSize(emulator.getState().get(call.getArguments()[1])
							.getFinalValue().intValue(), call);
				else
					throw new IllegalStackManipulationException(emulator, call,
							"In this first alpha of the \\English compiler, the stack can only be incremented by constant values!");

			/* emulate the command */
			MemoryBlock destination_value = emulator.getState().get(call.getArguments()[0]);
			MemoryBlock augend_value = emulator.getState().get(call.getArguments()[1]);

			if (destination_value.hasFinalValue() && augend_value.hasFinalValue()) {
				destination_value.getFinalValue().add(augend_value.getFinalValue());
				return true;
			} else {
				destination_value.removeFinalValue();
				return false;
			}
		}
	};
	public static final NASMCommand MOV = new NASMCommand("mov", 2, false) {

		@Override
		public boolean emulateOn(NASMEmulator emulator, FullyAppliedNASMCommandCall call)
				throws EmulationException {
			// make sure the destination is mutable
			if (!call.getArguments()[0].isMutable())
				throw new WritingImmutableException(emulator, call, call.getArguments()[0]);

			/* adjust the emulator's stack size manually as needed; if this is impossible throw an EmulationException */
			if (call.getArguments()[0] instanceof NASMGeneralRegister
					&& ((NASMGeneralRegister) call.getArguments()[0]).getFullRegister() == RSP)
				if (call.getArguments()[1] instanceof NASMGeneralRegister
						&& ((NASMGeneralRegister) call.getArguments()[1])
								.getFullRegister() == RBP)
					emulator.resetLocalStack();
				else
					throw new IllegalStackManipulationException(emulator, call,
							"Someone tried to overwrite the stack pointer to something other than the base pointer! This is not legal!");

			/* emulate the command */
			MemoryBlock destination_value = emulator.getState().get(call.getArguments()[0]);
			MemoryBlock new_value = emulator.getState().get(call.getArguments()[1]);

			if (new_value.hasFinalValue()) {
				destination_value.setFinalValue(new_value.getFinalValue());
				return true;
			} else {
				destination_value.removeFinalValue();
				return false;
			}
		}
	};
	public static final NASMCommand SUB = new NASMCommand("sub", 2, false) {
		@Override
		public boolean emulateOn(NASMEmulator emulator, FullyAppliedNASMCommandCall call)
				throws EmulationException {
			// make sure the destination is mutable
			if (!call.getArguments()[0].isMutable())
				throw new WritingImmutableException(emulator, call, call.getArguments()[0]);

			/* adjust the emulator's stack size manually as needed; if this is impossible throw an EmulationException */
			if (call.getArguments()[0] instanceof NASMGeneralRegister
					&& ((NASMGeneralRegister) call.getArguments()[0]).getFullRegister() == RSP)
				if (call.getArguments()[1] instanceof NASMInteger)
					emulator.addLocalStackSize(((NASMInteger) call.getArguments()[1]).getValue(),
							call);
				else if (emulator.getState().get(call.getArguments()[1]).hasFinalValue())
					emulator.addLocalStackSize(emulator.getState().get(call.getArguments()[1])
							.getFinalValue().intValue(), call);
				else
					throw new IllegalStackManipulationException(emulator, call,
							"In this first alpha of the \\English compiler, the stack can only be incremented by constant values!");

			/* emulate the command */
			MemoryBlock destination_value = emulator.getState().get(call.getArguments()[0]);
			MemoryBlock augend_value = emulator.getState().get(call.getArguments()[1]);

			if (destination_value.hasFinalValue() && augend_value.hasFinalValue()) {
				destination_value.getFinalValue().subtract(augend_value.getFinalValue());
				return true;
			} else {
				destination_value.removeFinalValue();
				return false;
			}
		}
	};
	public static final NASMCommand RESB = new NASMCommand("resb", 1, true) {

		@Override
		public boolean emulateOn(NASMEmulator emulator, FullyAppliedNASMCommandCall call)
				throws EmulationException {
			if (!(call.getArguments()[0] instanceof NASMInteger))
				throw new WrongArgTypeException(emulator, call, 0, NASMInteger.class);

			emulator.getStaticMemory().add(new StaticMemoryBlock(emulator,
					((NASMInteger) call.getArguments()[0]).getValue()));
			return true;
		}
	};

	private final String command;
	private final byte number_of_args;
	private boolean is_data;

	private static String formulateSyntax(String command, byte number_of_args) {
		StringBuilder arguments = new StringBuilder();
		if (number_of_args > 1) {
			arguments.append("<NASM value Arg1>");
			for (int i = 1; i < number_of_args; i++) {
				arguments.append(", <NASM value Arg" + (i + 1) + ">");
			}
		}

		return command + " " + arguments;
	}

	NASMCommand(String command, int number_of_args, boolean is_data) {
		super(StandardOp.NASM_COMMAND, formulateSyntax(command, (byte) number_of_args));
		this.command = command;
		this.number_of_args = (byte) number_of_args;
		this.is_data = is_data;
	}

	public String getCommand() {
		return command;
	}

	public byte getNumberOfArguments() {
		return number_of_args;
	}

	@Override
	public Assemblage assembleWith(FullyAppliedOpCall call) throws OpRunException {
		List<FullyAppliedNASMCommandCall> lines = new ArrayList<>();
		NASMArg[] arguments = new NASMArg[number_of_args];
		for (int i = 0; i < number_of_args; i++) {
			Assemblage argument_assemblage = call.getArgument("Arg" + (i + 1)).checkFull(call).run()
					.assembleWith(call);
			lines.addAll(Arrays.asList(argument_assemblage.getLines()));
			arguments[i] = argument_assemblage.getReturnValue();
		}

		lines.add(new FullyAppliedNASMCommandCall(this, call.getLocation(), call.getRecallEntry(), arguments));

		return new Assemblage(lines.toArray(new FullyAppliedNASMCommandCall[lines.size()]));
	}

	/** This method attempts to emulate this {@link NASMCommand} using the information contained in the given <b>
	 * <tt>call</b></tt> on <b><tt>emulator</b></tt>.
	 * 
	 * @return <b>true</b> if this {@link NASMCommand} was successfully emulated on the given {@link NASMEmulator};
	 *         <b>false</b> otherwise. */
	public abstract boolean emulateOn(NASMEmulator emulator, FullyAppliedNASMCommandCall call)
			throws EmulationException;

	@Override
	public NASMLabel runWith(FullyAppliedOpCall call) throws OpRunException {
		try {
			boolean successfully_emulated = emulateOn(BEngCompiler.emulator,
					(FullyAppliedNASMCommandCall) call);
			if (!successfully_emulated)
				return BEngCompiler.output.outputAndLabel((FullyAppliedNASMCommandCall) call);
			else
				/* TODO: what the hell do I do here?! What do I return from a NASM command call that the 
				 * emulator removed from the equation? A label to the next command? */
				return null;
		} catch (EmulationException exception) {
			throw new RuntimeException(exception);
		}
	}


	public boolean isData() {
		return is_data;
	}
}
