package fileHandling;


import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SimpleFileFilter extends FileFilter {
	
	String target;
	
	public SimpleFileFilter(String target) {
		super();
		this.target = target;
	}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()) {
			return true;
		}
		
		String extension = "";
		String filename = f.getName();
		int i = filename.lastIndexOf('.');
		if (i > 0) {
		    extension = filename.substring(i+1);
		}
		
		return extension.equals(target);
	}

	@Override
	public String getDescription() {
		return "Filter for " + this.target + " files";
	}
	
}
