package com.triadsoft.properties.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.triadsoft.properties.editors.PropertiesEditor;

/**
 * Accion que permite el cut & paste
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * 
 */
public class CutPropertyAction extends Action {
	private CopyPropertyAction copyPropertyAction;
	private RemovePropertyAction removePropertyAction;

	/**
	 * @param copyPropertyAction
	 * @param removePropertyAction
	 */
	public CutPropertyAction(CopyPropertyAction copyPropertyAction,
			RemovePropertyAction removePropertyAction) {

		// TODO: Ver si hace falta el texto
		super("");
		this.copyPropertyAction = copyPropertyAction;
		this.removePropertyAction = removePropertyAction;
	}

	public void setEditor(PropertiesEditor editor) {
		if (copyPropertyAction != null) {
			this.copyPropertyAction.setEditor(editor);
		}
		if (removePropertyAction != null) {
			this.removePropertyAction.setEditor(editor);
		}
	}

	public void setCopyAction(IAction action) {
		this.copyPropertyAction = (CopyPropertyAction) action;
	}

	public void setRemoveAction(IAction action) {
		this.removePropertyAction = (RemovePropertyAction) action;
	}

	@Override
	public void run() {
		if (copyPropertyAction != null) {
			copyPropertyAction.run();
		}
		if (removePropertyAction != null) {
			removePropertyAction.run();
		}
	}
}
