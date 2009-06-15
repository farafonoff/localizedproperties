package com.triadsoft.properties.editors;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialogo para agregar una nueva clave al archivo de recursos
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * 
 */
public class AddKeyDialog extends Dialog {

	private Label label;
	private Text wildcardPath;
	private String newKey;

	public AddKeyDialog(Shell shell) {
		super(shell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		final Label description = new Label(area, SWT.NONE);
		final GridData layoutData = new GridData();
		description.setLayoutData(layoutData);
		// TODO:Traducir
		description.setText("Crear una nueva clave para el archivo de recurso");
		GridData gridData = new GridData();
		gridData.horizontalSpan = 1;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		label = new Label(area, SWT.NONE);
		// TODO:Traducir
		label.setText("Nueva clave");
		label.setLayoutData(gridData);
		wildcardPath = new Text(area, SWT.BORDER);
		wildcardPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		wildcardPath.setText("");
		wildcardPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				newKey = wildcardPath.getText();
				changeData();
			}
		});
		area.setLayout(gridLayout);
		return area;
	}

	private void changeData() {
		if (newKey != null && newKey.length() == 0) {
			newKey = null;
			return;
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		// TODO: i18n
		newShell.setText("Agregar nueva clave");
	}
	
	public String getNewKey() {
		return newKey;
	}
}
