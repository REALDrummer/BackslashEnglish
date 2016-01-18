package com.beng.emulator.args;

import com.beng.op.Assemblage;
import com.beng.op.Op;
import com.beng.op.StandardOp;
import com.beng.opsyntax.OperatorDefinitionErrorType;
import com.beng.parser.Parser;

public class NASMEffectiveAddress implements NASMArg {
	private Op address;
	private short size;

	public NASMEffectiveAddress(Parser parser) {
		if (parser.parse("byte"))
			size = 8;
		else if (parser.parse("word"))
			size = 16;
		else if (parser.parse("dword"))
			size = 32;
		else
			size = -1;

		parser.parseOrErr("[", "opening bracket of an effective address");

		address = parser.parseOp(StandardOp.NUMBER);

		parser.parseOrErr("]", "closing bracket of an effective address");
	}

	@Override
	public Assemblage assemble() {
		return new Assemblage(this);
	}

	@Override
	public short getSize() {
		return size;
	}

	public String getSizeModifier() {
		switch (size) {
		case 8:
			return "byte";
		case 16:
			return "word";
		case 32:
			return "dword";
		default:
			return null;
		}
	}

	public void setSize(short size) {
		this.size = size;
	}

	@Override
	public String toString() {
		String size_modifier = getSizeModifier();
		return (size_modifier == null ? "" : size_modifier + " ") + "[" + address.toString() + "]";
	}
}
