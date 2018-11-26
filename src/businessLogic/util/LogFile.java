package businessLogic.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public final class LogFile {

	public static String PATH = "log/";
	public static String FILE_NAME = "error.log";
	public static String CHARSET = "utf8";
	
	private static Optional<File> file = null;
	private static DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

	private LogFile() {
	}

	public static File getFile() {
		if(!file.isPresent()) {
			try {
				file = Optional.ofNullable(createOrRetrieve(PATH + FILE_NAME));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file.get();
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
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFile(), append), CHARSET))) {
			writer.write(String.format("[%s] %s%n", timestampFormatter.format(LocalDateTime.now()), content));			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void log(Exception exception) {
		log(exception, true);
	}

	public static void log(Exception exception, boolean append) {
		try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(), append), CHARSET))){
			printWriter.println(String.format("### %s ###", timestampFormatter.format(LocalDateTime.now())));
			exception.printStackTrace(printWriter);
			printWriter.print(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a File if the file does not exist, or returns a
	 *  reference to the File if it already exists
	 * 
	 * @param target the targeted file path
	 * @return the already existing or the new created file
	 * @throws IOException When an I/O exception of some sort has occurred.
	 */
	private static File createOrRetrieve(String target) throws IOException {

		final Path path = Paths.get(target);

		if(Files.notExists(path)){
			return Files.createFile(Files.createDirectories(path)).toFile();
		}
		return path.toFile();
	}


	/**
	 * Deletes the target if it exists then creates a new empty file
	 * 
	 * @param target the targeted file path
	 * @return the already existing or the new created file
	 * @throws IOException When an I/O exception of some sort has occurred.
	 */
	private static File createOrReplaceFileAndDirectories(String target) throws IOException{

		final Path path = Paths.get(target);
		// Create only if it does not exist already
		Files.walk(path)
		.filter(p -> Files.exists(p))
		.sorted(Comparator.reverseOrder())
		.forEach(p -> {
			try{
				Files.createFile(Files.createDirectories(p));
			} catch(IOException e){
				throw new IllegalStateException(e);
			}
		});

		return Files.createFile(Files.createDirectories(path)).toFile();
	}

}
