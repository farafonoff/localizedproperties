package com.triadsoft.common.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * Esta clase es la encargada de parsear un archivo de properties, y dividirlo
 * en categorias y entradas. Tambien se puede persistir mediante el metodo save
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class PropertyFile extends PropertyElement {

	public static final String DEFAULT_EXTENSION = "properties";
	private List<PropertyCategory> categories;
	private List<IPropertyFileListener> listeners = new ArrayList<IPropertyFileListener>();
	private IFile file = null;
	private String encoding;

	/**
	 * 
	 * @param file
	 *            File para parsear
	 * @param encoding
	 *            Encoding del archivo a parsear
	 * @throws IOException
	 */
	public PropertyFile(File file, String encoding, String[] separators)
			throws IOException {
		super(null, separators);
		this.encoding = encoding;
		if (this.encoding == null) {
			throw new RuntimeException("No pude encontrar el encoding");
		}
		this.load(file);
	}

	/**
	 * Permite crear un property file a partir de un IFile
	 * 
	 * @param file
	 * @throws IOException
	 * @throws CoreException
	 */
	public PropertyFile(IFile ifile, String[] separators) throws IOException,
			CoreException {
		this.file = ifile;
		this.encoding = ifile.getCharset();
		setSeparators(separators);
		this.load(ifile.getLocation().toFile());
	}

	public PropertyFile(IFile ifile, Character separator) throws IOException,
			CoreException {
		this(ifile, ifile.getCharset(), separator);
	}

	public PropertyFile(IFile ifile, String encoding, Character separator)
			throws IOException {
		this.file = ifile;
		this.encoding = encoding;
		setSeparator(separator);
		this.load(ifile.getLocation().toFile());
	}

	public PropertyFile(String content, String encoding, Character separator)
			throws IOException {
		super();
		this.encoding = encoding;
		setSeparator(separator);
		this.load(content);
	}

	/**
	 * 
	 * @param content
	 * @throws IOException
	 */
	public PropertyFile(String content, String[] separators) throws IOException {
		super();
		setSeparators(separators);
		this.load(content);
	}

	public String getEncoding() {
		return encoding;
	}

	/**
	 * Permite cargar el property file, a partir de un file
	 * 
	 * @param content
	 * @throws IOException
	 */
	private void load(File file) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(stream, encoding);
		BufferedReader reader = new BufferedReader(isr);

		StringBuffer buffer = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line).append("\n");
		}
		stream.close();
		this.load(buffer.toString());
	}

	/**
	 * Permite cargar el property file, a partir de un contenido
	 * 
	 * @param content
	 * @throws IOException
	 */
	private void load(String content) throws IOException {
		this.categories = new ArrayList<PropertyCategory>();
		LineNumberReader reader = new LineNumberReader(
				new StringReader(content));
		categories.add(new PropertyCategory(this, reader));
		while (true) {
			reader.mark(1);
			int ch = reader.read();
			if (ch == -1) {
				break;
			}
			reader.reset();
			categories.add(new PropertyCategory(this, reader));
		}
	}

	public PropertyElement[] getChildren() {
		List<PropertyElement> children = new ArrayList<PropertyElement>();
		// children.addAll(unnamedCategory.getEntries());
		children.addAll(categories);
		return (PropertyElement[]) children
				.toArray(new PropertyElement[children.size()]);
	}

	public void addCategory(PropertyCategory category) {
		if (!categories.contains(category)) {
			categories.add(category);
			categoryAdded(category);
		}
	}

	public void removeCategory(PropertyCategory category) {
		if (categories.remove(category)) {
			categoryRemoved(category);
		}
	}

	public void removeFromParent() {
		//
	}

	public void addPropertyFileListener(IPropertyFileListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removePropertyFileListener(IPropertyFileListener listener) {
		listeners.remove(listener);
	}

	public void keyChanged(PropertyCategory category, PropertyEntry entry) {
		Iterator<IPropertyFileListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			((IPropertyFileListener) iter.next()).keyChanged(category, entry);
		}
	}

	public void valueChanged(PropertyCategory category, PropertyEntry entry) {
		Iterator<IPropertyFileListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			((IPropertyFileListener) iter.next()).valueChanged(category, entry);
		}
	}

	public void nameChanged(PropertyCategory category) {
		Iterator<IPropertyFileListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			((IPropertyFileListener) iter.next()).nameChanged(category);
		}
	}

	public void entryAdded(PropertyCategory category, PropertyEntry entry) {
		Iterator<IPropertyFileListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			((IPropertyFileListener) iter.next()).entryAdded(category, entry);
		}
	}

	public void entryRemoved(PropertyCategory category, PropertyEntry entry) {
		Iterator<IPropertyFileListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			((IPropertyFileListener) iter.next()).entryRemoved(category, entry);
		}
	}

	public void categoryAdded(PropertyCategory category) {
		Iterator<IPropertyFileListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			((IPropertyFileListener) iter.next()).categoryAdded(category);
		}
	}

	public void categoryRemoved(PropertyCategory category) {
		Iterator<IPropertyFileListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			((IPropertyFileListener) iter.next()).categoryRemoved(category);
		}
	}

	/**
	 * @see com.triadsoft.common.properties.PropertyElement#hasChildren()
	 */
	public boolean hasChildren() {
		if (categories != null && !categories.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Devuelve true, si existe una categoria con este nombre
	 * 
	 * @param categoryName
	 * @return Booleano TRUE o FALSE
	 */
	public boolean existCategory(String categoryName) {
		for (Iterator<PropertyCategory> iter = categories.iterator(); iter
				.hasNext();) {
			PropertyCategory category = (PropertyCategory) iter.next();
			if (categoryName.equals(category.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve verdadero si existe la propiedad en el archivo
	 * 
	 * @param propertyName
	 * @return Booleano TRUE o FALSE
	 */
	public boolean exist(String propertyName) {
		for (Iterator<PropertyCategory> iter = this.categories.iterator(); iter
				.hasNext();) {
			PropertyCategory category = (PropertyCategory) iter.next();
			if (category.existEntry(propertyName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Convierte el PropertyFile en texto
	 * 
	 * @return un String con todo el contenido del archivo de propiedades
	 */
	public String asText() {
		StringWriter stringWriter = new StringWriter(2000);
		PrintWriter writer = new PrintWriter(stringWriter);
		Iterator<PropertyCategory> iter = categories.iterator();
		while (iter.hasNext()) {
			((PropertyCategory) iter.next()).appendText(writer);
		}
		return stringWriter.toString();
	}

	/**
	 * Devuelve la categoria por default. La categoria por default es un
	 * categoria que se crea para todas las claves que no pertenecen a un
	 * categoria. Todas las claves entontradas antes de un comentario, son
	 * agregadas a esta categoria, ya que no se puede identificar a la categoria
	 * a la que pertenecen
	 * 
	 * @return PropertyCategory
	 * @see PropertyCategory
	 */
	public PropertyCategory getDefaultCategory() {
		PropertyCategory category = null;
		for (Iterator<PropertyCategory> iter = this.categories.iterator(); iter
				.hasNext();) {
			category = (PropertyCategory) iter.next();
			if (category.getName().trim().length() == 0) {
				return category;
			}
		}
		if (category == null) {
			category = new PropertyCategory((PropertyFile) this.getParent(), "");
			addCategory(category);
		}
		return category;
	}

	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}

	// public void resourceChanged(IResourceChangeEvent event) {
	// IResourceDelta delta = event.getDelta();
	// final ArrayList<IFile> propertyFiles = new ArrayList<IFile>();
	// IResourceDeltaVisitor propertyFile = new IResourceDeltaVisitor() {
	// public boolean visit(IResourceDelta delta) throws CoreException {
	// // only interested in changed resources (not added or removed)
	// if (delta.getKind() != IResourceDelta.CHANGED)
	// return true;
	// // only interested in content changes
	// if ((delta.getFlags() & IResourceDelta.CONTENT) == 0) {
	// return true;
	// }
	// IResource resource = delta.getResource();
	// // only interested in files with the "proeprty" extension
	// if (resource.getType() == IResource.FILE
	// && DEFAULT_EXTENSION.equalsIgnoreCase(resource
	// .getFileExtension())) {
	// propertyFiles.add((IFile) resource);
	// }
	// return true;
	// }
	// };
	// try {
	// delta.accept(propertyFile);
	// for (Iterator<IFile> iter = propertyFiles.iterator(); iter
	// .hasNext();) {
	// IFile file = (IFile) iter.next();
	// if (this.file != null
	// && file.getName().equals(this.file.getName())) {
	// LocalizedPropertiesLog.debug("Cambio " + file.getName()
	// + "!!!!", null);
	// Iterator<IPropertyFileListener> listenersIterator = this.listeners
	// .iterator();
	// while (listenersIterator.hasNext()) {
	// IPropertyFileListener listener = (IPropertyFileListener)
	// listenersIterator
	// .next();
	// listener.fileChanged(this);
	// }
	// }
	// }
	// } catch (CoreException e) {
	// // open error dialog with syncExec or print to plugin log file
	// }
	// }

	/**
	 * Devuelve una categoria a partir de su nombre
	 * 
	 * @param categoryName
	 *            Nombre de la categoria buscada
	 * @return PropertyCategory encontrada, null si no la encuentra
	 */
	public PropertyCategory getCategoryByName(String categoryName) {
		for (int i = 0; i < getChildren().length; i++) {
			PropertyCategory cat = (PropertyCategory) getChildren()[i];
			if (cat.getName().equals(categoryName)) {
				return cat;
			}
		}
		return null;
	}

	/**
	 * Devuelve la categoria a partir del key buscado
	 * 
	 * @param entryKey
	 * @return null si no existe la entrada
	 */
	public PropertyCategory getCategoryFromEntry(String entryKey) {
		for (int i = 0; i < getChildren().length; i++) {
			PropertyCategory cat = (PropertyCategory) getChildren()[i];
			if (cat.existEntry(entryKey)) {
				return cat;
			}
		}
		return null;
	}

	/**
	 * Devuelve el objeto PropertyEntry a partir de la clave
	 * 
	 * @param entryKey
	 *            String que identifica a la entrada
	 * @return ProperyEntry
	 */
	public PropertyEntry getPropertyEntry(String entryKey) {
		PropertyCategory category = getCategoryFromEntry(entryKey);
		if (category == null) {
			return null;
		}
		return category.getEntry(entryKey);
	}

	/**
	 * Mueve un entrada a un categoria
	 * 
	 * @param entry
	 * @param destinityCategory
	 */
	public boolean moveToCategory(PropertyEntry entry,
			PropertyCategory destinityCategory) {
		if (((PropertyCategory) entry.getParent()).getName().equals(
				destinityCategory.getName())) {
			// Si es la misma no hace nada
			return false;
		}
		entry.removeFromParent();
		entry.setParent(destinityCategory);
		return true;
	}

	/**
	 * @see com.triadsoft.common.properties.PropertyElement#getLine()
	 */
	public int getLine() {
		return 0;
	}

	public PropertyEntry[] getEntries() {
		List<PropertyEntry> entries = new LinkedList<PropertyEntry>();
		for (Iterator<PropertyCategory> iterator = categories.iterator(); iterator
				.hasNext();) {
			PropertyCategory category = (PropertyCategory) iterator.next();
			entries.addAll(category.getEntries());
		}
		return (PropertyEntry[]) entries.toArray(new PropertyEntry[entries
				.size()]);
	}

	public String[] getKeys() {
		List<String> keys = new LinkedList<String>();
		PropertyEntry[] entries = this.getEntries();
		for (int i = 0; i < entries.length; i++) {
			keys.add(entries[i].getKey());
		}
		return (String[]) keys.toArray(new String[keys.size()]);
	}

	public void save() throws IOException, CoreException {
		FileOutputStream stream = new FileOutputStream(new File(file
				.getLocationURI()));
		Writer out = new OutputStreamWriter(stream, encoding);
		out.write(asText());
		out.close();
		// file.getParent().refreshLocal(IFile.DEPTH_ONE, null);
	}

	public static void main(String[] args) {
		// try {
		// //PropertyFile file = new PropertyFile(new File(args[0]), "UTF8",
		// // new String[] { "=" });
		// // Activator.debug(file.asText(), null);
		// } catch (IOException e) {
		// Activator.getLogger().error(e.getLocalizedMessage());
		// }
	}

	public void fileChanged(PropertyFile propertyFile) {

	}

	public void keyRemoved(String key) {
		PropertyEntry entry = getPropertyEntry(key);
		entryRemoved((PropertyCategory) entry.getParent(), entry);
	}
}