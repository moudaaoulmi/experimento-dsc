package ish.ecletex.xml;

/**
 * Represent an XML-comment.
 */

public class Comment {
    private String text;
    /**
     * Returns the text of this comment.
     */
    public String getText() { return text; }

    /**
     * Returns the data of this data section.
     */
    public String toString() { return "<!--"+text+"-->"; }
    /**
     * Constructs a new data comment containing the given data.
     */
    public Comment (String text) {
	this.text = text;
    }
}
