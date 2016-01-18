package com.beng.emulator.args;

import com.beng.op.Assemblage;

public enum NASMGeneralRegister implements NASMArg {
	RAX, RBX, RCX, RDX, // 0-3; 64-bit
	EAX, EBX, ECX, EDX, // 4-7; 32-bit
	AX, BX, CX, DX, // 8-11; 16-bit
	AH, BH, CH, DH, // 12-15; high 8-bit
	AL, BL, CL, DL, // 16-19; low 8-bit
	CS, DS, ES, FS, GS, SS, // 20-25; 16-bit; segment
	RDI, RSI, RSP, RBP, RIP, // 26-30; 64-bit; special
	EDI, ESI, ESP, EBP, EIP, // 31-35; 32-bit; special
	DI, SI, SP, BP, IP, // 36-40; 16-bit; special
	R8, R9, R10, R11, R12, R13, R14, R15, // 41-48; 64-bit; x86_64 only
	R8D, R9D, R10D, R11D, R12D, R13D, R14D, R15D, // 49-56; 32-bit; x86_64 only
	R8W, R9W, R10W, R11W, R12W, R13W, R14W, R15W, // 57-64; 16-bit; x86_64 only
	R8B, R9B, R10B, R11B, R12B, R13B, R14B, R15B; // 65-72; 8-bit; x86_64 only

	public final short getSize() {
		if (ordinal() <= DH.ordinal())
			return (short) ((4 - ordinal() / 4) * 8);
		else if ((ordinal() <= DL.ordinal()))
			return 8;
		else if (ordinal() <= SS.ordinal())
			return 16;
		else if (ordinal() <= IP.ordinal())
			return (short) ((3 - (ordinal() - RDI.ordinal()) / 5) * 8 + 8);
		else
			return (short) ((4 - (ordinal() - R8.ordinal()) / 8) * 8);
	}

	@Override
	public Assemblage assemble() {
		return new Assemblage(this);
	}

	NASMGeneralRegister getFullRegister() {
		if (ordinal() <= DL.ordinal())
			return values()[ordinal() % 4];
		else if (ordinal() >= RDI.ordinal() && ordinal() <= IP.ordinal())
			return values()[(ordinal() - RDI.ordinal()) % 5 + RDI.ordinal()];
		else if (ordinal() >= R8.ordinal())
			return values()[(ordinal() - R8.ordinal()) % 8 + R8.ordinal()];
		else
			return this;
	}

	boolean isGeneralPurpose() {
		return ordinal() <= DL.ordinal() || ordinal() >= R8.ordinal();
	}

	boolean isInx86() {
		return ordinal() <= IP.ordinal();
	}

	boolean isMutable() {
		// all registers are mutable except for the `rip` register (and consequently `eip` and `ip`)
		return (ordinal() - RIP.ordinal()) % 5 == 0;
	}

	@Override
	public final String toString() {
		return name().toLowerCase();
	}

}
