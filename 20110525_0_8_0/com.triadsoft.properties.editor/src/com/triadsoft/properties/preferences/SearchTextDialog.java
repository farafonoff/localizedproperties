package com.triadsoft.properties.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.model.utils.WildCardPath2;

/**
 * <p>
 * Dialogo para poder filtrar un texto
 * </p>
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @see WildCardPath2
 */
public class SearchTextDialog extends Dialog {

	protected static final String ACTION_ADD_SEARCH_UPPER_LOWER_LABEL = "action.add.search.upperLower.label";
	protected static final String ACTION_ADD_SEARCH_DIALOG_LABEL = "action.add.search.dialog.label";
	private String _searchedText = null;
	private Text searchText;
	private boolean _matchCase;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/find.png");

	public SearchTextDialog(Shell parent) {
		super(parent);
		setDefaultImage(imageDescriptor.createImage());
	}

	@Override
	public int open() {
		return super.open();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		final GridData layoutData = new GridData();
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		gridData.heightHint = 20;

		final Label description = new Label(area, SWT.NONE);
		description.setLayoutData(layoutData);
		description.setText(LocalizedPropertiesPlugin
				.getString(ACTION_ADD_SEARCH_DIALOG_LABEL));
		description.setLayoutData(gridData);

		searchText = new Text(area, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(gridData);
		searchText.setText(_searchedText);
		searchText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				_searchedText = searchText.getText();
			}
		});
		final Button matchCase = new Button(area, SWT.CHECK);
		matchCase.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent arg0) {
				_matchCase = matchCase.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});
		matchCase.setSelection(_matchCase);
		Label matchCaseLabel = new Label(area, SWT.NONE);
		matchCaseLabel.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent arg0) {
				matchCase.setSelection(!matchCase.getSelection());
				_matchCase = matchCase.getSelection();
			}

			public void mouseDown(MouseEvent arg0) {

			}

			public void mouseDoubleClick(MouseEvent arg0) {

			}
		});
		matchCaseLabel.setText(LocalizedPropertiesPlugin
				.getString(ACTION_ADD_SEARCH_UPPER_LOWER_LABEL));
		area.setLayout(gridLayout);
		return area;
	}

	public String getSearchText() {
		if (_searchedText == null || _searchedText.length() == 0) {
			return null;
		}
		return _searchedText;
	}

	public void setSearchText(String searchText) {
		if (searchText == null || searchText.length() == 0) {
			this._searchedText = "";
			return;
		}
		this._searchedText = searchText;
	}

	public boolean getMatchCase() {
		return _matchCase;
	}

	public void setMatchCase(boolean matchCase) {
		this._matchCase = matchCase;
	}
}
