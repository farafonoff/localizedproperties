/**
 * Created on Sep 14, 2004
 */
package com.triadsoft.common.properties;

/**
 * @author lflores
 */
public abstract class PropertyElement {
	public static final PropertyElement[] NO_CHILDREN = {};

	private PropertyElement parent;

	public PropertyElement(PropertyElement parent) {
		this.parent = parent;
	}

	public PropertyElement getParent() {
		return parent;
	}
	
	public void setParent(PropertyElement parent){
		this.parent=parent;
	}

	public abstract PropertyElement[] getChildren();

	public abstract void removeFromParent();

	public abstract boolean hasChildren();
    
    public abstract int getLine();
}