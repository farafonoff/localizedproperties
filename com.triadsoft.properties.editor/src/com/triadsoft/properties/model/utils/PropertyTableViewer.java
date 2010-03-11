package com.triadsoft.properties.model.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesContentProvider;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.editors.PropertiesLabelProvider;
import com.triadsoft.properties.editors.PropertiesSorter;
import com.triadsoft.properties.editors.PropertyModifier;
import com.triadsoft.properties.editors.actions.AddKeyAction;
import com.triadsoft.properties.editors.actions.AddLocaleAction;
import com.triadsoft.properties.editors.actions.CopyKeyAction;
import com.triadsoft.properties.editors.actions.RemoveKeyAction;
import com.triadsoft.properties.editors.actions.RemoveLocaleAction;

/**
 * Tabla que muestra las columnas con las claves y los idiomas de los distintos
 * archivos de recursos
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class PropertyTableViewer extends TableViewer {
	private static final String DEFAULT_TEXT = "<default>";
	private TableColumn defaultColumn;
	private Locale defaultLocale;
	private Locale[] locales;
	private PropertiesEditor editor;
	private PropertiesSorter sorter = new PropertiesSorter(this);
	private MenuManager mgr;
	private AddKeyAction addKeyAction;
	private RemoveKeyAction removeKeyAction;

	private CopyKeyAction copyKeyAction;
	private TableColumn selectedColumn;

	public PropertyTableViewer(PropertiesEditor editor, Composite parent,
			Locale defaultLocale) {
		// super(parent, SWT.SINGLE | SWT.FULL_SELECTION |
		// SWT.MouseDown|SWT.FULL_SELECTION);

		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.BORDER);
		this.editor = editor;

		setContentProvider(new PropertiesContentProvider());
		setLabelProvider(new PropertiesLabelProvider(this));
		setCellModifier(new PropertyModifier(editor));

		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		this.defaultLocale = defaultLocale;
		setSorter(sorter);
		this.createInitialActions();
		this.createContextMenu();
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
			defaultColumn.setText(DEFAULT_TEXT);
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
		final TableColumn valueColumn = new TableColumn(getTable(), SWT.NONE);
		valueColumn.setText(locale.toString());
		if (locale.toString().equals("xx_XX")) {
			valueColumn.setText(DEFAULT_TEXT);
		}
		valueColumn.setWidth(150);
		valueColumn.setResizable(true);
		valueColumn.setMoveable(true);
		valueColumn.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				TableColumn col = (TableColumn) event.getSource();
				sorter.setColumn(index);
				sorter.setLocale(locales[index - 2]);
				getTable().setSortColumn(col);
				refresh();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
//		valueColumn.addListener(SWT.Selection, new Listener() {
//			public void handleEvent(Event event) {
//				System.out.println(valueColumn.getText());
//			}
//		});
		// createMenuItem(mgr.getMenu(), valueColumn);
	}

	public Locale[] getLocales() {
		return locales;
	}

	public void setLocales(Locale[] locales) {
		this.locales = locales;
		this.localesChanged();
	}
	
	public void dispose(){
		this.cleanColumns();
		locales = null;
		mgr = null;
		defaultColumn =null;
		defaultLocale=null;
		editor=null;
		sorter = null;
	}

	private void localesChanged() {
		this.cleanColumns();
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
		mgr = new MenuManager("#PopupMenu");
		mgr.setRemoveAllWhenShown(true);

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

	private synchronized void cleanColumns() {
		while (getTable().getColumnCount() > 0) {
			getTable().getColumn(0).dispose();
		}
		setCellEditors(null);
		setColumnProperties(null);
		defaultColumn = null;
		if (selectedColumn != null) {
			selectedColumn.dispose();
		}
		selectedColumn = null;
		refresh();
	}

	private void createInitialActions() {
		addKeyAction = new AddKeyAction(editor, this);
		removeKeyAction = new RemoveKeyAction(editor, this);
		// removeLocaleAction = new RemoveLocaleAction(this, tableViewer,
		// );
		copyKeyAction = new CopyKeyAction(editor, this);
	}

	private void createColumnActions(IMenuManager menuMgr) {
		if (selectedColumn != null) {
			menuMgr.add(new Separator());
			RemoveLocaleAction rla = new RemoveLocaleAction(editor, this,
					StringUtils.getLocale(selectedColumn.getText()));
			if (selectedColumn.getText().equals(DEFAULT_TEXT)) {
				rla.setText(Activator.getString(
						RemoveLocaleAction.MENU_MENUITEM_DELETE_LOCALE,
						new Object[] { selectedColumn.getText() }));
			}

			if (!selectedColumn.getText().equals(
					Activator.getString("editor.table.key"))) {
				rla.setEnabled(true);
				menuMgr.add(rla);
			}

		}
		AddLocaleAction ala = new AddLocaleAction(editor, this);
		ala.setEnabled(true);
		menuMgr.add(ala);
	}

	private void createContextMenu() {
		mgr = new MenuManager("#PopupMenu1");
		mgr.setRemoveAllWhenShown(true);
		mgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				PropertyTableViewer.this.fillContextMenu(mgr);
			}
		});
		Table table = this.getTable();
		Menu menu = mgr.createContextMenu(table);
		table.setMenu(menu);
		table.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent event) {
				selectedColumn = PropertyTableViewer.this.getTableColumn(
						event.x, event.y);
			}
		});
		editor.getSite().registerContextMenu(mgr, this);
	}

	public TableColumn getTableColumn(int x, int y) {
		// FIXME: Ver ésto porque está harcodeado la diferencia
		x = x - 274;
		y = y - 148;
		Rectangle tableBounds = getTable().getClientArea();
		Rectangle contBounds = getTable().getParent().getBounds();
		Rectangle colBounds = new Rectangle(contBounds.x + tableBounds.x,
				contBounds.y + tableBounds.y, tableBounds.width,
				tableBounds.height);
		for (int i = 0; i < this.getTable().getColumnCount(); i++) {
			TableColumn col = this.getTable().getColumn(i);
			colBounds.width = col.getWidth();

			if (x > colBounds.x && x < colBounds.x + colBounds.width) {
				return col;
			}
			colBounds.x = colBounds.x + col.getWidth();
		}
		return null;
	}

	private void fillContextMenu(IMenuManager menuMgr) {
		addKeyAction.setEnabled(true);
		menuMgr.add(addKeyAction);

		boolean isEmpty = this.getSelection().isEmpty();
		if (!isEmpty) {
			removeKeyAction.setEnabled(true);
			menuMgr.add(removeKeyAction);
		}
		menuMgr.add(copyKeyAction);
		this.createColumnActions(menuMgr);
		// menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
}
