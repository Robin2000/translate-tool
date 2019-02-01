package xyz.robin2000.task.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import xyz.robin2000.utils.MyStr;

public class QueryData {
	private String pk1;
	private String pk2;
	private String pk3;
	private String en;
	private String cn;
	private String query;
	private boolean pkMatch;
	
	//auto
	private String[] enCols;
	private String[] cnCols;
	private String querySql;
	
	private List<Map<String,Object>> data;
	private Map<Object,Map<String,Object>> dataPkMap; /*<pk,<field,value>>*/
	private Map<Object,Object> dataEnMap; /*<enValue,cnValue>*/	
	
	/*将所有中英文对照放入map，因为有可能相同的en对应不同的cn, 仅当pk无法匹配时才使用*/
	public Map<Object,Object> getDataEnMap() {
		if(dataEnMap==null && data!=null) {
			dataEnMap=new HashMap<>();
			String[] enCols=getEnCols();
			String[] cnCols=getCnCols();
			
			for(Map<String,Object> row: data) {
				
				for(int i=0;i<enCols.length;i++) {
					dataEnMap.put(row.get(enCols[i]), row.get(cnCols[i]));
				}
			}
		}
		return dataEnMap;
	}
	public Map<Object, Map<String, Object>> getDataPkMap() {
		if(dataPkMap==null && data!=null) {
			dataPkMap=new HashMap<>();
			for(Map<String,Object> row: data) {
				Object pkValue=getPkValue(row);
				dataPkMap.put(pkValue, row);
			}
		}
		return dataPkMap;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	/* 从源中找到对应中文字符串
	 * row待翻译的行数据
	 * colIdx正在处理的列索引
	 * */
	public Object findCN(Map<String,Object> row, int colIdx) {
		if(pkMatch) {
			//主键匹配，首先计算主键值
			Object pkValue= getPkValue(row);
			//根据主键值定位到源表中对应的行
			Map<String, Object> sourceRow=getDataPkMap().get(pkValue);
			if(sourceRow==null) {
				return null;
			}
			//根据列的索引值找到对应的列的值
			String cnCols[]=getCnCols();
			return sourceRow.get(cnCols[colIdx]);
		}
		
		//英文匹配时，首先得到对应的字段名
		String enField=getEnCols()[colIdx];
		Object targetEnStr=row.get(enField);

		return getDataEnMap().get(targetEnStr);
		
	}
	public Object getPkValue(Map<String,Object> row) {
		if(pk3!=null) {
			return MyStr.add(row.get(pk1),'_',row.get(pk2),'_',row.get(pk3));
		}else if(pk2!=null) {
			return MyStr.add(row.get(pk1),'_',row.get(pk2));
		}
		return row.get(pk1).toString();
	}

	public String getQuerySql() {
		if(querySql==null) {
			querySql=StringUtils.replace(query, "{pk1}", pk1);
			querySql=StringUtils.replace(querySql, "{pk2}", pk2);
			querySql=StringUtils.replace(querySql, "{pk3}", pk3);
			querySql=StringUtils.replace(querySql, "{en}", en);
			querySql=StringUtils.replace(querySql, "{cn}", cn);
		}
		return querySql;
	}

	public String[] getEnCols() {
		if(enCols==null) {
			enCols=en.split(",");
		}
		return enCols;
	}
	public String[] getCnCols() {
		if(cnCols==null) {
			cnCols=cn.split(",");
		}
		return cnCols;
	}
	public String getPk1() {
		return pk1;
	}
	public void setPk1(String pk1) {
		this.pk1 = pk1;
	}
	public String getPk2() {
		return pk2;
	}
	public void setPk2(String pk2) {
		this.pk2 = pk2;
	}
	public String getPk3() {
		return pk3;
	}
	public void setPk3(String pk3) {
		this.pk3 = pk3;
	}
	public String getEn() {
		return en;
	}
	public void setEn(String en) {
		this.en = en;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isPkMatch() {
		return pkMatch;
	}

	public void setPkMatch(boolean pkMatch) {
		this.pkMatch = pkMatch;
	}
	
}
