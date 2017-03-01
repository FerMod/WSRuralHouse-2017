package businessLogic.util;

import java.io.File;

public final class ErrorLog {

	private final String PATH = "/log/";
	private final String FILE_NAME = "error.log";
	private String directory;
	
	private ErrorLog() {
//		this.directory = PATH.concat(this.getClass(). );
	}

	private void generate() {
		File directory = new File(String.valueOf(PATH));
		if (! directory.exists()){
			directory.mkdir();
			// If you require it to make the entire directory path including parents,
			// use directory.mkdirs(); here instead.
		}
	}

}
