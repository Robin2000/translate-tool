package xyz.robin2000.db;

import java.sql.Connection;

public interface SqlExec {
	public Object exec(Connection con);
}
