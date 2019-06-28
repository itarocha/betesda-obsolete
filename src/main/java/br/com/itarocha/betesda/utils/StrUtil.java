package br.com.itarocha.betesda.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StrUtil {

	public static StringBuilder loadFile(String fileName) {
		StringBuilder sb = new StringBuilder();
		try {
			Path path = Paths.get(StrUtil.class.getResource(fileName).toURI());
			Stream<String> lines = Files.lines(path);
			lines.forEach(line -> sb.append(line).append("\n"));
			lines.close();
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return sb;
		
	}
}
