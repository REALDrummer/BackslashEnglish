package com.beng.op.args;

import java.util.Arrays;

import com.beng.op.OpRunException;
import com.beng.op.StandardOp;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCallException;

/** @author connor
 *
 *         This class represents a op argument that is quantified by being contained inside a {@link QuantifiedOpSyntax}
 *         , e.g. <tt>*</tt> or <tt>{2}</tt>. */
public class QuantifiedOpArg implements OpArg {
	private final OpArg[] values_given;
	private final FullyAppliedOpCall element_type;

	public QuantifiedOpArg(FullyAppliedOpCall element_type, OpArg... values_given) {
		this.values_given = values_given;
		this.element_type = element_type;
	}

	@Override
	public FullyAppliedOpCall getType() {
		try {
			return StandardOp.LIST.callWith(Arrays.asList(element_type), null).checkFull(null);
		} catch (OpRunException | OpCallException exception) {
			throw new RuntimeException(
					"There was a problem trying to construct the list of element types in returning the type of a quantified op argument.",
					exception);
		}
	}

	public OpArg[] getValuesGiven() {
		return values_given;
	}
}
