package xyz.robin2000.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

public abstract class AbstractPropertiesConfigBase {

	private static final Logger log = Logger.getLogger(AbstractPropertiesConfigBase.class);
	private boolean loaded = false;
	private HashMap<String, String> propMap = new HashMap<String, String>();

	public abstract String getFileName();

	public String get(String key) {
		if (!loaded) {
			load();
		}
		return propMap.get(key);
	}

	protected synchronized void load() {

		if (loaded) {
			return;
		}

		String fullFile = System.getProperty("user.dir") + getFileName();

		Properties prop = new Properties();
		try (FileInputStream inputStream = new FileInputStream(fullFile); 
			InputStreamReader isr =new InputStreamReader(inputStream,"UTF-8");){
			prop.load(isr);
		} catch (FileNotFoundException e) {
			log.error("Properties file not found: " + fullFile);
		} catch (IOException e) {
			log.error("Properties file can not be loading: " + fullFile);
		} 
		Iterator<Entry<Object, Object>> iterator = prop.entrySet().iterator();

		Entry<Object, Object> entry;
		while (iterator.hasNext()) {
			entry = iterator.next();
			propMap.put(entry.getKey().toString().trim(), entry.getValue().toString().trim());
		}

		loaded = true;

	}
}
