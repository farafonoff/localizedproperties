package com.triadsoft.properties.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTableViewer;
import com.triadsoft.properties.model.utils.SearchUtils;
import com.triadsoft.properties.model.utils.StringUtils;

/**
 * Provider para las columnas de PropertyTableViewer Tiene como funcion mostrar
 * las etiquetas e imagenes segun el contenido de cada columna. En el caso en
 * que la columna no tenga valor muestra al costado de misma un icono de warning
 * TODO: Translate
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @see PropertyTableViewer
 * 
 */
public class PropertiesLabelProvider extends StyledCellLabelProvider implements
		ITableLabelProvider {

	ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/8x8/warning.png");

	private TableViewer viewer;
	private String searchText;
	private Color systemColor;

	public PropertiesLabelProvider(TableViewer viewer) {
		super();
		this.viewer = viewer;
		systemColor = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
	}

	/**
	 * Establece el valor del texto a buscar
	 * 
	 * @param searchText
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * Devuelve una imagen segun el contenido de la columna. En caso que la
	 * celda no tenga valor, devolver� un icono de warning, para indicar que en
	 * esa cerda no hay valor
	 * 
	 * @param obj
	 *            Objeto que se est� dibujando, en �ste caso es un objeto del
	 *            tipo property
	 * @param index
	 *            indice de la columna a mostrar.Se numera a partir de cero
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
	 *      int)
	 */
	public Image getColumnImage(Object obj, int index) {
		Property property = (Property) obj;
		if (index == 0) {
			return null;
		}
		Locale locale = StringUtils.getLocale((String) viewer
				.getColumnProperties()[index]);
		if (property.getError(locale) != null) {
			return imageDescriptor.createImage();
		}
		return null;
	}

	/**
	 * Devuelve el contenido de la celda en formato de texto, segun la columna a
	 * mostra indicada por el valor de index. Para la primera columna mostrar�
	 * la clave de la propiedad, y la las columnas siguientes mostrar� el valor
	 * de la propiedad para cada locale
	 * 
	 * @param obj
	 *            Objeto Property para dibujar
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
	 *      int)
	 * @see Property
	 */
	public String getColumnText(Object obj, int index) {
		Property property = (Property) obj;
		if (index == 0) {
			return property.getKey();
		} else {
			return property.getValue(StringUtils.getLocale((String) viewer
					.getColumnProperties()[index]));
		}
	}

	@Override
	protected StyleRange prepareStyleRange(StyleRange styleRange,
			boolean applyColors) {
		// TODO Auto-generated method stub
		return super.prepareStyleRange(styleRange, true);
	}

	@Override
	public void update(ViewerCell cell) {
		Property element = (Property) cell.getElement();
		int index = cell.getColumnIndex();
		String columnText = getColumnText(element, index);
		cell.setText(columnText);
		cell.setImage(getColumnImage(element, index));

		Pattern p = Pattern.compile("\\{\\d{1,}\\}");
		Matcher m = p.matcher(columnText);
		StyledString text = new StyledString();
		List<StyleRange> ranges = new ArrayList<StyleRange>();
		while (m.find()) {
			System.out.println(m.start() + " " + (m.end() - m.start()));
			StyleRange myStyledRange = new StyleRange(m.start(), m.end()
					- m.start(), null, Display.getCurrent().getSystemColor(
					SWT.COLOR_DARK_RED));
			ranges.add(myStyledRange);
		}
		text.append(columnText, StyledString.DECORATIONS_STYLER);
		cell.setText(text.toString());

		/*
		 * 
		 * text.append("This is a test", StyledString.DECORATIONS_STYLER);
		 * text.append(" (" + 15 + ") ", StyledString.DECORATIONS_STYLER);
		 * cell.setText(text.toString());
		 * 
		 * StyleRange[] range = { myStyledRange }; cell.setStyleRanges(range);
		 * super.update(cell);
		 */

		// if (searchText != null && searchText.length() > 0) {
		// int intRangesCorrectSize[] = SearchUtils.getSearchTermOccurrences(
		// searchText, columnText);
		// List<StyleRange> styleRange = new ArrayList<StyleRange>();
		// for (int i = 0; i < intRangesCorrectSize.length / 2; i++) {
		// StyleRange myStyleRange = new StyleRange(0, 0, null,
		// systemColor);
		// myStyleRange.start = intRangesCorrectSize[i];
		// myStyleRange.length = intRangesCorrectSize[++i];
		// styleRange.add(myStyleRange);
		// }
		// cell.setStyleRanges(styleRange.toArray(new StyleRange[styleRange
		// .size()]));
		// } else {
		// cell.setStyleRanges(null);
		// }

		super.update(cell);
	}

	public void addListener(ILabelProviderListener arg0) {

	}

	public void dispose() {
		super.dispose();
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
	 *      java.lang.String)
	 */
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener arg0) {

	}
}
