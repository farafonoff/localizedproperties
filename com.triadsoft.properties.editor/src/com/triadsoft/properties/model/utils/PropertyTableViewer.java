package com.triadsoft.properties.model.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.editors.PropertiesSorter;

/**
 * Tabla que muestra las columnas con las claves y los idiomas de los distintos
 * archivos de recursos
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class PropertyTableViewer extends TableViewer {
	private TableColumn defaultColumn;
	private Locale defaultLocale;
	private Locale[] locales;
	private PropertiesSorter sorter = new PropertiesSorter(this);

	public PropertyTableViewer(Composite c, Locale defaultLocale) {
		super(c, SWT.SINGLE | SWT.FULL_SELECTION | SWT.MouseDown);
		Table table = getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		this.defaultLocale = defaultLocale;
		setSorter(sorter);
	}

	private void createKeyColumn() {
		TableColumn keyColumn = new TableColumn(getTable(), SWT.NONE);
		keyColumn.setText(Activator.getString("editor.table.key"));
		keyColumn.setWidth(150);
		keyColumn.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				TableColumn col = (TableColumn) event.getSource();
				sorter.setColumn(0);
				getTable().setSortColumn(col);
				refresh();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}

	private void createDefaultColumn() {
		defaultColumn = new TableColumn(getTable(), SWT.NONE);
		defaultColumn.setText(defaultLocale.toString());
		if (defaultLocale.toString().equals("xx_XX")) {
			defaultColumn.setText("<default>");
		}
		defaultColumn.setWidth(150);
		defaultColumn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				TableColumn col = (TableColumn) event.getSource();
				sorter.setColumn(1);
				sorter.setLocale(locales[0]);
				getTable().setSortColumn(col);
				refresh();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}

	private void createColumn(final Locale locale, final int index) {
		TableColumn valueColumn = new TableColumn(getTable(), SWT.NONE);
		valueColumn.setText(locale.toString());
		if (locale.toString().equals("xx_XX")) {
			valueColumn.setText("<default>");
		}
		valueColumn.setWidth(150);
		valueColumn.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				TableColumn col = (TableColumn) event.getSource();
				sorter.setColumn(index);
				sorter.setLocale(locales[index - 2]);
				getTable().setSortColumn(col);
				refresh();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
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
			createColumn(locales[i], i + 2);
			editors.add(new TextCellEditor(getTable()));
			columnProperties.add(locales[i].toString());
		}
		setCellEditors(editors.toArray(new CellEditor[editors.size()]));
		setColumnProperties(columnProperties
				.toArray(new String[columnProperties.size()]));
		refresh(true);
	}
}
