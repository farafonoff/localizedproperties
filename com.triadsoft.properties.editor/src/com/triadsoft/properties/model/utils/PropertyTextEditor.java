package com.triadsoft.properties.model.utils;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * Text editor para las celdas de las propiedades
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 */
public class PropertyTextEditor extends TextCellEditor {
	public PropertyTextEditor(Composite composite) {
		super(composite);
		Table table = (Table) composite;
		setFontSize(table.getFont().getFontData()[0].getHeight());
	}

	private void setFontSize(int fontSize) {
		Font font = getControl().getFont();
		FontData fontData = font.getFontData()[0];
		fontData.setHeight(fontSize);
		getControl().setFont(new Font(getControl().getDisplay(), fontData));
	}
}
