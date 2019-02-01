package xyz.robin2000.task.run;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import xyz.robin2000.db.DBConManager;
import xyz.robin2000.task.config.QueryData;
import xyz.robin2000.task.config.Task;
import xyz.robin2000.task.config.Tasks;
import xyz.robin2000.utils.MyStr;

public class TaskRunner {
	private static final Logger log = Logger.getLogger(TaskRunner.class);
	Tasks tasks;
	public TaskRunner(Tasks tasks) {
		this.tasks=tasks;
	}
	public void run() {
		loadData();
	}
	
	private void loadData() {
		for(Task task:tasks.getTask()) {
			loadQueryData("source",task.getSource());
			loadQueryData("target",task.getTarget());
			match(task);
		}
	}
	private void loadQueryData(String conName, QueryData queryData) {
		List<Map<String,Object>> result = DBConManager.me.query(conName,queryData.getQuerySql());
		queryData.setData(result);
		log.info(MyStr.add(result.size() , " records loaded! at ", conName ,':',queryData.getQuerySql()));
	}
	private boolean allEnglish(Object str) {
		String s=str.toString();
		return s.length()==s.getBytes().length;
	}
	private void match(Task task) {
		QueryData source=task.getSource();
		QueryData target=task.getTarget();
		
		//从目标数据集开始，有中文的skip,没有的尝试从源中获取
		//从源中也获取不到的skip，能从源中获取到则判断需要insert还是update
		
		List<Map<String,Object>> rows=target.getData();
		for(Map<String,Object> row : rows) {
			
			String cnCols[]=target.getCnCols();
			for(int i=0;i<cnCols.length;i++) {
				Object cn=row.get(cnCols[i]);
				
				// 不为空且含有汉字，表示已经处理过
				if(cn!=null && !allEnglish(cn)) {
					continue;
				}
				
				// 查找对应中文
				Object cnStr=source.findCN(row,i);
				if(cnStr==null || allEnglish(cnStr)) {
					continue;
				}
				
				//将中文写入
				SqlCreate.me.createSql(task,target,row,i,cnStr);
				
			}
		}
		
		
//		for(Map<String,Object> row : rows) {
//			Object hasLocalCn=row.get("hasCN");
//			if(hasLocalCn==null) {
//				
//			}
//		}
		
	}
}
