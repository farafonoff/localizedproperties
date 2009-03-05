/**
 * Created on Sep 14, 2004
 */
package com.triadsoft.common.properties;

/**
 * @author lflores
 */
public interface PropertyFileListener {
    void keyChanged(PropertyCategory category, PropertyEntry entry);

    void valueChanged(PropertyCategory category, PropertyEntry entry);

    void nameChanged(PropertyCategory category);

    void entryAdded(PropertyCategory category, PropertyEntry entry);

    void entryRemoved(PropertyCategory category, PropertyEntry entry);

    void categoryAdded(PropertyCategory category);

    void categoryRemoved(PropertyCategory category);
    
    void fileChanged(PropertyFile propertyFile); 
}