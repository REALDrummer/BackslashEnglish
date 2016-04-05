package com.beng.emulator;

import java.math.BigInteger;

import com.beng.op.Assemblable;

public abstract class MemoryBlock implements Assemblable {
	private final NASMEmulator emulator;
	
	private final int size_in_bytes;
	private BigInteger value = null;


	public MemoryBlock(NASMEmulator emulator, int size_in_bytes) {
		this.emulator = emulator;
		this.size_in_bytes = size_in_bytes;
	}
	
	public NASMEmulator getEmulator() {
		return emulator;
	}

	public boolean hasFinalValue() {
		return value != null;
	}

	public BigInteger getFinalValue() {
		return value;
	}

	public int getSize() {
		return size_in_bytes;
	}

	/** This method designates this {@link MemoryBlock} as non-final, removing the previous final value.
	 * 
	 * @return <b>true</b> if the value of this {@link MemoryBlock} was final before this method was called;
	 *         <b>false</b> otherwise. */
	public boolean removeFinalValue() {
		boolean result = value != null;
		value = null;
		return result;
	}

	public BigInteger setFinalValue(long new_value) {
		return value = BigInteger.valueOf(new_value);
	}

	public BigInteger setFinalValue(BigInteger new_value) {
		return value = new_value;
	}
}
