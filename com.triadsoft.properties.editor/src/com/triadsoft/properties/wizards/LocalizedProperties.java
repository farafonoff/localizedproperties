package com.triadsoft.properties.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * Wizard para poder crear un archivo de recursos internacionalizados
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */

public class LocalizedProperties extends Wizard implements INewWizard {
	private LocalizedPropertiesPage page;
	private ISelection selection;

	/**
	 * Contructor por default
	 */
	public LocalizedProperties() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * Agrega la pagina al wizard
	 */

	public void addPages() {
		page = new LocalizedPropertiesPage(selection);
		addPage(page);
	}

	/**
	 * Éste metodo es llamado cuando se presiona el botón finalizar del wizard
	 */
	public boolean performFinish() {
		final String filepath = page.getFilePath().substring(0,
				page.getFilePath().lastIndexOf("/"));
		final String containerName = page.getContainerName();
		final String fileName = page.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(containerName, filepath, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private void doFinish(String containerName, String filepath,
			String fileName, IProgressMonitor monitor) throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root
				.findMember(new Path(containerName + filepath));
		IResource containerFolder = root.findMember(new Path(containerName));
		if (resource == null || !resource.exists()
				|| !(resource instanceof IContainer)) {
			IFolder folder = ((IContainer) containerFolder).getFolder(new Path(
					filepath));
			folder.create(false, false, monitor);
			resource = root.findMember(new Path(containerName + filepath));
			// throwCoreException("Container \"" + containerName +
			// "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container
				.getFile(new Path(fileName + ".properties"));
		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					IDE
							.openEditor(page, file,
									"com.triadsoft.properties.editors.PropertiesEditor");
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}

	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream() {
		String contents = "#Default Category";
		return new ByteArrayInputStream(contents.getBytes());
	}

	@SuppressWarnings("all")
	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "com.triadsoft.properties",
				IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}