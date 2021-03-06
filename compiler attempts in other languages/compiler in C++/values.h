/*
 * values.h
 *
 *  Created on: Nov 27, 2015
 *      Author: connor
 */

#ifndef VALUES_H_
#define VALUES_H_

using std::string;

#define NUMBER_OF_VALUE_TYPES 3
typedef enum value_type {
	REGISTER, LABEL, LITERAL
} value_type;

class value {
public:
	virtual ~value() = 0;

	virtual value_type getType() = 0;

	/* returns a string describing the value, e.g. as the Assembly label identifier or a literal number value */
	virtual string toString() = 0;
};

class literal : public value {
private:
	int value;

public:
	value_type getType() {
		return LITERAL;
	}

	virtual string toString() {
		return std::to_string(value);
	}
};

class label : public value {
private:
	string identifier;

public:
	label(string identifier) {
		if (!isValidIdentifier(identifier))
			throw("\"" + identifier + " is not a valid identifier!");

		this->identifier = identifier;
	}

	static bool isValidIdentifier(string identifier) {
		bool skipped_dots = false;
		bool found_letter_to_start = false;
		const char* iter = identifier.c_str();
		for (unsigned int i = 0; i < identifier.length(); ++i, ++iter) {
			char character = *iter;
			if ((character < 'A' || character > 'Z') && character != '_' && (character != '.' || skipped_dots)
					&& (character > '0' || character < '9' || !found_letter_to_start))
				return false;
			else if (character != '.') {
				skipped_dots = true;
				if (character >= 'A' && character <= 'Z')
					found_letter_to_start = true;
			}
		}

		return true;
	}

	string toString() {
		return identifier + ":";
	}
};

typedef enum register_value {
	RAX, RBX, RCX, RDX, 		// 0-3; 64-bit
	EAX, EBX, ECX, EDX,			// 4-7; 32-bit
	AX, BX, CX, DX,				// 8-11; 16-bit
	AH, BH, CH, DH,				// 12-15; high 8-bit
	AL, BL, CL, DL,				// 16-19; low 8-bit
	CS, DS, ES, FS, GS, SS,		// 20-25; 16-bit; segment
	RDI, RSI, RSP, RBP, RIP,	// 26-30; 64-bit; special
	EDI, ESI, ESP, EBP, EIP, 	// 31-35; 32-bit; special
	DI, SI, SP, BP, IP,			// 36-40; 16-bit; special
	R8, R9, R10, R11, R12, R13, R14, R15,			// 41-48; 64-bit; x86_64 only
	R8D, R9D, R10D, R11D, R12D, R13D, R14D, R15D,	// 49-56; 32-bit; x86_64 only
	R8W, R9W, R10W, R11W, R12W, R13W, R14W, R15W,	// 57-64; 16-bit; x86_64 only
	R8B, R9B, R10B, R11B, R12B, R13B, R14B, R15B	// 65-72; 8-bit; x86_64 only
} register_value;
#define NUMBER_OF_ASM_REGISTER_VALUES R15B
const string asm_register_value_strings[] = { "rax", "rbx", "rcx", "rdx", "eax", "ebx", "ecx", "edx", "ax", "bx", "cx", "dx", "ah", "bh", "ch", "dh", "al",
		"bl", "cl", "dl", "cs", "ds", "es", "fs", "gs", "ss", "rdi", "rsi", "rsp", "rbp", "rip", "edi", "esi", "esp", "ebp", "eip", "di", "si", "sp", "bp",
		"ip", "r8", "r9", "r10", "r11", "r12", "r13", "r14", "r15", "r8d", "r9d", "r10d", "r11d", "r12d", "r13d", "r14d", "r15d", "r9w", "r10w", "r11w", "r12w",
		"r13w", "r14w", "r15w", "r9b", "r10b", "r11b", "r12b", "r13b", "r14b", "r15b" };

class asm_register : public value {
	register_value value;

public:
	asm_register(register_value value) {
		this->value = value;
	}

	register_value getFullRegister() {
		if (value <= DL)
			return static_cast<register_value>(value % 4);
		else if (value >= RDI && value <= IP)
			return static_cast<register_value>((value - RDI) % 5 + RDI);
		else if (value >= R8)
			return static_cast<register_value>((value - R8) % 8 + R8);
		else
			return value;
	}

	unsigned char isGeneralPurpose() {
		return value <= DL || value >= R8;
	}

	bool isInx86() {
		return value <= IP;
	}

	unsigned char sizeInBits() {
		if (value <= DH)
			return (4 - value / 4) * 8;
		else if (value <= DL)
			return 8;
		else if (value <= SS)
			return 16;
		else if (value <= IP)
			return (3 - (value - RDI) / 5) * 8 + 8;
		else
			return (4 - (value - R8) / 8) * 8;
	}

	bool isMutable() {
		// all registers are mutable except for the `rip` register (and consequently `eip` and `ip`)
		return value != RIP && value != EIP && value != IP;
	}

	value_type getType() {
		return REGISTER;
	}

	register_value* fromString(string register_name) {
		for (unsigned int i = 0; i < NUMBER_OF_ASM_REGISTER_VALUES; i++)
			if (register_name.compare(asm_register_value_strings[i]) == 0) {
				register_value* value = (register_value*) malloc(sizeof(register_value));
				*value = static_cast<register_value>(i);
				return value;
			}
		return nullptr;
	}

	string toString() {
		return asm_register_value_strings[value];
	}
};

/* I made this class thinking it may be useful, and I think it will be useful in the future for some really powerful optimizations with the address
 * calculation ALU. However, with this first version of the compiler, it will probably be too complicated. Still, I'm leaving the work on this
 * class so far here in case it may be useful.

 class effective_address {
 private:
 /* effective addressing in NASM comes in the following form:
 * [ [base] + [index]*[scale] + [offset] ]
 * where the index, scale, and offset are optional * /
 asm_register base;
 asm_register index;			// defaults to EAX
 unsigned char index_scale;	// defaults to 1 or 0 if index is -1 (not given)
 unsigned int offset;		// defaults to 0

 public:
 effective_address(asm_register base, asm_register index, unsigned char index_scale, unsigned int offset) {
 this->base = base;
 this->index = index;
 this->index_scale = index_scale;
 this->offset = offset;
 }

 effective_address(asm_register base, asm_register index, unsigned char index_scale) :
 effective_address(base, index, index_scale, 0) {
 }

 effective_address(asm_register base, unsigned int offset) :
 effective_address(base, *new asm_register(EAX /* placeholder * /), 0, offset) {
 }

 effective_address(asm_register base) :
 effective_address(base, *new asm_register(EAX /* placeholder * /), 0, 0) {
 }
 }; */

#endif /* VALUES_H_ */
