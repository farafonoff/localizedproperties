package com.triadsoft.properties.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
	private Label label;
	private Text wildcardPath;

	public WilcardPathDialog(Shell parent) {
		super(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FormLayout());
		final Label description = new Label(area, SWT.NONE);
		final GridData layoutData = new GridData();
		description.setLayoutData(layoutData);
		description.setText("Crear un nuevo wildcard path");
		GridData gridData = new GridData();
		gridData.horizontalSpan = 1;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		label = new Label(area, SWT.NONE);
		label.setText("WildcardPath");
		label.setLayoutData(gridData);
		wildcardPath = new Text(area, SWT.BORDER);
		wildcardPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		wildcardPath.setText("{root}");
		wildcardPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				_wildcardPath = wildcardPath.getText();
			}
		});
		area.setLayout(gridLayout);
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
