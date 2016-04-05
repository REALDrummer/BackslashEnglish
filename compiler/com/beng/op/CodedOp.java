package com.beng.op;

import com.beng.SourceFileLocation;
import com.beng.op.call.OpCall;

/** @author connor
 *
 *         This interface represents an {@link Op} that has a {@link SourceFileLocation location} in the program code
 *         such as a {@link CodedOpDefinition} or an {@link OpCall}. */
public interface CodedOp extends Op {
	public SourceFileLocation getLocation();
}
