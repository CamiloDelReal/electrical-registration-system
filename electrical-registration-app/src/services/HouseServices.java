package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import utils.Connection;
import models.Accountant;
import models.House;


public class HouseServices {
	
	public static List<House> getHouses() throws SQLException{
		List<House> houses = new LinkedList<House>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerHogares\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			houses.add(new House(rs.getInt("idHogar"),rs.getString("direccion"), rs.getInt("habitantes"),rs.getInt("numero"), rs.getBoolean("cancelado"), rs.getString("nombre") + " " + rs.getString("apellidos")));
		}
		
		call.close();
		rs.close();
		
		return houses;
	}
	
	public static List<House> getEnabledHouses() throws SQLException{
		List<House> houses = new LinkedList<House>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerHogaresHabilitados\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			houses.add(new House(rs.getInt("idHogar"),rs.getString("direccion"), rs.getInt("habitantes"),rs.getInt("numero"), false, rs.getString("nombre") + " " + rs.getString("apellidos")));
		}
		
		call.close();
		rs.close();
		
		return houses;
	}
	
		
	public static void inserthouse(int number,String address, int population, int accountant) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"InsertarHogar\"(?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.setInt(1, number);
		call.setString(2, address);
		call.setInt(3, population);
		call.setInt(4, accountant);
		call.execute();
		
		call.close();
	}
	
	public static void modifyHouse(int id, int number, String address, int population, int accountant) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"ModificarHogar\"(?, ?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.setInt(2, number);
		call.setString(3, address);
		call.setInt(4, population);
		call.setInt(5, accountant);
		call.execute();
		
		call.close();
	}

	
	public static  Accountant getAccountantOfHouse(int houseNumber) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"ObtenerContadorDeHogar\"(?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, houseNumber);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		rs.first();
		
		Accountant accountant = new Accountant(rs.getInt("idContador"), rs.getString("nombreMunicipio"), rs.getInt("experiencia"),rs.getLong("carnet"), rs.getString("apellidos"), rs.getString("nombre"), rs.getBoolean("cancelado"));
		call.close();
		
		return accountant;
	}
	public static void cancelHouse(int id) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"CancelarHogar\"(?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setInt(1, id);
		call.execute();
		
		call.close();
	}
	
	public static House getHauseByNumber(int houseNumber) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{? = call \"ObtenerHogarPorNumero\"(?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, houseNumber);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		rs.first();
		
		House house = new House(rs.getInt("idHouse"), rs.getString("direccion"), rs.getInt("habitantes"), rs.getInt("numero"), rs.getBoolean("cancelado"), rs.getString("nombre")+ rs.getString("apellidos"));
		call.close();
		
		return house;
	}
}
