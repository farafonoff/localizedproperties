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
import com.triadsoft.properties.editors.actions.CutPropertyAction;
import com.triadsoft.properties.editors.actions.DecreaseFontAction;
import com.triadsoft.properties.editors.actions.IncreaseFontAction;
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

	private CopyPropertyAction copyAction = null;

	private PastePropertyAction pasteAction = null;

	private CutPropertyAction cutAction = null;

	private IncreaseFontAction increaseFontAction = null;

	private DecreaseFontAction decreaseFontAction = null;

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
			//cutAction.setRemoveAction(removeAction);

//			copyAction.setEditor(editor);
//			pasteAction.setEditor(editor);
//			cutAction.setEditor(editor);
			increaseFontAction.setEditor(editor);
			decreaseFontAction.setEditor(editor);

//			getActionBars().setGlobalActionHandler(
//					ITextEditorActionConstants.CUT, cutAction);
//			getActionBars().setGlobalActionHandler(
//					ITextEditorActionConstants.COPY, copyAction);
//			getActionBars().setGlobalActionHandler(
//					ITextEditorActionConstants.PASTE, pasteAction);

			getActionBars().setGlobalActionHandler("increaseFont",
					increaseFontAction);
			getActionBars().setGlobalActionHandler("decreaseFont",
					decreaseFontAction);

			getActionBars().updateActionBars();
		}
	}

	public void setActivePage(IEditorPart part) {
	}

	private void createActions() {
//		if (copyAction == null) {
//			copyAction = new CopyPropertyAction(null);
//		}
//
//		if (pasteAction == null) {
//			pasteAction = new PastePropertyAction(null);
//		}

//		if (cutAction == null) {
//			cutAction = new CutPropertyAction(copyAction, null);
//		}

		if (increaseFontAction == null) {
			increaseFontAction = new IncreaseFontAction(null);
		}

		if (decreaseFontAction == null) {
			decreaseFontAction = new DecreaseFontAction(null);
		}
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
