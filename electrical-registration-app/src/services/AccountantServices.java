package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.Accountant;

import utils.Connection;

public class AccountantServices {
	
	public static void insertAccountant(long idNumber,String name, String lastName, int experience, int municipality) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"InsertarContador\"(?, ?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.setLong(1, idNumber);
		call.setString(2, name);
		call.setString(3, lastName);
		call.setInt(4, experience);
		call.setInt(5, municipality);
		call.execute();
		
		call.close();
	}
	
	public static void modifyAccountant(int id, long idNumber, String name, String lastName, int experience, int municipality ) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"ModificarContador\"(?, ?, ?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.setLong(2, idNumber);
		call.setString(3,name);
		call.setString(4, lastName);
		call.setInt(5, experience);
		call.setInt(6,  municipality);
		call.execute();
		
		call.close();
	}
	
	public static List<Accountant> getAccountants() throws SQLException{

		List<Accountant> accountants = new LinkedList<>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerContadores\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			accountants.add(new Accountant(rs.getInt("idContador"), rs.getString("nombreMunicipio"), rs.getInt("experiencia"),rs.getLong("carnet"), rs.getString("apellidos"), rs.getString("nombre"), rs.getBoolean("cancelado")));
		}
		
		call.close();
		rs.close();
		
		return accountants;
	}
	
	public static void cancelAccountant(int id) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"CancelarContador\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.execute();
		
		call.close();
	}

	public static List<Accountant> getEnlabledAccountants() throws SQLException{

		List<Accountant> accountants = new LinkedList<>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerContadoresHabilitados\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			accountants.add(new Accountant(rs.getInt("idContador"), rs.getString("nombreMunicipio"), rs.getInt("experiencia"),rs.getLong("carnet"), rs.getString("apellidos"), rs.getString("nombre"), false));
		}
		
		call.close();
		rs.close();
		
		return accountants;
	}
}
