package com.triadsoft.properties.editors;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class PropertiesEditorContributor extends EditorActionBarContributor {
	private static final String[] WORKBENCH_ACTIONS_ID = { IWorkbenchActionConstants.CUT };
	private static final String[] EDITOR_ACTIONS_ID = {
			ITextEditorActionConstants.CUT, ITextEditorActionConstants.CUT_LINE };

	public void setActiveCellEditor(IEditorPart part) {
		PropertiesEditor editor = (PropertiesEditor) part;
		//No puedo obtener la pagina activa desde el editor, porque es 
		//setActivePage(editor, editor.getActivePage());
		setActivePage(editor, 0);
	}

	public void setActivePage(PropertiesEditor editor, int pageIndex) {
		IActionBars actionBars = getActionBars();
		if (actionBars != null) {
			switch (pageIndex) {
			case 0:
				hookGlobalTreeAction(editor, actionBars);
				break;
			}
		}
	}

	private void hookGlobalTreeAction(PropertiesEditor editor,
			IActionBars actionBars) {
		for (int i = 0; i < WORKBENCH_ACTIONS_ID.length; i++) {
			actionBars.setGlobalActionHandler(WORKBENCH_ACTIONS_ID[i], editor
					.getTableViewerAction(WORKBENCH_ACTIONS_ID[i]));

		}
	}
}
