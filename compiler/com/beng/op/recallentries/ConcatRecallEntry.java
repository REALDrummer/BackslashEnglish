package com.beng.op.recallentries;

import java.util.Iterator;

import com.beng.op.args.OpArg;

public class ConcatRecallEntry implements RecallEntry {
	private final RecallEntry[] group_entries;

	public ConcatRecallEntry(RecallEntry... entries) {
		group_entries = entries;
	}

	@Override
	public OpArg getArgument(String name) {
		for (RecallEntry group_entry : group_entries) {
			OpArg argument = group_entry.getArgument(name);
			if (argument != null)
				return argument;
		}

		return null;
	}

	public static class ConcatRecallEntryArgIterator implements Iterator<OpArg> {
		private final ConcatRecallEntry recall;
		private int current_group_entry_index = 0;
		private Iterator<OpArg> current_iterator = null;

		private boolean checked_for_next = false;
		private OpArg next = null;

		public ConcatRecallEntryArgIterator(ConcatRecallEntry recall) {
			this.recall = recall;
		}

		@Override
		public boolean hasNext() {
			if (!checked_for_next) {
				next = next();
				checked_for_next = true;
			}

			return next != null;
		}

		@Override
		public OpArg next() {
			if (!checked_for_next) {
				if (current_iterator.hasNext()) {
					next = current_iterator.next();
				} else {
					while (current_group_entry_index < recall.getGroupEntries().length) {
						current_group_entry_index++;
						current_iterator = recall.getGroupEntries()[current_group_entry_index]
								.getArguments();
						if (current_iterator.hasNext()) {
							next = current_iterator.next();
							break;
						}
					}
				}
			}

			return next;
		}
	}

	@Override
	public Iterator<OpArg> getArguments() {
		return new ConcatRecallEntryArgIterator(this);
	}

	@Override
	public String getCapturedLiteral(String name) {
		for (RecallEntry group_entry : group_entries) {
			String literal = group_entry.getCapturedLiteral(name);
			if (literal != null)
				return literal;
		}

		return null;
	}

	public static class ConcatRecallEntryCapturedLiteralIterator implements Iterator<String> {
		private final ConcatRecallEntry recall;
		private int current_group_entry_index = 0;
		private Iterator<String> current_iterator = null;

		private boolean checked_for_next = false;
		private String next = null;

		public ConcatRecallEntryCapturedLiteralIterator(ConcatRecallEntry recall) {
			this.recall = recall;
		}

		@Override
		public boolean hasNext() {
			if (!checked_for_next) {
				next = next();
				checked_for_next = true;
			}

			return next != null;
		}

		@Override
		public String next() {
			if (!checked_for_next) {
				if (current_iterator.hasNext()) {
					next = current_iterator.next();
				} else {
					while (current_group_entry_index < recall.getGroupEntries().length) {
						current_group_entry_index++;
						current_iterator = recall.getGroupEntries()[current_group_entry_index]
								.getCapturedLiterals();
						if (current_iterator.hasNext()) {
							next = current_iterator.next();
							break;
						}
					}
				}
			}

			return next;
		}
	}

	@Override
	public Iterator<String> getCapturedLiterals() {
		return new ConcatRecallEntryCapturedLiteralIterator(this);
	}

	public RecallEntry[] getGroupEntries() {
		return group_entries;
	}
}
