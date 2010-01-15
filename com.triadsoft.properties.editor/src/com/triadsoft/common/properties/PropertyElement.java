/**
 * Created on Sep 14, 2004
 */
package com.triadsoft.common.properties;

/**
 * @author Triad (flores.leonardo@gmail.com)
 */
public abstract class PropertyElement {
	public static final PropertyElement[] NO_CHILDREN = {};
	private String[] separators = { "=" };
	private Character separator = null;

	private PropertyElement parent;

	public PropertyElement() {
		this(null, null);
	}

	public PropertyElement(PropertyElement parent) {
		this(parent, null);
	}

	public PropertyElement(PropertyElement parent, String[] separators) {
		this.parent = parent;
		this.separators = separators;
	}

	public PropertyElement getParent() {
		return parent;
	}

	public void setParent(PropertyElement parent) {
		this.parent = parent;
	}

	public final void setSeparator(Character separator) {
		if (parent != null) {
			parent.setSeparator(separator);
			return;
		}
		this.separator = separator;
	}

	public final Character getSeparator() {
		if (parent != null) {
			return parent.getSeparator();
		}
		return separator;
	}

	protected void discoveringSeparator(String codeLine) {
		for (int i = 0; i < separators.length; i++) {
			Character character = separators[i].charAt(0);
			String[] splitted = codeLine.split(character.toString());
			if (splitted.length == 2) {
				setSeparator(character);
				break;
			}
		}
	}

	protected final void setSeparators(String[] separators) {
		this.separators = separators;
	}

	protected String[] getSeparators() {
		return separators;
	}

	public abstract PropertyElement[] getChildren();

	public abstract void removeFromParent();

	public abstract boolean hasChildren();

	public abstract int getLine();

}