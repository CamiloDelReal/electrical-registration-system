package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.County;
import utils.Connection;

public class CountyServices {
	
	public static List<County> getEnablesCounty() throws SQLException{
		List<County> munic = new LinkedList<County>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerMunicipiosHabilitados\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			munic.add(new County(rs.getInt("idMunicipio"), rs.getString("nombreMunicipio"), false));
		}
		
		call.close();
		rs.close();
		
		return munic;
	}
	
	public static List<County> getCounty() throws SQLException{
		List<County> munic = new LinkedList<County>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerMunicipios\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			munic.add(new County(rs.getInt("idMunicipio"), rs.getString("nombreMunicipio"), rs.getBoolean("cancelado")));
		}
		
		call.close();
		rs.close();
		
		return munic;
	}
	
	public static void insertCounty(String name) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"InsertarMunicipio\"(?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.setString(1, name);
		call.execute();
		
		call.close();
	}
	
	public static void modifyCounty(int id, String name) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"ModificarMunicipio\"(?,?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.setString(2, name);
		call.execute();
		
		call.close();
	}
	
	public static void cancelCounty(int id) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"CancelarMunicipio\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.execute();
		
		call.close();
	}

}
