package xyz.robin2000.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import xyz.robin2000.config.Config;
import xyz.robin2000.utils.MyStr;

public class DBConManager {
	public final static DBConManager me=new DBConManager();
	private static final Logger log = Logger.getLogger(DBConManager.class);
	boolean driverInited=false;
	
	private synchronized void initDriver() {
		if(driverInited) {
			return;
		}
		try {
			Class.forName(Config.ME.get("jdbc_driverClassName"));
		} catch (ClassNotFoundException e) {
			log.error("init driver fail?",e);
		}
		driverInited=true;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T execSql(String conName,SqlExec sqlExec) {
		if(!driverInited) {
			initDriver();
		}
		String jdbcUrl = Config.ME.get(MyStr.add(conName,"_jdbc_url"));
		String jdbcUsername = Config.ME.get(MyStr.add(conName,"_jdbc_username"));
		String jdbcPassword = Config.ME.get(MyStr.add(conName,"_jdbc_password"));
		
        try (final java.sql.Connection con = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword)){
			return (T)sqlExec.exec(con);
        } catch (Exception e) {
        	log.error("SQL excute fail",e);
        }
		return null;
	}
	public List<Map<String,Object>> query(String conName,String sql) {
		return execSql(conName, con->{
	        try (PreparedStatement ps=con.prepareStatement(sql);ResultSet rs = ps.executeQuery();){
				if(rs.isBeforeFirst()) {
					
					ResultSetMetaData rsmd=rs.getMetaData();
					int count=rsmd.getColumnCount();
					
					List<Map<String,Object>> result=new LinkedList<>();
					while(rs.next()) {
						Map<String,Object> rec=new HashMap<>();
						for(int i=0;i<count;i++) {
							//String colName=rsmd.getColumnName(i+1);
							rec.put(rsmd.getColumnLabel(i+1), rs.getObject(i+1));
						}
						result.add(rec);
					}
					return result;
				}
	        } catch (Exception e) {
	        	log.error(MyStr.add("SQL excute fail at ",conName,":\r\n",sql,":\r\n"),e);
	        }
	        return Collections.emptyList();
		});
	}
}
