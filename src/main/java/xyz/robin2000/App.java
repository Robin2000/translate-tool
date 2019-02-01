package xyz.robin2000;

import org.apache.log4j.xml.DOMConfigurator;

import xyz.robin2000.config.Config;
import xyz.robin2000.config.TranslateTaskLoader;
import xyz.robin2000.task.config.Tasks;
import xyz.robin2000.task.run.SqlCreate;
import xyz.robin2000.task.run.TaskRunner;

public class App 
{
    public static void main( String[] args )
    {
		DOMConfigurator.configure("conf/log4j.xml");
		String[] taskDefs=Config.ME.get("task_def").split(",");
		
		
		for(String taskDef:taskDefs) {
			Tasks tasks=new TranslateTaskLoader(taskDef).getTasks();
			new TaskRunner(tasks).run();
		}
		
		SqlCreate.me.finish();
    }
}
