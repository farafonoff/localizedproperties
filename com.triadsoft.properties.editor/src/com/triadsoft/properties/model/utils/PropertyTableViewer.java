package com.triadsoft.properties.model.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.triadsoft.common.utils.LocalizedPropertiesMessages;
import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.editors.PropertiesContentProvider;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.editors.PropertiesLabelProvider;
import com.triadsoft.properties.editors.PropertiesSorter;
import com.triadsoft.properties.editors.PropertyModifier;
import com.triadsoft.properties.editors.actions.AddKeyAction;
import com.triadsoft.properties.editors.actions.AddLocaleAction;
import com.triadsoft.properties.editors.actions.CopyKeyAction;
import com.triadsoft.properties.editors.actions.CopyPropertyAction;
import com.triadsoft.properties.editors.actions.DecreaseFontAction;
import com.triadsoft.properties.editors.actions.IncreaseFontAction;
import com.triadsoft.properties.editors.actions.PastePropertyAction;
import com.triadsoft.properties.editors.actions.RemoveLocaleAction;
import com.triadsoft.properties.editors.actions.RemovePropertyAction;
import com.triadsoft.properties.editors.actions.RemoveSearchTextAction;
import com.triadsoft.properties.editors.actions.SearchTextAction;
import com.triadsoft.properties.editors.actions.SetDefaultTextAction;

/**
 * Table that shows columns with locales and keys on rows
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class PropertyTableViewer extends TableViewer {
	private static final int DEFAULT_COLUMN_SIZE = 150;
	private static final String PREFERENCES_COLUMNS_WIDTH = "preferences.columns.width";
	protected static final String EDITOR_TABLE_MODIFY_KEY_NULLVALUE = "editor.table.modifyKey.nullvalue";
	public static final String PREFERENCES_FONT_SIZE = "preferences.font.size";
	protected static final String PREFERENCES_FONT_MIN_SIZE = "preferences.font.minSize";
	protected static final String PREFERENCES_FONT_MAX_SIZE = "preferences.font.maxSize";

	protected static final String EDITOR_TABLE_KEY = "editor.table.key";
	private static final String DEFAULT_TEXT = "<default>";
	private TableColumn defaultColumn;
	private Locale defaultLocale;
	private Locale[] locales;
	private PropertiesEditor editor;
	private PropertiesSorter sorter = new PropertiesSorter(this);
	private MenuManager mgr;
	private AddKeyAction addKeyAction;
	private SetDefaultTextAction setDefaultTextAction;
	private RemovePropertyAction removePropertyAction;
	private IncreaseFontAction increaseFontAction;
	private DecreaseFontAction decreaseFontAction;

	private SearchTextAction searchTextAction;
	private RemoveSearchTextAction removeSearchTextAction;

	private CopyKeyAction copyKeyAction;
	private TableColumn selectedColumn;
	private CopyPropertyAction copyProperty;
	private PastePropertyAction pastProperty;

	public PropertyTableViewer(PropertiesEditor editor, Composite parent,
			Locale defaultLocale) {
		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.BORDER);
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		this.editor = editor;
		setFontSize(LocalizedPropertiesPlugin.getDefault().getPreferenceStore()
				.getFloat(PREFERENCES_FONT_SIZE));
		setContentProvider(new PropertiesContentProvider());
		setLabelProvider(new PropertiesLabelProvider(this));
		setCellModifier(new PropertyModifier(editor));

		editor.getSite().setSelectionProvider(this);

		getTable().setHeaderVisible(true);
		getTable().setLinesVisible(true);
		this.defaultLocale = defaultLocale;
		setSorter(sorter);
		this.createKeyColumn();
		this.createDefaultColumn();
		this.createInitialActions();
		this.createContextMenu();
		addFilter(new PropertyFilter());
	}

	public void setFontSize(float size) {
		if (size >= LocalizedPropertiesPlugin.getDefault().getPreferenceStore()
				.getFloat(PREFERENCES_FONT_MAX_SIZE)) {
			return;
		}
		if (size <= LocalizedPropertiesPlugin.getDefault().getPreferenceStore()
				.getFloat(PREFERENCES_FONT_MIN_SIZE)) {
			return;
		}
		Font font = getTable().getFont();
		FontData[] fontdata = font.getFontData();
		FontData data = new FontData();
		data.setName(fontdata[0].getName());
		data.setStyle(fontdata[0].getStyle());
		data.height = size;
		getTable().setFont(
				new Font(editor.getSite().getShell().getDisplay(), data));
		LocalizedPropertiesPlugin.getDefault().getPreferenceStore()
				.setValue(PREFERENCES_FONT_SIZE, size);
		for (int i = 0; i < getTable().getColumns().length; i++) {
			getTable().getColumn(i).pack();
		}
		this.updateEditorsFontSize(size);
	}

	private void updateEditorsFontSize(float size) {
		if (getCellEditors() == null) {
			return;
		}
		for (int i = 0; i < getTable().getColumns().length; i++) {
			getTable().getColumn(i).pack();
		}
	}

	public float getFontSize() {
		Font font = getTable().getFont();
		FontData[] fontdata = font.getFontData();
		FontData data = fontdata[0];
		return data.getHeight();
	}

	private void createKeyColumn() {
		TableColumn keyColumn = new TableColumn(getTable(), SWT.NONE);
		keyColumn.setText(LocalizedPropertiesMessages
				.getString(EDITOR_TABLE_KEY));
		keyColumn.setWidth(getColumnWidth(0));
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

		keyColumn.addControlListener(new ControlListener() {

			public void controlResized(ControlEvent event) {
				// FIXME: Is not perform save with every change
				// but I didn't found how to save, previously to dispose
				PropertyTableViewer.this.saveColumnsSize();
			}

			public void controlMoved(ControlEvent arg0) {
			}
		});

		List<CellEditor> editors = new LinkedList<CellEditor>();
		List<String> columnProperties = new LinkedList<String>();
		// Editor para la clave que es readonly
		PropertyTextEditor keyEditor = new PropertyTextEditor(getTable());
		keyEditor.setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				if (((String) value).trim().length() == 0) {
					return LocalizedPropertiesMessages
							.getString(EDITOR_TABLE_MODIFY_KEY_NULLVALUE);
				}
				return null;
			}
		});
		editors.add(keyEditor);
		columnProperties.add(PropertiesEditor.KEY_COLUMN_ID);
		setCellEditors(editors.toArray(new CellEditor[editors.size()]));
		setColumnProperties(columnProperties
				.toArray(new String[columnProperties.size()]));
	}

	public void editElement(java.lang.Object element, int column) {
		super.editElement(element, column);
	}

	public PropertiesEditor getEditor() {
		return this.editor;
	}

	public IAction getRemovePropertyAction() {
		return removePropertyAction;
	}

	private void createDefaultColumn() {
		defaultColumn = new TableColumn(getTable(), SWT.NONE);
		defaultColumn.setText(defaultLocale.toString());
		if (defaultLocale.toString().equals(
				StringUtils.getKeyLocale().toString())) {
			defaultColumn.setText(DEFAULT_TEXT);
		}
		defaultColumn.setWidth(getColumnWidth(1));
		defaultColumn.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				TableColumn col = (TableColumn) event.getSource();
				sorter.setColumn(1);
				sorter.setLocale(defaultLocale);
				getTable().setSortColumn(col);
				refresh();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		defaultColumn.addControlListener(new ControlListener() {

			public void controlResized(ControlEvent event) {
				// FIXME: Is not perform save with every change
				// but I didn't found how to save, previously to dispose
				PropertyTableViewer.this.saveColumnsSize();
			}

			public void controlMoved(ControlEvent arg0) {
			}
		});

		List<CellEditor> editors = new LinkedList<CellEditor>();
		editors.add(getCellEditors()[0]);
		List<String> columnProperties = new LinkedList<String>();
		columnProperties.add((String) getColumnProperties()[0]);

		// Creo la columna para el locale por default
		editors.add(new PropertyTextEditor(getTable()));
		columnProperties.add(defaultLocale.toString());

		setCellEditors(editors.toArray(new CellEditor[editors.size()]));
		setColumnProperties(columnProperties
				.toArray(new String[columnProperties.size()]));
	}

	private void createColumn(final Locale locale, final int index) {
		final TableColumn valueColumn = new TableColumn(getTable(), SWT.NONE);
		valueColumn.setText(locale.toString());
		if (locale.toString().equals(StringUtils.getKeyLocale().toString())) {
			valueColumn.setText(DEFAULT_TEXT);
		}
		valueColumn.setWidth(getColumnWidth(index));
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

		valueColumn.addControlListener(new ControlListener() {

			public void controlResized(ControlEvent event) {
				// FIXME: Is not perform save with every change
				// but I didn't found how to save, previously to dispose
				PropertyTableViewer.this.saveColumnsSize();
			}

			public void controlMoved(ControlEvent arg0) {
			}
		});
	}

	public Locale[] getLocales() {
		return locales;
	}

	public void setLocales(Locale[] locales) {
		this.locales = locales;
		this.getControl().getDisplay().syncExec(new Runnable() {
			public void run() {
				PropertyTableViewer.this.localesChanged();
			}
		});

	}

	public void dispose() {
		locales = null;
		mgr = null;
		defaultColumn = null;
		defaultLocale = null;
		editor = null;
		sorter = null;
	}

	private void localesChanged() {
		refresh(false);
		this.cleanColumns();
		List<CellEditor> editors = new LinkedList<CellEditor>(
				Arrays.asList(getCellEditors()));
		List<Object> columnProperties = new LinkedList<Object>(
				Arrays.asList(getColumnProperties()));

		for (int i = 0; i < locales.length; i++) {
			if (locales[i].equals(defaultLocale)) {
				continue;
			}
			createColumn(locales[i], i + 2);
			editors.add(new PropertyTextEditor(getTable()));
			columnProperties.add(locales[i].toString());
		}
		setCellEditors(editors.toArray(new CellEditor[editors.size()]));
		setColumnProperties(columnProperties
				.toArray(new String[columnProperties.size()]));
		refresh(true);
	}

	/**
	 * 
	 */
	private void cleanColumns() {
		/**
		 * SWT has a reported bug when you try to dispose s
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=258282#c16 This is
		 * because I used getTable().setRedraw(true/false) workaround
		 */
		getTable().setRedraw(false);
		while (getTable().getColumnCount() > 2) {
			getTable().getColumn(2).dispose();
		}
		this.cleanCellEditors();
		this.cleanColumnProperties();
		getTable().setRedraw(true);
	}

	private void cleanCellEditors() {
		List<CellEditor> cellEditors = new LinkedList<CellEditor>();
		cellEditors.add(getCellEditors()[0]);
		cellEditors.add(getCellEditors()[1]);
		setCellEditors(cellEditors.toArray(new CellEditor[2]));
	}

	private void cleanColumnProperties() {
		List<String> columnProperties = new LinkedList<String>();
		columnProperties.add((String) getColumnProperties()[0]);
		columnProperties.add((String) getColumnProperties()[1]);
		setColumnProperties(columnProperties.toArray(new String[2]));
	}

	public void saveColumnsSize() {
		List<String> sizesList = new LinkedList<String>();
		for (int i = 0; i < getTable().getColumnCount(); i++) {
			sizesList.add(String.valueOf(getTable().getColumn(i).getWidth()));
		}

		String sizes = StringUtils.join(
				sizesList.toArray(new String[sizesList.size()]), '|');
		LocalizedPropertiesPlugin.getDefault().getPreferenceStore()
				.setValue(PREFERENCES_COLUMNS_WIDTH, sizes);
	}

	private int getColumnWidth(int index) {
		String savedSizes = LocalizedPropertiesPlugin.getDefault()
				.getPreferenceStore().getString(PREFERENCES_COLUMNS_WIDTH);
		if (savedSizes.length() == 0) {
			return DEFAULT_COLUMN_SIZE;
		}
		String[] sizes = savedSizes.split("\\|");
		if (index > sizes.length - 1) {
			// Si la cantidad de columnas es menor
			// que la columna pedida, devuelve un default
			return DEFAULT_COLUMN_SIZE;
		}
		return Integer.valueOf(sizes[index]);
	}

	private void createInitialActions() {
		addKeyAction = new AddKeyAction(editor, this);
		removePropertyAction = new RemovePropertyAction(editor, this);
		copyKeyAction = new CopyKeyAction(editor, this);
		increaseFontAction = new IncreaseFontAction(this);
		decreaseFontAction = new DecreaseFontAction(this);
		copyProperty = new CopyPropertyAction(editor);
		pastProperty = new PastePropertyAction(editor);
		searchTextAction = new SearchTextAction(editor);
		removeSearchTextAction = new RemoveSearchTextAction(editor);
		setDefaultTextAction = new SetDefaultTextAction(editor, this);
	}

	private void createColumnActions(IMenuManager menuMgr) {
		menuMgr.add(new Separator());
		AddLocaleAction ala = new AddLocaleAction(editor, this);
		ala.setEnabled(true);
		menuMgr.add(ala);
		if (selectedColumn != null
				&& StringUtils.getLocale(selectedColumn.getText()) != null) {
			RemoveLocaleAction rla = new RemoveLocaleAction(editor, this,
					StringUtils.getLocale(selectedColumn.getText()));
			if (!selectedColumn.equals(defaultColumn)
					&& !selectedColumn.getText().equals(
							LocalizedPropertiesMessages
									.getString(EDITOR_TABLE_KEY))) {
				rla.setEnabled(true);
				menuMgr.add(rla);
			}
			setDefaultTextAction.setEnabled(true);
			setDefaultTextAction.setSelectedLocale(StringUtils.getLocale(selectedColumn.getText()));
		}
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
		int xOffseted = x;
		int yOffseted = y;
		Control currentControl = getTable();
		
		while(!(currentControl instanceof Shell)) {
			Rectangle bd = currentControl.getBounds();
			xOffseted -= bd.x;
			yOffseted -= bd.y;
			currentControl = currentControl.getParent();
		}
		
		Point point = new Point(xOffseted, yOffseted);
		
		ViewerCell cell = getCell(point);
		if (cell!=null) {
			int column = cell.getColumnIndex();
			return getTable().getColumn(column);
		}
		return null;
	}

	private void fillContextMenu(IMenuManager menuMgr) {
		addKeyAction.setEnabled(true);
		menuMgr.add(addKeyAction);

		boolean isEmpty = this.getSelection().isEmpty();

		// Activo o no las acciones que dependen de la seleccion
		copyKeyAction.setEnabled(!isEmpty);
		removePropertyAction.setEnabled(!isEmpty);
		copyProperty.setEnabled(!isEmpty);

		menuMgr.add(removePropertyAction);
		menuMgr.add(copyKeyAction);
		menuMgr.add(setDefaultTextAction);
		this.createColumnActions(menuMgr);
		menuMgr.add(new Separator());
		menuMgr.add(copyProperty);
		menuMgr.add(pastProperty);
		menuMgr.add(new Separator());
		menuMgr.add(increaseFontAction);
		menuMgr.add(decreaseFontAction);
		menuMgr.add(new Separator());
		menuMgr.add(searchTextAction);
		menuMgr.add(removeSearchTextAction);

		// menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

}
