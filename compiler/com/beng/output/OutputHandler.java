package com.beng.output;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.beng.emulator.FullyAppliedNASMCommandCall;
import com.beng.emulator.args.NASMLabel;

public class OutputHandler {
	private static Set<OutputHandler> output_handlers = new HashSet<>();

	private File output_file;

	private StringBuilder rodata_section = new StringBuilder();
	private StringBuilder text_section = new StringBuilder();

	private int label_counter = 0;

	private OutputHandler(File output_file) {
		this.output_file = output_file;

		output_handlers.add(this);
	}

	private OutputHandler(String output_file_path) {
		this(new File(output_file_path));
	}

	public static OutputHandler getOutputHandler(File output_file) {
		// if the file already exists, try to determine whether or not there is already an OutputHandler for it
		if (output_file.exists()) {
			OutputHandler existing_handler = null;
			for (OutputHandler handler : output_handlers) {
				if (handler.getOutputFile().equals(output_file)) {
					existing_handler = handler;
					break;
				}
			}
			// if there is an existing handler, return that handler
			if (existing_handler != null)
				return existing_handler;
		}

		return new OutputHandler(output_file);
	}

	public static OutputHandler getOutputHandler(String output_file_path) {
		return getOutputHandler(new File(output_file_path));
	}

	public File getOutputFile() {
		return output_file;
	}

	private void output(FullyAppliedNASMCommandCall command, boolean add_whitespace) {
		StringBuilder target_section;
		if (command.getDefinition().isData())
			target_section = rodata_section;
		else
			target_section = text_section;

		if (add_whitespace) {
			if (target_section.length() != 0)
				target_section.append("\n");
			target_section.append("\t");
		}

		target_section.append(command.getDefinition().getCommand());
		if (command.getArguments().length > 0) {
			target_section.append(" ");
			target_section.append(command.getArguments()[0]);
			if (command.getArguments().length > 1)
				for (int i = 1; i < command.getArguments().length; i++) {
					target_section.append(", " + command.getArguments()[i].toString());
				}
		}
	}

	public void output(FullyAppliedNASMCommandCall command) {
		output(command, true);
	}

	public NASMLabel nextLabel() {
		label_counter++;
		return new NASMLabel("label" + (label_counter - 1) + ": ");
	}

	public NASMLabel outputAndLabel(FullyAppliedNASMCommandCall command) {
		// for now, assume all NASM commands belong in the text section

		// add whitespace before the command and label as necessary
		if (text_section.length() != 0)
			text_section.append("\n");

		// make a new label
		NASMLabel label = nextLabel();

		// output the new label
		text_section.append("\t" + label.getLabel() + ": ");

		// output the rest of the command
		output(command, false);

		return label;
	}
}
