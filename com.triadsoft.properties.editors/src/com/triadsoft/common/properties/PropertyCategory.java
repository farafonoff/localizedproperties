/**
 * Created on Sep 14, 2004
 */
package com.triadsoft.common.properties;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author lflores
 */
public class PropertyCategory extends PropertyElement {
	private String name;
	private List<PropertyEntry> entries;
	private int lineNumber = 0;

	public PropertyCategory(PropertyFile parent, String name) {
		super(parent);
		this.name = name;
		entries = new ArrayList<PropertyEntry>();
	}

	public PropertyCategory(PropertyFile parent, LineNumberReader reader)
			throws IOException {
		super(parent);
		this.lineNumber = reader.getLineNumber();
		this.readCategoryName(reader);
		if (name == null) {
			name = "";
		}

		// Determine the properties in this category.
		entries = new ArrayList<PropertyEntry>();
		this.readEntries(reader);
	}

	/**
	 * @param reader
	 * @throws IOException
	 */
	private void readEntries(LineNumberReader reader) throws IOException {
		while (true) {
			reader.mark(1);
			int ch = reader.read();
			if (ch == -1) {
				break;
			}
			reader.reset();
			if (ch == '#') {
				break;
			}
			String line = reader.readLine();
			int index = line.indexOf('=');
			if (index != -1) {
				String key = line.substring(0, index).trim();
				String value = line.substring(index + 1).trim();
				entries.add(new PropertyEntry(this, key, value, 0, index,
						reader.getLineNumber()));
			}
		}
	}

	/**
	 * @param reader
	 * @throws IOException
	 */
	private void readCategoryName(LineNumberReader reader) throws IOException {
		while (true) {
			reader.mark(1);
			int ch = reader.read();
			if (ch == -1) {
				break;
			}
			reader.reset();
			if (ch != '#') {
				break;
			}
			String line = reader.readLine();
			line = line.replace('#', ' ').trim();
			if (name == null) {
				if (line.length() > 0) {
					name = line;
				}
			}else{
				//Si name es distinto de null, hago un append
				name += '\n';
				name += line;
			}
		}
	}

	public String getName() {
		return name;
	}

	public Collection<PropertyEntry> getEntries() {
		return entries;
	}
	
	public PropertyEntry getEntry(String entryKey){
		for (Iterator<PropertyEntry> iterator = entries.iterator(); iterator.hasNext();) {
			PropertyEntry entry = (PropertyEntry) iterator.next();
			if( entry.getKey().equals(entryKey)){
				return entry;
			}
		}
		return null;
	}

	public PropertyElement[] getChildren() {
		return (PropertyElement[]) entries.toArray(new PropertyElement[entries
				.size()]);
	}

	public void setName(String text) {
		if (name.equals(text))
			return;
		name = text;
		((PropertyFile) getParent()).nameChanged(this);
	}

	public void addEntry(PropertyEntry entry) {
		if (!entries.contains(entry)) {
			entries.add(entry);
			((PropertyFile) getParent()).entryAdded(this, entry);
		}
	}

	public void removeEntry(PropertyEntry entry) {
		if (entries.remove(entry)) {
			((PropertyFile) getParent()).entryRemoved(this, entry);
		}
	}

	public void removeFromParent() {
		((PropertyFile) getParent()).removeCategory(this);
	}

	public void keyChanged(PropertyEntry entry) {
		((PropertyFile) getParent()).keyChanged(this, entry);
	}

	public void valueChanged(PropertyEntry entry) {
		((PropertyFile) getParent()).valueChanged(this, entry);
	}

	/**
	 * @see com.sistran.tools.editors.views.PropertyElement#hasChildren()
	 */
	public boolean hasChildren() {
		if (entries != null && !entries.isEmpty()) {
			return true;
		}
		return false;
	}

	public void appendText(PrintWriter writer) {
		if (name.length() > 0) {
			//Valido si el comentario es multilinea
			//entonces hago un split y en cada inicio le agrego el numeral
			//junto al texto
			String[] nameSplited = name.split("\\n");
			for (int i = 0; i < nameSplited.length; i++) {
				writer.print("#");
				writer.println(nameSplited[i]);
			}
		}
		Iterator<PropertyEntry> iter = entries.iterator();
		while (iter.hasNext())
			((PropertyEntry) iter.next()).appendText(writer);
	}

	/**
	 * Devuelve un booleano que indica si la clave existe en esta categoria
	 * 
	 * @param entryValue
	 * @return
	 */
	public boolean existEntry(String entryValue) {
		PropertyElement[] elements = getChildren();
		if (elements instanceof PropertyCategory[]) {
			return false;
		}

		for (int i = 0; i < elements.length; i++) {
			if (((PropertyEntry) elements[i]).getKey().equals(entryValue)) {
				return true;
			}
		}
		return false;
	}

	public int getLine() {
		return this.lineNumber;
	}
	
	@Override
	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("name:'"+name+"'");
		return returnString.toString();
	}
}