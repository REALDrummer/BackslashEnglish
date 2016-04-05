package com.beng.opsyntax.quantifiable.escaped;

import com.beng.opsyntax.quantifiable.QuantifiableOpSyntax;
import com.beng.parser.Parser;

public class OpWordCharSyntax extends OpEscapedCharacterSyntax implements QuantifiableOpSyntax {
	public OpWordCharSyntax(Parser input) {
		super(input, 'w', "word character");
	}

	public static boolean isWordCharacter(char character) {
		return Character.isLetterOrDigit(character) || character == '_';
	}

	@Override
	public boolean canMatchNothing() {
		return false;
	}

}
