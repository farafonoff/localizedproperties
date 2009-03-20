package com.triadsoft.properties.editors;

import java.util.LinkedList;
import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.triadsoft.properties.model.ResourceList;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class PropertiesEditor extends MultiPageEditorPart implements
		IResourceChangeListener {

	protected static final String KEY_COLUMN_ID = "key_column";
	protected static final String ES_AR_COLUMN_ID = "es_AR";
	protected static final String EN_US_COLUMN_ID = "en_US";

	/** The text editor used in page 0. */
	private TableViewer tableViewer;

	private TextEditor textEditor;

	/** The text widget used in page 2. */
	private StyledText text;

	private ResourceList resource;

	private boolean isModified;

	/**
	 * Creates a multi-page editor example.
	 */
	public PropertiesEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);

	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	void createPage0() {
		createTable();
		int index = addPage(tableViewer.getControl());
		setPageText(index, "Properties");
	}

	private void createTable() {
		tableViewer = new TableViewer(getContainer(), SWT.SINGLE
				| SWT.FULL_SELECTION);
		tableViewer.setContentProvider(new PropertiesContentProvider());
		tableViewer.setLabelProvider(new PropertiesLabelProvider());

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn keyColumn = new TableColumn(table, SWT.NONE);
		keyColumn.setText("Clave");
		keyColumn.setWidth(150);
		Locale[] locales = resource.getLocales();
		LinkedList<String> columnsNamesList = new LinkedList<String>();
		columnsNamesList.add(KEY_COLUMN_ID);

		for (int i = 0; i < locales.length; i++) {
			TableColumn valueColumn = new TableColumn(table, SWT.NONE);
			valueColumn.setText(locales[i].toString());
			valueColumn.setWidth(200);
			columnsNamesList.add(locales[i].toString());
		}
		tableViewer.setInput(resource);

		tableViewer.setColumnProperties((String[]) columnsNamesList
				.toArray(new String[columnsNamesList.size()]));

		final CellEditor[] cellEditors = new CellEditor[3];
		cellEditors[0] = new TextCellEditor(tableViewer.getTable(),
				SWT.READ_ONLY);
		cellEditors[1] = new TextCellEditor(tableViewer.getTable());
		cellEditors[2] = new TextCellEditor(tableViewer.getTable());

		tableViewer.setCellEditors(cellEditors);
		tableViewer.setCellModifier(new PropertyModifier(tableViewer));
	}

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the
	 * font used in page 2.
	 */
	void createPage1() {
		try {
			textEditor = new TextEditor();
			int index = addPage(textEditor, getEditorInput());
			setPageText(index, "Source");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates page 2 of the multi-page editor, which shows the sorted text.
	 */
	void createPage2() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);

		int index = addPage(composite);
		setPageText(index, "Preview");
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		resource = new ResourceList(file);
		createPage0();
		createPage1();
		setTitle(resource.getFileName());
		// createPage2();
	}

	protected void tableChanged() {
		isModified = true;
		if (!super.isDirty()) {
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	@Override
	protected void handlePropertyChange(int propertyId) {
		if (propertyId == IEditorPart.PROP_DIRTY) {
			isModified = isDirty();
		}
		super.handlePropertyChange(propertyId);
	}

	@Override
	public boolean isDirty() {
		return isModified || super.isDirty();
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	void updateTitle() {
		IEditorInput input = getEditorInput();
		// setTitle(input.getName());
		setTitleToolTip(input.getToolTipText());
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		// getEditor(0).doSave(monitor);
		resource.save();
		isModified = false;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	public void doSaveAs() {
		// IEditorPart editor = getEditor(0);
		// editor.doSaveAs();
		// setPageText(0, editor.getTitle());
		// setInput(editor.getEditorInput());
		// updateTitle();
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	public void setFocus() {
		switch (getActivePage()) {
		case 0:
			tableViewer.getTable().setFocus();
			break;
		case 1:
			textEditor.setFocus();
			break;
		}

	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		// Codigo aplicado a la pagina seleccionada
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					// IWorkbenchPage[] pages = getSite().getWorkbenchWindow()
					// .getPages();
					// for (int i = 0; i < pages.length; i++) {
					// if (((FileEditorInput) editor.getEditorInput())
					// .getFile().getProject().equals(
					// event.getResource())) {
					// IEditorPart editorPart = pages[i].findEditor(editor
					// .getEditorInput());
					// pages[i].closeEditor(editorPart, true);
					// }
					// }
				}
			});
		}
	}
}
