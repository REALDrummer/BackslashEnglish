package com.beng.emulator.args;

import java.util.Arrays;

import com.beng.op.Op;
import com.beng.op.OpRunException;
import com.beng.op.StandardOp;
import com.beng.op.call.FullyAppliedOpCall;
import com.beng.op.call.OpCallException;

public class NASMEffectiveAddress implements NASMArg {
	private FullyAppliedOpCall address;
	private short size_in_bytes;
	
	public NASMEffectiveAddress(int size_in_bytes, FullyAppliedOpCall address) {
		this.address = address;
		this.size_in_bytes = (short) size_in_bytes;

	}

	public NASMEffectiveAddress(String size_specifier, FullyAppliedOpCall address) {
		this(sizeFromSpecifier(size_specifier), address);

		// if the size specifier was invalid, throw an error
		if (size_in_bytes == -1 && size_specifier != null)
			throw new RuntimeException("\"" + size_specifier
					+ "\" is not a valid size specifier for an effective address!");
	}

	private static int sizeFromSpecifier(String size_specifier) {
		if (size_specifier == null)
			return -1;
		else
			switch (size_specifier) {
			case "byte":
				return 1;
			case "word":
				return 2;
			case "dword":
				return 4;
			case "qword":
				return 8;
			case "tword":
				return 10;
			case "oword":
				return 16;
			case "yword":
				return 32;
			case "zword":
				return 64;
			default:
				return -1;
			}
	}

	public FullyAppliedOpCall getAddress() {
		return address;
	}

	@Override
	public FullyAppliedOpCall getReturnType() {
		return StandardOp.EFFECTIVE_ADDRESS;
	}
	
	@Override
	public short getSizeInBits() {
		return (short) (size_in_bytes * 8);
	}

	public short getSizeInBytes() {
		return size_in_bytes;
	}

	public String getSizeModifier() {
		switch (size_in_bytes) {
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

	public void setSize(short size_in_bytes) {
		this.size_in_bytes = size_in_bytes;
	}

	@Override
	public String toString() {
		String size_modifier = getSizeModifier();
		return (size_modifier == null ? "" : size_modifier + " ") + "[" + address.toString() + "]";
	}
}
