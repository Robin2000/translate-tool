package xyz.robin2000.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import xyz.robin2000.task.config.Tasks;
import xyz.robin2000.utils.MyStr;

public class TranslateTaskLoader {

	private static final Logger log = Logger.getLogger(TranslateTaskLoader.class);
	
	private String taskDef;
	private Tasks tasks;
	
	public TranslateTaskLoader(String taskDef) {
		this.taskDef=taskDef;
	}
	
	public Tasks getTasks() {
		if(tasks==null) {
			load();
		}
		return tasks;
	}

	private void load() {

		String fullFile=MyStr.add(System.getProperty("user.dir"), "/conf/" , taskDef , ".yaml");
		
		Constructor constructor = new Constructor(Tasks.class);
		Yaml yaml = new Yaml(constructor);
		
		try (FileInputStream inputStream = new FileInputStream(fullFile); 
			InputStreamReader isr =new InputStreamReader(inputStream,"UTF-8");){
			tasks=yaml.load(isr);
			log.info(MyStr.add(tasks.getTitle()," loaded!"));
		} catch (FileNotFoundException e) {
			log.error("Properties file not found: " + fullFile);
		} catch (IOException e) {
			log.error("Properties file can not be loading: " + fullFile);
		} 

	}
}
