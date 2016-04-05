package com.beng.op;

import static com.beng.emulator.NASMCommand.ADD;
import static com.beng.emulator.NASMCommand.MOV;
import static com.beng.emulator.args.NASMGeneralRegister.R8;
import static com.beng.emulator.args.NASMGeneralRegister.R9;
import static com.beng.emulator.args.NASMGeneralRegister.RCX;
import static com.beng.emulator.args.NASMGeneralRegister.RDI;
import static com.beng.emulator.args.NASMGeneralRegister.RDX;
import static com.beng.emulator.args.NASMGeneralRegister.RSI;
import static com.beng.emulator.args.NASMGeneralRegister.RSP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.beng.BEngCompiler;
import com.beng.SourceFileLocation;
import com.beng.emulator.FullyAppliedNASMCommandCall;
import com.beng.emulator.NASMStackRelativeEffectiveAddress;
import com.beng.emulator.args.NASMArg;
import com.beng.emulator.args.NASMEffectiveAddress;
import com.beng.emulator.args.NASMGeneralRegister;
import com.beng.emulator.args.NASMInteger;
import com.beng.emulator.exceptions.EmulationException;
import com.beng.op.args.OpArg;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCallException;
import com.beng.op.call.PartiallyAppliedOpCall;
import com.beng.opsyntax.OpSyntax;

public class CodedOpDefinition extends OpDefinition implements CodedOp, Assemblable {
	private final SourceFileLocation location;
	private final FullyAppliedOpCall body;
	private final PartiallyAppliedOpCall modifier;
	private final FullyAppliedOpCall modified_body;

	public CodedOpDefinition(SourceFileLocation location, FullyAppliedOpCall return_type, OpSyntax syntax,
			FullyAppliedOpCall body, PartiallyAppliedOpCall modifier) {
		super(return_type, syntax);

		this.location = location;
		this.modifier = modifier;
		// note: the body should be parsed contextually before it gets here
		this.body = body;

		try {
			modified_body = getSyntax().callWith(Arrays.asList(body), modifier, location).checkFull(null);
		} catch (OpRunException | OpCallException exception) {
			throw new RuntimeException("I seem to have gotten a malformed modifier!");
		}
	}

	private static class ArgumentStack {
		static final NASMGeneralRegister[] GENERAL_REGISTER_ARGUMENT_ORDER_CONVENTION = new NASMGeneralRegister[] {
				RDI, RSI, RDX, RCX, R8, R9 };
		private byte register_counter = 0;

		ArgumentStack() {}

		public Assemblage next() {
			if (register_counter < GENERAL_REGISTER_ARGUMENT_ORDER_CONVENTION.length) {
				register_counter++;
				return new Assemblage(GENERAL_REGISTER_ARGUMENT_ORDER_CONVENTION[register_counter - 1]);
			} else {
				return new Assemblage(new NASMStackRelativeEffectiveAddress(8, 8),
						new FullyAppliedNASMCommandCall(ADD, null, null, RSP,
								new NASMInteger(-8)));
			}
		}
	}

	// TODO: issues in this method!
	@Override
	public Assemblage assembleWith(FullyAppliedOpCall call) throws OpRunException {
		List<FullyAppliedNASMCommandCall> lines = new ArrayList<>();
		ArgumentStack argument_stacker = new ArgumentStack();
		for (Iterator<OpArg> iterator = call.getRecallEntry().getArguments(); iterator.hasNext();) {
			FullyAppliedOpCall argument = ((OpArg) iterator.next()).checkFull(call);

			/* TODO: if the argument is something not assemblable, i.e. a SysOp, just throw an error for now; figure this out later */
			if (!(argument.getDefinition() instanceof Assemblable))
				throw new RuntimeException(
						"Someone attempted to use something not assemblable in the system!");

			// first, assemble the argument itself in order to have the value of the argument to use
			Assemblage argument_assemblage = ((Assemblable) argument.getDefinition()).assembleWith(call);
			lines.addAll(Arrays.asList(argument_assemblage.getLines()));

			/* then, assemble code to move the argument into the correct place to pass it into the function according to function calling conventions */
			Assemblage argument_movement = argument_stacker.next();
			lines.addAll(Arrays.asList(argument_movement.getLines()));
			// TODO: using null location and recall breaks the CodedOp paradigm!
			// TODO: register clobbering could completely ruin this
			lines.add(new FullyAppliedNASMCommandCall(MOV, null, null, argument_movement.getReturnValue(),
					argument_assemblage.getReturnValue()));
		}

		if (!(body.getDefinition() instanceof Assemblable))
			throw new RuntimeException("Someone attempted to use something not assemblable in the system!");

		Assemblage body_assemblage = ((Assemblable) body.getDefinition()).assembleWith(call);

		lines.addAll(Arrays.asList(body_assemblage.getLines()));
		return new Assemblage(body_assemblage.getReturnValue(),
				lines.toArray(new FullyAppliedNASMCommandCall[lines.size()]));
	}

	@Override
	public SourceFileLocation getLocation() {
		return location;
	}

	public FullyAppliedOpCall getBody() {
		return body;
	}

	@Override
	public NASMArg runWith(FullyAppliedOpCall call) throws OpRunException {
		return BEngCompiler.emulator.emulateOrOutput(assembleWith(call), BEngCompiler.output);
	}
}
