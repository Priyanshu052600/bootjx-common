package com.boot.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.boot.model.SafeString;
import com.boot.model.UtilityModels.Stringable;

public abstract class BashUtil {
	private static final int NO_TOKEN_STATE = 0;
	private static final int NORMAL_TOKEN_STATE = 1;
	private static final int SINGLE_QUOTE_STATE = 2;
	private static final int DOUBLE_QUOTE_STATE = 3;

	/**
	 * Tokenizes the given String into String tokens
	 *
	 * @param arguments A String containing one or more command-line style arguments
	 *                  to be tokenized.
	 * @return A list of parsed and properly escaped arguments.
	 */
	public static List<String> tokenize(String arguments) {
		return tokenize(arguments, false);
	}

	/**
	 * Tokenizes the given String into String tokens.
	 *
	 * @param arguments A String containing one or more command-line style arguments
	 *                  to be tokenized.
	 * @param stringify whether or not to include escape special characters
	 * @return A list of parsed and properly escaped arguments.
	 */
	public static List<String> tokenize(String arguments, boolean stringify) {

		LinkedList<String> argList = new LinkedList<String>();
		StringBuilder currArg = new StringBuilder();
		boolean escaped = false;
		int state = NO_TOKEN_STATE; // start in the NO_TOKEN_STATE
		int len = arguments.length();

		// Loop over each character in the string
		for (int i = 0; i < len; i++) {
			char c = arguments.charAt(i);
			if (escaped) {
				// Escaped state: just append the next character to the current arg.
				escaped = false;
				currArg.append(c);
			} else {
				switch (state) {
				case SINGLE_QUOTE_STATE:
					if (c == '\'') {
						// Seen the close quote; continue this arg until whitespace is seen
						state = NORMAL_TOKEN_STATE;
					} else {
						currArg.append(c);
					}
					break;
				case DOUBLE_QUOTE_STATE:
					if (c == '"') {
						// Seen the close quote; continue this arg until whitespace is seen
						state = NORMAL_TOKEN_STATE;
					} else if (c == '\\') {
						// Look ahead, and only escape quotes or backslashes
						i++;
						char next = arguments.charAt(i);
						if (next == '"' || next == '\\') {
							currArg.append(next);
						} else {
							currArg.append(c);
							currArg.append(next);
						}
					} else {
						currArg.append(c);
					}
					break;
//          case NORMAL_TOKEN_STATE:
//            if (Character.isWhitespace(c)) {
//              // Whitespace ends the token; start a new one
//              argList.add(currArg.toString());
//              currArg = new StringBuffer();
//              state = NO_TOKEN_STATE;
//            }
//            else if (c == '\\') {
//              // Backslash in a normal token: escape the next character
//              escaped = true;
//            }
//            else if (c == '\'') {
//              state = SINGLE_QUOTE_STATE;
//            }
//            else if (c == '"') {
//              state = DOUBLE_QUOTE_STATE;
//            }
//            else {
//              currArg.append(c);
//            }
//            break;
				case NO_TOKEN_STATE:
				case NORMAL_TOKEN_STATE:
					switch (c) {
					case '\\':
						escaped = true;
						state = NORMAL_TOKEN_STATE;
						break;
					case '\'':
						state = SINGLE_QUOTE_STATE;
						break;
					case '"':
						state = DOUBLE_QUOTE_STATE;
						break;
					default:
						if (!Character.isWhitespace(c)) {
							currArg.append(c);
							state = NORMAL_TOKEN_STATE;
						} else if (state == NORMAL_TOKEN_STATE) {
							// Whitespace ends the token; start a new one
							argList.add(currArg.toString());
							currArg = new StringBuilder();
							state = NO_TOKEN_STATE;
						}
					}
					break;
				default:
					throw new IllegalStateException("ArgumentTokenizer state " + state + " is invalid!");
				}
			}
		}

		// If we're still escaped, put in the backslash
		if (escaped) {
			currArg.append('\\');
			argList.add(currArg.toString());
		}
		// Close the last argument if we haven't yet
		else if (state != NO_TOKEN_STATE) {
			argList.add(currArg.toString());
		}
		// Format each argument if we've been told to stringify them
		if (stringify) {
			for (int i = 0; i < argList.size(); i++) {
				argList.set(i, "\"" + _escapeQuotesAndBackslashes(argList.get(i)) + "\"");
			}
		}
		return argList;
	}

	/**
	 * Inserts backslashes before any occurrences of a backslash or quote in the
	 * given string. Also converts any special characters appropriately.
	 */
	protected static String _escapeQuotesAndBackslashes(String s) {
		final StringBuilder buf = new StringBuilder(s);

		// Walk backwards, looking for quotes or backslashes.
		// If we see any, insert an extra backslash into the buffer at
		// the same index. (By walking backwards, the index into the buffer
		// will remain correct as we change the buffer.)
		for (int i = s.length() - 1; i >= 0; i--) {
			char c = s.charAt(i);
			if ((c == '\\') || (c == '"')) {
				buf.insert(i, '\\');
			}
			// Replace any special characters with escaped versions
			else if (c == '\n') {
				buf.deleteCharAt(i);
				buf.insert(i, "\\n");
			} else if (c == '\t') {
				buf.deleteCharAt(i);
				buf.insert(i, "\\t");
			} else if (c == '\r') {
				buf.deleteCharAt(i);
				buf.insert(i, "\\r");
			} else if (c == '\b') {
				buf.deleteCharAt(i);
				buf.insert(i, "\\b");
			} else if (c == '\f') {
				buf.deleteCharAt(i);
				buf.insert(i, "\\f");
			}
		}
		return buf.toString();
	}

	public static class BashArguments {

	}

	public static class CurlCommand implements Serializable, Stringable {
		private static final long serialVersionUID = 2226272517915004544L;
		private String bash;
		private String command;
		private String method;
		private String url;
		private List<String> headers;
		private List<String> auths;
		private Map<String, Object> data;
		private Map<String, String> fields;

		@Override
		public void fromString(String text) {
			if (!ArgUtil.is(text)) {
				return;
			}
			this.bash = text;
			List<String> args = tokenize(text);
			for (int i = 0; i < args.size(); i++) {
				String option = args.get(i);
				if (option.startsWith("http://") || option.startsWith("https://")) {
					this.setUrl(option);
				}

				SafeString optionValue = SafeString.from(CollectionUtil.get(args, i + 1));
				switch (option) {
				case "-X":
					this.setMethod(optionValue.asString());
					i++;
					break;
				case "-H":
					this.header(optionValue.asString());
					i++;
					break;
				case "-u":
					this.auth(optionValue.asString());
					i++;
					break;
				case "-d":
					optionValue.trim();
					if (optionValue.startsWith("{")) {
						this.setData(JsonUtil.fromJsonToMap(optionValue.asString()));
					} else if (optionValue.exists()) {
						Map<String, String> fields = StringUtils.getMapFromString("&", "=", optionValue.asString());
						this.fields(fields);
					}
					i++;
					break;
				default:
					break;
				}
			}
		}

		public static CurlCommand parse(String text) {
			CurlCommand req = new CurlCommand();
			req.fromString(text);
			return req;
		}

		@Override
		public String toString() {
			return this.bash;
		}

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}

		public CurlCommand header(String header) {
			if (this.headers == null) {
				this.headers = new ArrayList<String>();
			}
			this.headers.add(header);
			return this;
		}

		public CurlCommand auth(String header) {
			if (this.auths == null) {
				this.auths = new ArrayList<String>();
			}
			this.auths.add(header);
			return this;
		}

		public List<String> getHeaders() {
			return headers;
		}

		public void setHeaders(List<String> headers) {
			this.headers = headers;
		}

		public Map<String, Object> getData() {
			return data;
		}

		public void setData(Map<String, Object> data) {
			this.data = data;
		}

		public CurlCommand fields(Map<String, String> morefields) {
			if (this.fields == null) {
				this.fields = new HashMap<String, String>();
			}
			this.fields.putAll(morefields);
			return this;
		}

		public Map<String, String> getFields() {
			return fields;
		}

		public void setFields(Map<String, String> fields) {
			this.fields = fields;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getBash() {
			return bash;
		}

		public void setBash(String bash) {
			this.bash = bash;
		}

		public List<String> getAuths() {
			return auths;
		}

		public void setAuths(List<String> auths) {
			this.auths = auths;
		}

	}

}