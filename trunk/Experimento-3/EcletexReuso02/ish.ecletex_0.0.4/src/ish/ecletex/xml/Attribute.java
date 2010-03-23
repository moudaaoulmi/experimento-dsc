package ish.ecletex.xml;

/**
 * An xml attribute.
 */

public class Attribute {
	private String name = "";
	private String value = "";

	/**
	 * Returns the value of this attribute.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the value of this attribute as an integer - may throw an error if
	 * value is not an integer.
	 */
	public int getIntValue() {
		return Integer.parseInt(value);
	}

	/**
	 * Returns the name of this attribute.
	 */
	public String getName() {
		return name;
	}

	Attribute(String name, String value) {
		this.name = name;
		this.value = value;
	}
}
