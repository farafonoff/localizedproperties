package com.triadsoft.properties.editors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import com.triadsoft.properties.editor.extensions.IPropertiesExport;
import com.triadsoft.properties.editor.extensions.IPropertiesImport;
import com.triadsoft.properties.editors.actions.CopyPropertyAction;
import com.triadsoft.properties.editors.actions.CutPropertyAction;
import com.triadsoft.properties.editors.actions.DecreaseFontAction;
import com.triadsoft.properties.editors.actions.ExtensionIEAction;
import com.triadsoft.properties.editors.actions.IncreaseFontAction;
import com.triadsoft.properties.editors.actions.PastePropertyAction;
import com.triadsoft.properties.editors.actions.RemoveSearchTextAction;
import com.triadsoft.properties.editors.actions.SearchTextAction;

/**
 * Controla la instalacion/desinstalacion de las acciones globales para los
 * editores multi-pagina. Responsable de la redireccion de las acctiones
 * globales para el editor activo.
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class MultiPageEditorContributor extends
		MultiPageEditorActionBarContributor {

	public static final String IPROPERTIES_EXPORT_ID = "com.triadsoft.properties.editor.export";
	public static final String IPROPERTIES_IMPORT_ID = "com.triadsoft.properties.editor.import";

	private CopyPropertyAction copyAction = null;

	private PastePropertyAction pasteAction = null;

	private CutPropertyAction cutAction = null;

	private IncreaseFontAction increaseFontAction = null;

	private DecreaseFontAction decreaseFontAction = null;

	private SearchTextAction searchTextAction = null;

	private RemoveSearchTextAction removeSearchTextAction = null;

	private static final List<ExtensionIEAction> importers = new LinkedList<ExtensionIEAction>();
	private static final List<ExtensionIEAction> exporters = new LinkedList<ExtensionIEAction>();

	// private IAction globalCopy = null;

	/**
	 * Constructor por default
	 */
	public MultiPageEditorContributor() {
		super();
		createActions();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
	}

	/**
	 * Devuelve la accion registrada para el editor de texto dado
	 * 
	 * @return IAction o nulo si el editor es nulo.
	 */
	protected IAction getAction(IEditorPart editor, String actionID) {
		return (editor == null ? null : ((PropertiesEditor) editor)
				.getTableViewerAction(actionID));
	}

	public void setActiveEditor(IEditorPart part) {
		PropertiesEditor editor = ((PropertiesEditor) part);
		if (getActionBars() != null && editor != null) {
			getActionBars().setGlobalActionHandler(
					ITextEditorActionConstants.DELETE,
					getAction(part, ITextEditorActionConstants.DELETE));
			IAction removeAction = getAction(part,
					ITextEditorActionConstants.CUT);
			cutAction.setRemoveAction(removeAction);

			copyAction.setEditor(editor);
			pasteAction.setEditor(editor);
			cutAction.setEditor(editor);
			increaseFontAction.setEditor(editor);
			decreaseFontAction.setEditor(editor);

			searchTextAction.setEditor(editor);
			removeSearchTextAction.setEditor(editor);

			getActionBars().setGlobalActionHandler(
					ITextEditorActionConstants.CUT, cutAction);
			getActionBars().setGlobalActionHandler(
					ITextEditorActionConstants.COPY, copyAction);
			getActionBars().setGlobalActionHandler(
					ITextEditorActionConstants.PASTE, pasteAction);

			getActionBars().setGlobalActionHandler("increaseFont",
					increaseFontAction);
			getActionBars().setGlobalActionHandler("decreaseFont",
					decreaseFontAction);

			getActionBars().setGlobalActionHandler(
					ITextEditorActionConstants.FIND, searchTextAction);

			getActionBars().updateActionBars();
		}
	}

	public void setActivePage(IEditorPart part) {
	}

	private void createActions() {
		if (copyAction == null) {
			copyAction = new CopyPropertyAction(null);
		}

		if (pasteAction == null) {
			pasteAction = new PastePropertyAction(null);
		}

		if (cutAction == null) {
			cutAction = new CutPropertyAction(copyAction, null);
		}

		if (increaseFontAction == null) {
			increaseFontAction = new IncreaseFontAction(null);
		}

		if (decreaseFontAction == null) {
			decreaseFontAction = new DecreaseFontAction(null);
		}

		if (searchTextAction == null) {
			searchTextAction = new SearchTextAction(null);
		}
		if (removeSearchTextAction == null) {
			removeSearchTextAction = new RemoveSearchTextAction(null);
		}
		this.createImportExtensions();
		this.createExportExtensions();
	}

	public void contributeToMenu(IMenuManager manager) {
		// IMenuManager menu = new MenuManager("Editor &Menu");
		// manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		// retargetRemoveAction.setEnabled(true);
		// menu.add(retargetRemoveAction);
	}

	public void contributeToToolBar(IToolBarManager manager) {
		// manager.add(new Separator());
		// manager.add(retargetRemoveAction);
	}

	private void createExportExtensions() {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(IPROPERTIES_EXPORT_ID);
		try {
			for (IConfigurationElement e : config) {
				System.out.println("Evaluating extension");
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IPropertiesExport) {
					ISafeRunnable runnable = new ISafeRunnable() {
						public void run() throws Exception {
							System.out.println("The name is "
									+ ((IPropertiesExport) o).getName());
							System.out.println("And the description is "
									+ ((IPropertiesExport) o).getDescription());
							ExtensionIEAction action = new ExtensionIEAction(
									null, (IPropertiesExport) o);
							exporters.add(action);
						}

						public void handleException(Throwable exception) {
							// FIXME Make better exception
							System.out.println("Exception in client");
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void createImportExtensions() {
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(IPROPERTIES_IMPORT_ID);
		try {
			for (IConfigurationElement e : config) {
				System.out.println("Evaluating import extension");
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IPropertiesImport) {
					ISafeRunnable runnable = new ISafeRunnable() {
						public void run() throws Exception {
							System.out.println("The name is "
									+ ((IPropertiesImport) o).getName());
							System.out.println("And the description is "
									+ ((IPropertiesImport) o).getDescription());
							ExtensionIEAction action = new ExtensionIEAction(
									null, (IPropertiesImport) o);
							importers.add(action);
						}

						public void handleException(Throwable exception) {
							// FIXME Make better exception
							System.out.println("Exception in client");
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void setEditor(){
		
	}
}
