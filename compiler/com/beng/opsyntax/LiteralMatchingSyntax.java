package com.beng.opsyntax;

import java.util.List;

import com.beng.SourceFileLocation;
import com.beng.op.OpDefinition;
import com.beng.op.args.GivenOpArg;
import com.beng.op.call.OpCall;
import com.beng.op.call.OpCallException;
import com.beng.op.call.PartiallyAppliedOpCall;
import com.beng.op.recallentries.RecallEntry;
import com.beng.op.types.OpType;

public interface LiteralMatchingSyntax extends OpSyntax {
	@Override
	default OpCall callWith(List<GivenOpArg> arguments, RecallEntry previous_arguments, OpDefinition definition,
			SourceFileLocation location) throws OpCallException {
		/* literal matching syntaxes take no arguments by definition, so if we get arguments here, throw an error! */
		if (arguments.size() > 0)
			throw new OpCallException("someone tried to pass arguments to a nullary op!", location);

		/* do nothing and return basically what we had before */
		return new PartiallyAppliedOpCall(definition, location, previous_arguments);
	}

	@Override
	default RecallEntry callWithSubset(List<GivenOpArg> arguments, RecallEntry previous_arguments)
			throws OpCallException {
		// by default, do nothing!
		/* in the other callWith for LiteralMatchingSyntaxes, we threw an error if there were no arguments, but unlike in that method, this 
		 * method is to be used when this OpSyntax is just part of a larger one. One of the most important parts of that is not throwing
		 * errors in this case where arguments are passed over a literal matcher; they should just be ignored */
		return previous_arguments;
	}

	@Override
	default boolean matchesArgs(OpType type) {
		/* literal matching syntaxes (by themselves) are nullary, so they only match OpType args if there are no arguments! */
		return type.getTypeArgTypes().length == 0;
	}
}
