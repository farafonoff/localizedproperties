package com.triadsoft.properties.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.StyleRange;

import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTableViewer;
import com.triadsoft.properties.model.utils.SearchUtils;

/**
 * Provider para las columnas de PropertyTableViewer Tiene como funcion mostrar
 * las etiquetas e imagenes segun el contenido de cada columna. En el caso en
 * que la columna no tenga valor muestra al costado de misma un icono de warning
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @see PropertyTableViewer
 * 
 */
public class PropertiesLabelProvider extends StyledCellLabelProvider implements
		ITableLabelProvider {

	ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(this
			.getClass(), "/icons/8x8/warning.png");

	private TableViewer viewer;
	private String searchText;
	private Color systemColor;

	public PropertiesLabelProvider(TableViewer viewer) {
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
	 * celda no tenga valor, devolverá un icono de warning, para indicar que en
	 * esa cerda no hay valor
	 * 
	 * @param obj
	 *            Objeto que se está dibujando, en éste caso es un objeto del
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
		Locale locale = getLocale((String) viewer.getColumnProperties()[index]);
		if (property.getError(locale) != null) {
			return imageDescriptor.createImage();
		}
		return null;
	}

	/**
	 * Devuelve el contenido de la celda en formato de texto, segun la columna a
	 * mostra indicada por el valor de index. Para la primera columna mostrará
	 * la clave de la propiedad, y la las columnas siguientes mostrará el valor
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
			return property.getValue(getLocale((String) viewer
					.getColumnProperties()[index]));
		}
	}

	@Override
	public void update(ViewerCell cell) {
		Property element = (Property) cell.getElement();
		int index = cell.getColumnIndex();
		String columnText = getColumnText(element, index);
		cell.setText(columnText);
		cell.setImage(getColumnImage(element, index));
		if (searchText != null && searchText.length() > 0) {
			int intRangesCorrectSize[] = SearchUtils.getSearchTermOccurrences(
					searchText, columnText);
			List<StyleRange> styleRange = new ArrayList<StyleRange>();
			for (int i = 0; i < intRangesCorrectSize.length / 2; i++) {
				StyleRange myStyleRange = new StyleRange(0, 0, null,
						systemColor);
				myStyleRange.start = intRangesCorrectSize[i];
				myStyleRange.length = intRangesCorrectSize[++i];
				styleRange.add(myStyleRange);
			}
			cell.setStyleRanges(styleRange.toArray(new StyleRange[styleRange
					.size()]));
		} else {
			cell.setStyleRanges(null);
		}
		super.update(cell);
	}

	/**
	 * Este metode devuelve el locale a partir del nombre de la columna. Cada
	 * columna se agrega en las columnsProperties del viewer, en la forma
	 * {lang}_{country}
	 * 
	 * @param localeString
	 *            String de la columna con el locale parseado
	 * @return
	 */
	private Locale getLocale(String localeString) {
		String[] loc = localeString.split("_");
		if (loc.length == 1) {
			return new Locale(loc[0]);
		}
		return new Locale(loc[0], loc[1]);
	}

	public void addListener(ILabelProviderListener arg0) {

	}

	public void dispose() {
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
