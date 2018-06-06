package graphstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ConfigReader {
    private static final String graphInstMatchStr = "graph(:|=)\\s*(?<graphPort>\\d+)";
    
    private static final Pattern configMatcher = Pattern.compile(
            String.format("%s\\s*",
                graphInstMatchStr

            ));

    protected File configFile;
    protected String config;
    
    public Integer graphPort;
    
	public ConfigReader(File configFile) throws FileNotFoundException {
		if (!configFile.exists()) {
			throw new FileNotFoundException(configFile.getPath());
		}
		
		try {
			config = new String(Files.readAllBytes(configFile.toPath()), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		parseConfigFile();
	}
	
	public ConfigReader(String configString) {
		configFile = null;
		config = configString;
		parseConfigFile();
	}

	protected void parseConfigFile() {

        for(String line : config.split("\\r?\\n")) {
            Matcher result = configMatcher.matcher(line);
            if(!result.matches()){
                System.err.println("ConfigReader: Invalid line:\n" + line);
            }
            else if (result.group("graphPort") != null) {
                graphPort = Integer.parseInt(result.group("graphPort"));
            } else{
                System.err.println("ConfigReader: Invalid line:\n" + line);
            }
        }

        if (graphPort == null) {
            throw new RuntimeException("Config file is missing one or more required lines!");
        }
    }

    public int getBlockPort() {
        return graphPort;
    }
}