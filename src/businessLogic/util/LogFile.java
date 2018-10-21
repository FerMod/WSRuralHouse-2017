package businessLogic.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LogFile {

	public static String PATH = "log/";
	public static String FILE_NAME = "error.log";
	public static String CHARSET = "utf8";

	private static DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

	private LogFile() {
	}

	public static String getPath() {
		return PATH + FILE_NAME;
	}

	public static String getAbsolutePath() {
		File file = new File(PATH + FILE_NAME);
		return file.getAbsolutePath();
	}

	public static DateTimeFormatter getTimestampFormatter() {
		return timestampFormatter;
	}

	public static void setTimestampFormatter(DateTimeFormatter timestampFormatter) {
		LogFile.timestampFormatter = timestampFormatter;
	}

	public static void log(String content) {
		log(content, true);
	}

	public static void log(String content, boolean append) {
	
		createDirectory(PATH);
	
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getPath(), append), CHARSET))) {
			writer.write(String.format("[%s] %s%n", timestampFormatter.format(LocalDateTime.now()), content));			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public static void log(Exception exception) {
		log(exception, true);
	}

	public static void log(Exception exception, boolean append) {

		createDirectory(PATH);

		try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getPath(), append), CHARSET))){
			printWriter.println(String.format("### %s ###", timestampFormatter.format(LocalDateTime.now())));
			exception.printStackTrace(printWriter);
			printWriter.print(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates the necessary folders if those doesnt exist.
	 * 
	 * @param directoryPath the directory path
	 */
	private static void createDirectory(String directoryPath) {
		File directory = new File(String.valueOf(directoryPath));
		if (!directory.exists() && directory.isDirectory()){
			directory.mkdirs();
		}
	}

}
