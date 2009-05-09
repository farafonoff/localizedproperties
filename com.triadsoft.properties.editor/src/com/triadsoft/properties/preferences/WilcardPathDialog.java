package com.triadsoft.properties.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class WilcardPathDialog extends Dialog {

	private String _wildcardPath = null;

	public WilcardPathDialog(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FormLayout());
		final Label description = new Label(area, SWT.NONE);
		final FormData formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 5);
		description.setLayoutData(formData);
		description.setText("Crear un nuevo wildcard path");

		final Composite composite = new Composite(area, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		composite.setLayout(gridLayout);
		final FormData formData1 = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 5, SWT.TOP);
		composite.setLayoutData(formData1);

		final Text wildcardpath = new Text(composite, SWT.BORDER);
		wildcardpath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		wildcardpath.setText("{root}");
		wildcardpath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				_wildcardPath = wildcardpath.getText();
			}
		});
		return area;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Agregar WildcardPath");
	}

	public String getWildcardPath() {
		return _wildcardPath;
	}
}
