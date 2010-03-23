/*
 * Created on 13-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ish.ecletex.builders.latexlogparser;

import java.io.File;
import java.io.FileInputStream;
import java.util.Stack;

/**
 * @author ish
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class Parser {
	class Lexer {

		void pushbackToken() {
			pos -= token.length();
		}

		void nextToken() {
			for (; pos < buf.length && buf[pos] == 32 || buf[pos] == 9
					|| buf[pos] == 10; pos++)
				;
			int i = pos;
			for (; pos < buf.length && buf[pos] != 32 && buf[pos] != 9
					&& buf[pos] != 10 && buf[pos] != 40 && buf[pos] != 41
					&& buf[pos] != 91 && buf[pos] != 93 && buf[pos] != 123
					&& buf[pos] != 125; pos++)
				;
			if (pos < buf.length)
				if (pos - i == 0)
					token = new String(buf, pos++, 1);
				else if (buf[pos] == 40 || buf[pos] == 91 || buf[pos] == 41
						|| buf[pos] == 93)
					token = new String(buf, i, pos - i);
				else
					token = new String(buf, i, pos++ - i);
		}

		String seek(char c) {
			int i = pos;
			for (; pos < buf.length; pos++)
				if (buf[pos] == c) {
					String s = new String(buf, i, pos - i);
					pos++;
					nextToken();
					return s;
				}

			throw new Error(
					"Parser error! Did TeX/LaTeX complete its compilation?");
		}

		String seek(String s) {
			int j = buf.length - s.length();
			int k = pos;
			for (; pos < j; pos++) {
				int i;
				for (i = 0; i < s.length(); i++)
					if (buf[pos + i] != s.charAt(i))
						break;

				if (i == s.length()) {
					String s1 = new String(buf, k, (pos + i) - k);
					pos += i;
					nextToken();
					return s1;
				}
			}

			throw new Error(
					"Parser error! Did TeX/LaTeX complete its compilation?");
		}

		String crossLineSeek(String s) {
			int j = buf.length - s.length();
			int k = 0;
			int l = pos;
			for (; pos < j; pos++) {
				int i;
				for (i = 0; i < s.length(); i++) {
					if (buf[pos + i + k] == 10 || buf[pos + i + k] == 13) {
						i--;
						k++;
						continue;
					}
					if (buf[pos + i + k] != s.charAt(i))
						break;
				}

				if (i == s.length()) {
					String s1 = new String(buf, l, (pos + i + k) - l);
					pos += i + k;
					nextToken();
					s1 = strip(s1, '\n');
					s1 = strip(s1, '\r');
					return s1;
				}
			}

			throw new Error(
					"Parser error! Did TeX/LaTeX complete its compilation?");
		}

		Lexer() {
		}
	}

	String filename;

	public Parser(String filename) {
		infc = 0;
		prec = 0;
		filc = 0;
		pagc = 0;
		msgc = 0;
		lexer = new Lexer();
		this.filename = filename;
		// controller = controller1;
		model = new Model();
	}

	public Model getModel() {
		return model;
	}

	public void parse() {
		FileInputStream fileinputstream = new FileInputStream(filename);
		byte abyte0[] = new byte[fileinputstream.available()];
		fileinputstream.read(abyte0);
		fileinputstream.close();
		int i = 0;
		int j;
		for (j = 0; j < abyte0.length - 2;) {
			if (abyte0[j] == 13) {
				if (abyte0[j + 1] == 10)
					abyte0[i] = abyte0[++j];
				else
					abyte0[i] = 10;
			} else {
				abyte0[i] = abyte0[j];
			}
			j++;
			i++;
		}

		if (abyte0[j] == 13)
			abyte0[i] = 10;
		else
			abyte0[i] = abyte0[j];
		buf = new byte[i];
		for (int k = 0; k < i; k++)
			buf[k] = abyte0[k];

		logfilepath = (new File(filename)).getParent();
		pos = 0;
		filenames = new Stack();
		lexer.seek("**");
		for (; !token.equals("("); lexer.nextToken())
			;
		while (pre())
			;
	}

	private boolean pre() {
		if (token.equals("(")) {
			String s = "";
			do {
				lexer.nextToken();
				s = s + token + " ";
			} while (!token.equals(")")
					&& (s.length() <= 4 || s.charAt(s.length() - 5) != '.'));
			if (token.equals(")")) {
				lexer.pushbackToken();
				token = s.substring(0, s.length() - 3);
			} else {
				token = s.substring(0, s.length() - 1);
			}
			if (token.endsWith(".tex") || token.endsWith(".bbl"))
				file();
			else
				info();
			return true;
		} else {
			return false;
		}
	}

	private boolean file() {
		if (token.charAt(1) == ':' || token.charAt(0) == '/')
			filenames.push((new File(token)).getAbsolutePath());
		else
			filenames.push((new File(logfilepath, token)).getAbsolutePath());
		model.add(new Entry(100, (String) filenames.peek(), (String) filenames
				.peek(), 1));
		lexer.nextToken();
		while (!token.equals(")"))
			if (token.equals("("))
				pre();
			else if (token.equals("["))
				pageno();
			else
				msgs();
		lexer.nextToken();
		filenames.pop();
		internalFile();
		return true;
	}

	private void internalFile() {
		model.add(new Entry(100, (String) filenames.peek(), (String) filenames
				.peek(), 1));
	}

	private void info() {
		lexer.nextToken();
		while (!token.equals(")"))
			if (token.equals("(")) {
				lexer.nextToken();
				info();
			} else {
				lexer.nextToken();
			}
		lexer.nextToken();
	}

	private void msgs() {
		while (!token.equals("(") && !token.equals(")") && !token.endsWith(".")) {
			if (token.equals("!")) {
				StringBuffer stringbuffer = new StringBuffer();
				stringbuffer.append("! ");
				lexer.nextToken();
				stringbuffer.append(token);
				if (token.equals("Font")) {
					stringbuffer
							.append(" " + lexer.crossLineSeek("not found."));
					model.add(new Entry(0, stringbuffer.toString(),
							(String) filenames.peek()));
				} else {
					stringbuffer.append(" " + lexer.seek('\n'));
					model.add(new Entry(8, stringbuffer.toString(),
							(String) filenames.peek()));
				}
				return;
			}
			if (token.equals("Package")) {
				StringBuffer stringbuffer1 = new StringBuffer();
				stringbuffer1.append(token + " ");
				lexer.nextToken();
				stringbuffer1.append(token + " ");
				if (token.equals("FiXme")) {
					fixmeParse(stringbuffer1);
					return;
				}
				lexer.nextToken();
				if (token.equals("Warning:")) {
					stringbuffer1.append(token + " ");
					lexer.nextToken();
					if (token.equals("Citation")) {
						stringbuffer1.append(token + " "
								+ lexer.crossLineSeek("on input line"));
						stringbuffer1.append(token);
						int i = Integer.parseInt(token.substring(0, token
								.length() - 1));
						model.add(new Entry(7, stringbuffer1.toString(),
								(String) filenames.peek(), i));
						return;
					} else {
						System.out
								.println("found: " + stringbuffer1.toString());
						lexer.seek('\n');
						return;
					}
				} else {
					lexer.seek('\n');
					return;
				}
			}
			if (token.equals("Missing")) {
				StringBuffer stringbuffer2 = new StringBuffer();
				stringbuffer2.append(token + " ");
				lexer.nextToken();
				if (token.equals("character:")) {
					stringbuffer2.append(token + " ");
					String s = lexer.seek('\n');
					s = s.substring(0, s.length() - 1);
					stringbuffer2.append(s);
					model.addOnce(new Entry(0, stringbuffer2.toString(),
							(String) filenames.peek(), 1));
					return;
				} else {
					lexer.seek('\n');
					return;
				}
			}
			if (token.equals("No")) {
				StringBuffer stringbuffer3 = new StringBuffer();
				stringbuffer3.append(token + " ");
				lexer.nextToken();
				if (token.equals("file")) {
					stringbuffer3.append(token + " ");
					String s1 = lexer.seek('\n');
					s1 = s1.substring(0, s1.length() - 1);
					if (!s1.endsWith(".aux")) {
						stringbuffer3.append(s1);
						model.add(new Entry(6, stringbuffer3.toString(),
								(String) filenames.peek()));
					}
					return;
				} else {
					lexer.seek('\n');
					return;
				}
			}
			if (token.equals("Overfull")) {
				StringBuffer stringbuffer4 = new StringBuffer();
				stringbuffer4.append(token + " ");
				lexer.nextToken();
				if (token.equals("\\hbox")) {
					stringbuffer4.append("\\hbox ");
					lexer.nextToken();
					stringbuffer4.append("(");
					lexer.nextToken();
					int i2 = Integer.parseInt(token.substring(0, token
							.indexOf('.')));

					// /////////////////////Edited HERRRRRREEEEEEEE

					if (i2 >= 0) {
						stringbuffer4.append(token + " ");
						lexer.nextToken();
						stringbuffer4.append(lexer.crossLineSeek("line"));
						if (token.equals("s")) {
							stringbuffer4.append(token + " ");
							lexer.nextToken();
							int j = Integer.parseInt(token.substring(0, token
									.indexOf('-')));
							model.add(new Entry(3, stringbuffer4.toString()
									+ token, (String) filenames.peek(), j));
							lexer.seek("\n\n");
						} else {
							stringbuffer4.append(token);
							int k = Integer.parseInt(token);
							model.add(new Entry(6, stringbuffer4.toString(),
									(String) filenames.peek(), k));
						}
					} else {
						lexer.seek("\n\n");
						return;
					}
				} else {
					lexer.seek("\n\n");
					return;
				}
			} else if (token.equals("Underfull")) {
				StringBuffer stringbuffer5 = new StringBuffer();
				stringbuffer5.append(token + " ");
				lexer.nextToken();
				if (token.equals("\\hbox")) {
					stringbuffer5.append(token + " "
							+ lexer.crossLineSeek("line"));
					if (token.equals("s")) {
						stringbuffer5.append(token + " ");
						lexer.nextToken();
						int l = Integer.parseInt(token.substring(0, token
								.indexOf('-')));
						model.add(new Entry(2,
								stringbuffer5.toString() + token,
								(String) filenames.peek(), l));
						lexer.seek("\n\n");
					} else {
						stringbuffer5.append(token);
						int i1 = Integer.parseInt(token);
						model.add(new Entry(6, stringbuffer5.toString(),
								(String) filenames.peek(), i1));
					}
					return;
				}
				if (token.equals("\\vbox")) {
					stringbuffer5.append("\\vbox ");
					stringbuffer5.append(lexer.seek('\n'));
					model.add(new Entry(4, stringbuffer5.toString(),
							(String) filenames.peek(), 1));
				} else {
					lexer.seek("\n\n");
					return;
				}
			} else if (token.equals("LaTeX")) {
				StringBuffer stringbuffer6 = new StringBuffer();
				stringbuffer6.append(token + " ");
				lexer.nextToken();
				if (token.equals("Font")) {
					lexer.nextToken();
					if (token.equals("Info:")) {
						String s2 = lexer.seek('\n');
						if (s2.endsWith("> not available")) {
							s2 = strip(s2, "   ");
							lexer.crossLineSeek("on input line");
							model.add(new Entry(0, "LaTeX Font Info: " + s2
									+ ", line " + token, (String) filenames
									.peek(), Integer.parseInt(token.substring(
									0, token.length() - 1))));
							return;
						}
					} else if (token.equals("Warning:")) {
						lexer.nextToken();
						if (!token.equals("Some")) {
							String s3 = token + lexer.seek('\n');
							if (s3.endsWith("undefined")) {
								s3 = s3 + '\n' + token;
								s3 = s3 + lexer.crossLineSeek("on input line");
								s3 = s3.replace('\n', ' ');
								s3 = strip(s3, "(Font)              ");
								model.add(new Entry(1, "LaTeX Font Warning: "
										+ s3 + token,
										(String) filenames.peek(), Integer
												.parseInt(token.substring(0,
														token.length() - 1))));
								return;
							}
						}
					}
				} else if (token.equals("Warning:")) {
					stringbuffer6.append(token + " ");
					lexer.nextToken();
					if (token.equals("Citation")) {
						stringbuffer6.append(token + " "
								+ lexer.crossLineSeek("on input line") + " ");
						stringbuffer6.append(token);
						int j1 = Integer.parseInt(token.substring(0, token
								.length() - 1));
						model.add(new Entry(7, stringbuffer6.toString(),
								(String) filenames.peek(), j1));
						return;
					}
					if (token.equals("Reference")) {
						stringbuffer6.append(token + " ");
						String s4 = lexer.seek(".");
						s4 = strip(s4, '\n');
						int j2 = s4.lastIndexOf(' ');
						stringbuffer6.append(s4.substring(0, j2));
						int k1 = Integer.parseInt(s4.substring(j2 + 1, s4
								.length() - 1));
						stringbuffer6.append(" " + k1);
						model.add(new Entry(7, stringbuffer6.toString(),
								(String) filenames.peek(), k1));
						return;
					}
					if (token.equals("File")) {
						stringbuffer6
								.append(token
										+ " "
										+ lexer
												.crossLineSeek("not found on input line"));
						stringbuffer6.append(token);
						int l1 = Integer.parseInt(token.substring(0, token
								.length() - 1));
						model.add(new Entry(6, stringbuffer6.toString(),
								(String) filenames.peek(), l1));
						return;
					}
					if (!token.equals("There")) {
						stringbuffer6.append(token + " " + lexer.seek('.'));
						String s5 = stringbuffer6.toString();
						if (s5.indexOf("float specifier changed to") > -1)
							model.add(new Entry(5, s5, (String) filenames
									.peek(), 1));
						else
							model.add(new Entry(8, s5, (String) filenames
									.peek(), 1));
						return;
					}
				}
			} else {
				lexer.nextToken();
			}
		}
		if (token.endsWith("."))
			lexer.nextToken();
	}

	private void fixmeParse(StringBuffer stringbuffer) {
		byte byte0 = -1;
		lexer.nextToken();
		if (token.equals("Note:"))
			byte0 = 30;
		else if (token.equals("Warning:"))
			byte0 = 31;
		else if (token.equals("Error:"))
			byte0 = 32;
		else if (token.equals("Summary:")) {
			lexer.seek('.');
			return;
		}
		stringbuffer.append(" " + lexer.crossLineSeek("on input line") + " ");
		stringbuffer.append(token);
		int i = Integer.parseInt(token.substring(0, token.length() - 1));
		model.add(new Entry(byte0, stringbuffer.toString(), (String) filenames
				.peek(), i));
	}

	private void pageno() {
		lexer.nextToken();
		if (!token.equals("]"))
			lexer.nextToken();
		if (token.equals("{"))
			pdfinfo();
		if (token.equals("]"))
			lexer.nextToken();
		else
			System.out
					.println("parse error during pageno parse...expected ']' got '"
							+ token + "'");
	}

	private void pdfinfo() {
		while (!token.equals("]"))
			if (token.equals("Warning:")) {
				lexer.nextToken();
				if (token.equals("pdflatex")) {
					String s = "Warning: pdflatex "
							+ strip(lexer.seek("ExtendFont"), '\n');
					model.add(new Entry(1, s, (String) filenames.peek(), 1));
				} else {
					lexer.nextToken();
				}
			} else {
				lexer.nextToken();
			}
	}

	private String strip(String s, char c) {
		int i = s.indexOf(c);
		if (i == -1)
			return s;
		int j = 0;
		StringBuffer stringbuffer = new StringBuffer();
		do {
			stringbuffer.append(s.substring(j, i));
			j = i + 1;
			i = s.indexOf(c, j);
		} while (i != -1);
		stringbuffer.append(s.substring(j));
		return stringbuffer.toString();
	}

	private String strip(String s, String s1) {
		int i = s.indexOf(s1);
		if (i == 0)
			if (s.length() > s1.length())
				return strip(s.substring(s1.length()), s1);
			else
				return "";
		if (i > 0)
			return strip(s.substring(0, i) + s.substring(i + s1.length()), s1);
		else
			return s;
	}

	int infc;
	int prec;
	int filc;
	int pagc;
	int msgc;
	String logfilepath;
	Model model;
	// Controller controller;
	Lexer lexer;
	String token;
	int pos;
	Stack filenames;
	byte buf[];

}
