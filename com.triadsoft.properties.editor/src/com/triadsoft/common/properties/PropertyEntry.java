/**
 * Created on Sep 14, 2004
 */
package com.triadsoft.common.properties;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;

/**
 * @author Triad (flores.leonardo@gmail.com)
 */
public class PropertyEntry extends PropertyElement {
	private String key;
	private String value;
	private int lineNumber;
	private int start = 0;
	private int end = 1;

	public PropertyEntry(PropertyCategory parent, LineNumberReader reader,
			Character separator) throws IOException {
		super(parent);
		while (true) {
			try {
				reader.mark(1);
				int ch = reader.read();
				if (ch == -1) {
					break;
				}
				if (ch == '#') {
					reader.reset();
					break;
				}
				reader.reset();
				String line = reader.readLine();
				int index = line.indexOf(getSeparator());
				if (index != -1) {
					this.key = line.substring(0, index).trim();
					this.value = line.substring(index + 1).trim();
					this.lineNumber = reader.getLineNumber();
					break;
				}
			} catch (IOException ex) {
				throw ex;
			}
		}
	}

	/**
	 * Crea la propiedad sin el control de la linea
	 * 
	 * @param parent
	 * @param key
	 * @param value
	 */
	public PropertyEntry(PropertyCategory parent, String key, String value) {
		this(parent, key, value, 0, 2, 0);
	}

	public PropertyEntry(PropertyCategory parent, String key, String value,
			int start, int end, int lineNumber) {
		super(parent);
		this.key = key;
		setValue(value);
		this.lineNumber = lineNumber;
		this.start = start;
		this.end = end;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public PropertyElement[] getChildren() {
		return NO_CHILDREN;
	}

	public void setKey(String text) {
		if (key.equals(text))
			return;
		key = text;
		((PropertyCategory) getParent()).keyChanged(this);
	}

	public void setValue(String text) {
		try {
			if (text != null && text.trim().length() == 0) {
				value = null;
				return;
			} else if (value != null && value.equals(text)) {
				return;
			} else if (text != null && text.equals("null")) {
				value = null;
				return;
			}
			value = text;
			if (getParent() != null) {
				((PropertyCategory) getParent()).valueChanged(this);
			}
		} catch (Exception e) {
			// no hago nada
			value = null;
			return;
		}
	}

	public void removeFromParent() {
		((PropertyCategory) getParent()).removeEntry(this);
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @see com.triadsoft.common.properties.PropertyElement#hasChildren()
	 */
	public boolean hasChildren() {
		// the end of tree
		return false;
	}

	public void appendText(PrintWriter writer) {
		writer.print(key);
		if (getSeparator() == null) {
			setSeparator(LocalizedPropertiesPlugin.getDefaultSeparator());
		}
		writer.print(getSeparator());
		if (value != null) {
			writer.print(value);
		}
		writer.println();
	}

	public int getLine() {
		return this.lineNumber;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getKey() + getSeparator() + getValue();
	}
}