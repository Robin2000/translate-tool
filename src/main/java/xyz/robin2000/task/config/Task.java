package xyz.robin2000.task.config;

public final class Task {
	private String table;
	private QueryData source;
	private QueryData target;
	private Translate translate;
	
	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public QueryData getSource() {
		return source;
	}
	public void setSource(QueryData source) {
		this.source = source;
	}
	public QueryData getTarget() {
		return target;
	}
	public void setTarget(QueryData target) {
		this.target = target;
	}
	public Translate getTranslate() {
		return translate;
	}
	public void setTranslate(Translate translate) {
		this.translate = translate;
	}
	
}
