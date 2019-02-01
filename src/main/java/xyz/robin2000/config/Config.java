package xyz.robin2000.config;

public class Config extends AbstractPropertiesConfigBase {

	public final static Config ME = new Config();

	@Override
	public String getFileName() {
		return "/conf/config.properties";
	}

	public int getInt(String key) {
		return Integer.parseInt(super.get(key));
	}
	
	public boolean getBoolean(String key) {
		return Boolean.getBoolean(super.get(key));
	}
}
