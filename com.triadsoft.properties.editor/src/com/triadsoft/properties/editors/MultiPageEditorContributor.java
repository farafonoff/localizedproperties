package com.triadsoft.properties.editors;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import com.triadsoft.properties.editors.actions.CopyPropertyAction;
import com.triadsoft.properties.editors.actions.PastePropertyAction;

/**
 * Controla la instalacion/desinstalacion de las acciones globales para los
 * editores multi-pagina. Responsable de la redireccion de las acctiones
 * globales para el editor activo.
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class MultiPageEditorContributor extends
		MultiPageEditorActionBarContributor {
	private IEditorPart activeEditorPart;

	// private static final String[] WORKBENCH_ACTIONS_ID = {
	// IWorkbenchActionConstants.CUT };
	// private static final String[] EDITOR_ACTIONS_ID = {
	// ITextEditorActionConstants.CUT, ITextEditorActionConstants.CUT_LINE };

	private CopyPropertyAction copyAction = null;

	private PastePropertyAction pasteAction = null;

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
		IActionBars actionBars = getActionBars();
		PropertiesEditor editor = ((PropertiesEditor) part);
		if (actionBars != null && editor != null) {
			actionBars = editor.getEditorSite().getActionBars();
			actionBars.setGlobalActionHandler(ITextEditorActionConstants.CUT,
					getAction(part, ITextEditorActionConstants.CUT));
			actionBars.setGlobalActionHandler(
					ITextEditorActionConstants.DELETE, getAction(part,
							ITextEditorActionConstants.DELETE));
			if (copyAction == null) {
				copyAction = new CopyPropertyAction();
			}

			if (pasteAction == null) {
				pasteAction = new PastePropertyAction();
			}
			copyAction.setEditor(editor);
			pasteAction.setEditor(editor);

			getActionBars().setGlobalActionHandler(
					ITextEditorActionConstants.COPY, copyAction);
			getActionBars().setGlobalActionHandler(
					ITextEditorActionConstants.PASTE, pasteAction);
			actionBars.updateActionBars();
		}
	}

	public void setActivePage(IEditorPart part) {
		// if (activeEditorPart == part)
		// return;
		//
		// activeEditorPart = part;
		//
		// IActionBars actionBars = getActionBars();
		// PropertiesEditor editor = ((PropertiesEditor) part);
		// if (actionBars != null && editor != null) {
		// actionBars = editor.getEditorSite().getActionBars();
		// actionBars.setGlobalActionHandler(ITextEditorActionConstants.CUT,
		// getAction(part, ITextEditorActionConstants.CUT));
		// actionBars.setGlobalActionHandler(
		// ITextEditorActionConstants.DELETE, getAction(part,
		// ITextEditorActionConstants.DELETE));
		// if (copyAction == null) {
		// copyAction = new CopyPropertyAction();
		// }
		//
		// if (pasteAction == null) {
		// pasteAction = new PastePropertyAction();
		// }
		// copyAction.setEditor(editor);
		// pasteAction.setEditor(editor);
		//
		// getActionBars().setGlobalActionHandler(
		// ITextEditorActionConstants.COPY, copyAction);
		// getActionBars().setGlobalActionHandler(
		// ITextEditorActionConstants.PASTE, pasteAction);
		// actionBars.updateActionBars();
		// }
	}

	private void createActions() {
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
}
