package com.triadsoft.properties.model.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;

/**
 * Tabla que muestra las columnas con las claves y los idiomas de los distintos
 * archivos de recursos
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 */
public class PropertyTableViewer extends TableViewer {
	private TableColumn defaultColumn;
	private Locale defaultLocale;
	private Locale[] locales;

	public PropertyTableViewer(Composite c, Locale defaultLocale) {
		super(c, SWT.SINGLE | SWT.FULL_SELECTION);
		Table table = getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		this.defaultLocale = defaultLocale;
	}

	private void createKeyColumn() {
		TableColumn keyColumn = new TableColumn(getTable(), SWT.NONE);
		keyColumn.setText(Activator.getString("editor.table.key"));
		keyColumn.setWidth(150);
	}

	private void createDefaultColumn() {
		defaultColumn = new TableColumn(getTable(), SWT.NONE);
		defaultColumn.setText(defaultLocale.toString());
		defaultColumn.setWidth(200);
	}

	private void createColumn(Locale locale) {
		TableColumn valueColumn = new TableColumn(getTable(), SWT.NONE);
		valueColumn.setText(locale.toString());
		valueColumn.setWidth(200);
	}

	public Locale[] getLocales() {
		return locales;
	}

	public void setLocales(Locale[] locales) {
		this.locales = locales;
		createKeyColumn();
		createDefaultColumn();

		List<CellEditor> editors = new LinkedList<CellEditor>();
		List<String> columnProperties = new LinkedList<String>();
		// Editor para la clave que es readonly
		editors.add(new TextCellEditor(getTable(), SWT.READ_ONLY));
		columnProperties.add(PropertiesEditor.KEY_COLUMN_ID);
		// Creo la columna para el locale por default
		editors.add(new TextCellEditor(getTable()));
		columnProperties.add(defaultLocale.toString());

		for (int i = 0; i < locales.length; i++) {
			if (locales[i].equals(defaultLocale)) {
				continue;
			}
			createColumn(locales[i]);
			editors.add(new TextCellEditor(getTable()));
			columnProperties.add(locales[i].toString());
		}
		setCellEditors(editors.toArray(new CellEditor[editors.size()]));
		setColumnProperties(columnProperties
				.toArray(new String[columnProperties.size()]));
		refresh(true);
	}
}
