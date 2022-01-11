package org.sonatype.cs.metrics.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.nio.charset.StandardCharsets;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class FileIoService {
	@Value("${reports.outputdir}")
	private String outputdir;

	@Value("${data.dir}")
	private String datadir;
	
	@Value("${data.successmetrics}")
	private String successmetricsFile;
	
	public void writeInsightsCsvFile(String csvFilename, List<String[]> csvData, String beforeDateRange, String afterDateRange) throws IOException {
		
		String[] header = { "Measure", beforeDateRange, afterDateRange, "Delta", "Change (%)", "xTimes"};
		
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFilename));
	
			writer.write(String.join(",", header));
			writer.newLine();
			
			for (String[] array : csvData) {
				//log.info("- " + Arrays.toString(array));
				writer.write(String.join(",", Arrays.asList(array)));
				writer.newLine();
		    }	
			
			writer.close();
		}
		catch (IOException e) {
			throw e;
		}
		
		return;
	}
	
	
	public String makeFilename(String prefix, String extension, String timestamp) throws IOException {	
	    String filename = prefix + "-" + timestamp + "." + extension;
	
	    String reportsdir = datadir + File.separator + outputdir;
	    		
	    Path path = Paths.get(reportsdir);
	
	    if (!Files.exists(path)){
	      Files.createDirectory(path);
	    }
	
	    String filepath = reportsdir + File.separator + filename;

	    return filepath;
	}
	
	public String readJsonAsString(String filename) throws IOException {
		
		String jsonString = null;
		
		if (this.fileExists(filename)) {
			jsonString = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.ISO_8859_1);
		}
		
		return jsonString;
	}
	
	public boolean fileExists(String filename) throws IOException {
		boolean exists = false;
		
		File f = new File(filename);
		
		if (f.exists() && f.length() > 0){
			exists = true;
		}
		else {
			throw new IOException("Failed to read file : " + filename);
		}
		
		return exists;
	}

	public void writeSuccessMetricsFile(InputStream content) throws IOException {
	    File outputFile = new File(datadir + File.separator + successmetricsFile);
	    java.nio.file.Files.copy(content, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    IOUtils.closeQuietly(content);
		return;
	}

	public List<String> readFWCsvFile(String filename) throws IOException{
 		List<String> lines = Files.readAllLines(Paths.get(filename));
		
//		for (String line : lines) {
//			log.info(line);
//		}
		
		return lines;
	}

}
