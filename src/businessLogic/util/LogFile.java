package businessLogic.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class LogFile {

	public static String PATH = "log/";
	public static String FILE_NAME = "error.log";

	private LogFile() {
	}

	public static String getPath() {
		return PATH + FILE_NAME;
	}

	public static String getAbsolutePath() {
		File file = new File(PATH + FILE_NAME);
		return file.getAbsolutePath();
	}

	public static void generateFile(Exception exception) {
		generateFile(exception, false);
	}

	public static void generateFile(Exception exception, boolean append) {

		File directory = new File(String.valueOf(PATH));
		if (!directory.exists()){
			directory.mkdirs();
		}

		try {

			Writer w = new FileWriter(getPath(), append);
			PrintWriter pw = new PrintWriter(new BufferedWriter(w));
			pw.println("### " + getCurrentDate() + " ###");
			exception.printStackTrace(pw);
			pw.print(System.getProperty("line.separator"));
			pw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void generateFile(String content) {
		generateFile(content, false);
	}

	public static void generateFile(String content, boolean append) {

		File directory = new File(String.valueOf(PATH));
		if (!directory.exists()){
			directory.mkdirs();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(getCurrentDate());
		sb.append(System.getProperty("line.separator"));
		sb.append(content);
		sb.append(System.getProperty("line.separator"));

		try {
			
			FileWriter fw = new FileWriter(new File(PATH + FILE_NAME), append);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String getCurrentDate(){
		Calendar cal = Calendar.getInstance();
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(cal.getTime());
	}

}
