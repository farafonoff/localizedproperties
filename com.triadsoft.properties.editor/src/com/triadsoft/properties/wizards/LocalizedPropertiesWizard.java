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
import org.eclipse.core.runtime.IPath;
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

public class LocalizedPropertiesWizard extends Wizard implements INewWizard {
	private LocalizedPropertiesPage page;
	private ISelection selection;

	/**
	 * Contructor por default
	 */
	public LocalizedPropertiesWizard() {
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
		final IPath filePath = page.getFilePath();
		final IResource resource = page.getResource();
		final String fileName = page.getFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(resource.getFullPath(), filePath, fileName,
							monitor);
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

	private void doFinish(IPath containerName, IPath filepath, String fileName,
			IProgressMonitor monitor) throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		String containerText = containerName.lastSegment();
		String fileContainer = filepath.segment(0);
		IPath filefullpath = new Path(containerName.toString() + filepath);
		if (containerText.equals(fileContainer)) {
			filefullpath = new Path(containerName.removeLastSegments(1).toString()+filepath);
		}
		IPath withoutFileName = filefullpath.removeLastSegments(1);
		LocalizedPropertiesWizard.createFullFilePath(withoutFileName);
		IResource resource = root.findMember(filefullpath.removeLastSegments(1));
		IContainer container = (IContainer) resource;
		final IFile file = container
				.getFile(new Path(filefullpath.lastSegment()));
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
		String contents = "#Default Category\n";
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

	public static void createFullFilePath(IPath path) {
		String[] segments = path.segments();
		IPath testPath = new Path("/" + segments[0]);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource res = root.findMember(testPath);
		for (int i = 1; i < segments.length; i++) {
			try {
				if (res == null || !res.exists()) {
					try {
						IFolder fol = root.getFolder(testPath);
						fol.create(false, false, null);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
				testPath = testPath.append("/" + segments[i]);
				res = root.findMember(testPath);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (res == null || !res.exists()) {
			try {
				IFolder fol = root.getFolder(testPath);
				fol.create(false, false, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
}