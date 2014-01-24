package tern.eclipse.ide.ui.hyperlink;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;

import tern.eclipse.ide.core.IDETernProject;
import tern.eclipse.ide.ui.utils.EditorUtils;
import tern.server.protocol.TernDoc;
import tern.server.protocol.definition.ITernDefinitionCollector;
import tern.utils.StringUtils;

public abstract class AbstractTernHyperlink implements IHyperlink,
		ITernDefinitionCollector {

	protected final IRegion region;
	protected final IDETernProject ternProject;

	public AbstractTernHyperlink(IRegion region, IDETernProject ternProject) {
		this.region = region;
		this.ternProject = ternProject;
	}

	@Override
	public void open() {
		try {
			TernDoc doc = createDoc();
			ternProject.request(doc, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public IRegion getHyperlinkRegion() {
		return region;
	}

	@Override
	public void setDefinition(String filename, Long start, Long end) {
		IFile file = getFile(filename);
		if (file != null && file.exists()) {
			EditorUtils.openInEditor(file, start.intValue(), end.intValue()
					- start.intValue(), true);
		}
	}

	private IFile getFile(String filename) {
		if (StringUtils.isEmpty(filename)) {
			return null;
		}
		return ternProject.getProject().getFile(filename);
	}

	protected abstract TernDoc createDoc() throws Exception;
}
