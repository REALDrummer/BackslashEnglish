package com.beng.parser;

import java.util.ListIterator;
import java.util.Stack;

import com.beng.op.Op;
import com.beng.op.SysOp;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.parseexceptions.OpParseException;

public class OpContext implements OpsEntry {
	// in the future, it may be worth it to create a type tree to find ops by type more quickly
	private Stack<OpsEntry> ops_by_order = new Stack<>();

	@Override
	public Op parse(Parser input, FullyAppliedOpCall type) throws OpParseException {
		// first, try to parse the given operators
		OpParseException closest_match_exception = null;
		ListIterator<OpsEntry> ops_stack_iterator = ops_by_order.listIterator(ops_by_order.size());
		while (ops_stack_iterator.hasPrevious()) {
			try {
				return ops_stack_iterator.previous().parse(input, type);
			} catch (OpParseException exception) {
				// in the future, maybe make exception handling more advanced in
				if (closest_match_exception == null || closest_match_exception
						.getMatchPriority() < exception.getMatchPriority())
					closest_match_exception = exception;
			}
		}

		// next, if we couldn't find a parsable entry, check against the sysops
		// note that order doesn't matter among the sysops
		for (int i = SysOp.SYS_OPS.size(); i >= 0; i--) {
			try {
				return SysOp.SYS_OPS.get(i).parse(input, type);
			} catch (OpParseException exception) {
				// in the future, maybe make exception handling more advanced in
				if (closest_match_exception == null || closest_match_exception
						.getMatchPriority() < exception.getMatchPriority())
					closest_match_exception = exception;
			}
		}

		/* this `if-else-null` statement below is unnecessary; by the logic of the for loops, if the code execution 
		 * reaches this point, there must have been (many) exceptions, so there will be a closest match exception; 
		 * however, I added the `if` for the sake of the IDE because the flow control is too complicated for the 
		 * IDE to understand and check properly */
		if (closest_match_exception != null)
			throw closest_match_exception;
		else
			return null;
	}
}
