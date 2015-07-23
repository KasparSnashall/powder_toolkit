package perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IFolderLayout;

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
			IFolderLayout folderLayout = layout.createFolder("Powder folder", IPageLayout.LEFT, 0.76f, IPageLayout.ID_EDITOR_AREA);
			folderLayout.addView("Views.Loadview");
			folderLayout.addView("Views.Peakview");
			folderLayout.addView("Views.Indexview");
			folderLayout.addView("Views.Compareview");
			folderLayout.addView("Views.Searchview");
		}
		layout.addView("Views.LoadedDataview", IPageLayout.RIGHT, 0.81f, "Powder folder");
		layout.addView("uk.ac.diamond.scisoft.analysis.rcp.plotView1", IPageLayout.BOTTOM, 0.63f, "Powder folder");
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
