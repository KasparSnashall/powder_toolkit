package uk.ac.diamond.powder_toolkit.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The main perspective for the program
 * 
 * @author sfz19839
 *
 */
public class PowderToolkit implements IPerspectiveFactory {

	/**
	 * Creates the initial layout for a page.
	 */
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);

		addFastViews(layout);
		addViewShortcuts(layout);
		addPerspectiveShortcuts(layout);
		{
			IFolderLayout folderLayout = layout.createFolder("Powder folder",
					IPageLayout.TOP, 0.84f, IPageLayout.ID_EDITOR_AREA);
			folderLayout.addView("uk.ac.diamond.powder_toolkit.views.Loadview");
			folderLayout
					.addView("org.dawb.workbench.plotting.views.toolPageView.1D");
			folderLayout
					.addView("uk.ac.diamond.powder_toolkit.views.Indexview");
			folderLayout
					.addView("uk.ac.diamond.powder_toolkit.views.Compareview");
		}
		{
			IFolderLayout folderLayout = layout.createFolder("folder",
					IPageLayout.RIGHT, 0.85f, "Powder folder");
			folderLayout
					.addView("uk.ac.diamond.powder_toolkit.views.LoadedDataview");
		}
		layout.addView("uk.ac.diamond.powder_toolkit.views.Plotview",
				IPageLayout.TOP, 0.38f, "Powder folder");
	}

	/**
	 * Add fast views to the perspective.
	 */
	private void addFastViews(IPageLayout layout) {
	}

	/**
	 * Add view shortcuts to the perspective.
	 */
	private void addViewShortcuts(IPageLayout layout) {
	}

	/**
	 * Add perspective shortcuts to the perspective.
	 */
	private void addPerspectiveShortcuts(IPageLayout layout) {
	}

}
