package ca.bc.gov.hlth.hnweb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtil {

	private static final Logger log = LoggerFactory.getLogger(TestUtil.class);

	public static String convertXMLFileToString(String file) throws IOException {
		// our XML file for this example
		File xmlFile = new File(file);

		Reader fileReader;
		fileReader = new FileReader(xmlFile);
		BufferedReader bufReader = new BufferedReader(fileReader);

		StringBuilder sb = new StringBuilder();
		String line = bufReader.readLine();
		while (line != null) {
			sb.append(line).append("\n");
			line = bufReader.readLine();
		}
		String xml2String = sb.toString();
		bufReader.close();
		
		log.debug("");

		return xml2String;
	}

}
