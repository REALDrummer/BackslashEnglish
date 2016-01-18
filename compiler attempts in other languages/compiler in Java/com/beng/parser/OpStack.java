package com.beng.parser;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

import com.beng.op.Op;
import com.beng.op.SysOp;
import com.beng.op.parseexceptions.NoMatchException;
import com.beng.op.parseexceptions.OpParseException;
import com.beng.op.parseexceptions.TypeMismatchException;

public class OpStack implements OpsEntry {
	// in the future, it may be worth it to create a type tree to find ops by type more quickly
	private Stack<OpsEntry> ops_by_order = new Stack<>();

	@Override
	public Op parse(Parser input, Op type) throws OpParseException {
		// first, try to parse the given operators
		OpParseException closest_match_exception = null;
		ListIterator<OpsEntry> ops_stack_iterator = ops_by_order.listIterator(ops_by_order.size());
		while (ops_stack_iterator.hasPrevious()) {
			try {
				return ops_stack_iterator.previous().parse(input, type);
			} catch (OpParseException exception) {
				// in the future, maybe make exception handling more advanced in
				if (closest_match_exception == null
						|| closest_match_exception instanceof NoMatchException
								&& exception instanceof TypeMismatchException)
					closest_match_exception = exception;
			}
		}

		// next, if we couldn't find a parsable entry, check against the sysops
		// note that order doesn't matter among the sysops
		for (SysOp sys_op : SysOp.SYS_OPS) {
			try {
				return sys_op.parse(input, type);
			} catch (OpParseException exception) {
				// in the future, maybe make exception handling more advanced in
				if (closest_match_exception == null
						|| closest_match_exception instanceof NoMatchException
								&& exception instanceof TypeMismatchException)
					closest_match_exception = exception;
			}
		}
	}
}
