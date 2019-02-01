package xyz.robin2000.task.run;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.apache.log4j.Logger;

import xyz.robin2000.config.Config;
import xyz.robin2000.task.config.QueryData;
import xyz.robin2000.task.config.Task;
import xyz.robin2000.utils.MyStr;
/**
 * 
        update: UPDATE locales_quest SET {CN_FIELD}={CN_VALUE} WHERE entry={PK1_VALUE}};
        insert: INSERT INTO locales_quest(entry,{CN_FIELD}) VALUES({PK1_VALUE},{CN_VALUE}});
 *
 */
public class SqlCreate {
	
	private static final Logger log = Logger.getLogger(SqlCreate.class);
	
	public static SqlCreate me=new SqlCreate();
	
	private BufferedWriter bw=null;
	
	public void createSql(Task task, QueryData target,Map<String,Object> row,int colIdx,Object cnStr) {
		String cnField=target.getCnCols()[colIdx];
		Object pkValue=target.getPkValue(row);
		Object hasCN=row.get("hasCN");
		
		boolean hasCnRecod = true;
		if(hasCN==null) {
			hasCnRecod =false;
			row.put("hasCN", 1);
		}
		
		String sql0;
		if(hasCnRecod) {
			sql0 = task.getTranslate().getUpdate();
		}else {
			sql0 = task.getTranslate().getInsert();
		}
		
		String sql=sql0.replaceAll("\\{CN_FIELD\\}", cnField);
		sql=sql.replaceAll("\\{CN_VALUE\\}", MyStr.add('\'',cnStr.toString(),'\''));
		
		String pk[]=pkValue.toString().split("_");
		sql=sql.replaceAll("\\{PK1_VALUE\\}", pk[0]);
		if(pk.length==2) {
			sql=sql.replaceAll("\\{PK2_VALUE\\}", pk[1]);
		}
		if(pk.length==3) {
			sql=sql.replaceAll("\\{PK3_VALUE\\}", pk[2]);
		}
		
		writeFile(MyStr.add(sql,"\r\n"));
	}
	
	public void writeFile(String text) {
			if (text == null)
				return;
			
			try {
				if(bw==null) {
					String file = Config.ME.get("output");
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				}

				bw.write(text);
			}catch(Exception e) {
				log.error(e.getMessage(),e);
				Runtime.getRuntime().exit(0);
			}
	}
	
	
	
	public void finish() {
		try {
			if(bw!=null) {
				bw.flush();
				bw.close();
			}
		}catch(Exception e) {
			
		}
		
		log.info(MyStr.add("finished output file :",Config.ME.get("output")));
	}
}
