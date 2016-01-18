package com.beng;

public class Pair<T1, T2> {
	private T1 first_element;
	private T2 second_element;

	public Pair(T1 first_element, T2 second_element) {
		this.first_element = first_element;
		this.second_element = second_element;
	}

	public T1 getFirstElement() {
		return first_element;
	}

	public void setFirstElement(T1 first_element) {
		this.first_element = first_element;
	}

	public T2 getSecondElement() {
		return second_element;
	}

	public void setSecondElement(T2 second_element) {
		this.second_element = second_element;
	}
}
