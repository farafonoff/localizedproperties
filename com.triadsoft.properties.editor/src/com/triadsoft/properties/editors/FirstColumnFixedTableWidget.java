package com.triadsoft.properties.editors;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;

public class FirstColumnFixedTableWidget extends Composite {
	Table leftTable;
	Table rightTable;
	
	void setLeft(Table view) {
		leftTable = view;
		leftTable.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, true));
		leftTable.setSize(300, 500);
		ScrollBar vBarLeft = leftTable.getVerticalBar();
	    vBarLeft.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	    	  if (rightTable!=null)
	    		  	rightTable.setTopIndex(leftTable.getTopIndex());
	      }
	    });
	    vBarLeft.setVisible(true);
	}
	
	void setRight(Table view) {
		rightTable = view;
		GridData table2Data = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2);
	    rightTable.setLayoutData(table2Data);
	    ScrollBar vBarRight = rightTable.getVerticalBar();
	    vBarRight.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event event) {
	    	  if (leftTable!=null)
	    		  leftTable.setTopIndex(rightTable.getTopIndex());
	      }
	    });
	    vBarRight.setVisible(true);
	}

	public FirstColumnFixedTableWidget(Composite parent, int style) {
		super(parent, style);
	    GridLayout layout = new GridLayout(2, true);
	    layout.marginWidth = layout.marginHeight = layout.horizontalSpacing = 0;
	    setLayout(layout);
	}

}
