package utils;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
	private static java.sql.Connection connection = null;
	
	private static final String DRIVER_CLASS = "org.postgresql.Driver";
	private static final String ACCESS_PROTOCOL = "jdbc";
	private static final String PROTOCOL = "postgresql";
	private static final String ACCESS_PORT = "5432";
	
	private static final String SERVER = "localhost";
	private static final String BD = "ConsumosElectricos";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";
	
	private Connection(String serverAddress, String database, String user,String password) throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER_CLASS);
		String url = ACCESS_PROTOCOL + ":" + PROTOCOL + "://" + serverAddress + ":" + ACCESS_PORT + "/"+ database;
		connection = DriverManager.getConnection(url, user, password);
	}
	
	public static  java.sql.Connection getConnection() {
		 if(connection == null)
			try {
				new Connection(SERVER, BD, USER, PASSWORD);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 return connection;
	}
}

