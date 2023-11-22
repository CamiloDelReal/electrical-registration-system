package services;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.ElectricReading;
import utils.Connection;

public class ElectricReadingServices {
	
	public static List<ElectricReading> getElectricReadingByHouse(int hogar, int month, int year) throws SQLException{
		List<ElectricReading> elecReading = new LinkedList<ElectricReading>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerLecturasDeHogar\"(?, ?, ?) }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.setInt(2, hogar);
		call.setInt(3, month);
		call.setInt(4, year);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			elecReading.add(new ElectricReading(rs.getDate("fecha"), rs.getInt("consumo_inicial"),rs.getInt("consumo_final"), rs.getInt("idHogar")));
		}
		
		call.close();
		rs.close();
		
		return elecReading;
	}
	
	public static List<ElectricReading> getElectricReading() throws SQLException{
		List<ElectricReading> elecReading = new LinkedList<ElectricReading>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerLecturas\" }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			elecReading.add(new ElectricReading(rs.getDate("fecha"), rs.getInt("consumo_inicial"),rs.getInt("consumo_final"),rs.getInt("numero")));
		}
		
		call.close();
		rs.close();
		
		return elecReading;
	}
	
	public static void insertElectricReading(Date fecha,int hogar, int consumo_inicial, int consumo_final) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"InsertarLectura\"(?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.setDate(1, fecha);
		call.setInt(2, hogar);
		call.setInt(3, consumo_inicial);
		call.setInt(4, consumo_final);		
		call.execute();
		
		call.close();
	}
	
	public static void modifyElectricReading(Date fecha,int hogar, int consumo_inicial, int consumo_final) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"ModificarLectura\"(?, ?, ?, ? )}";
		CallableStatement call = connection.prepareCall(function);
		call.setDate(1,(java.sql.Date) fecha);
		call.setInt(2, hogar);
		call.setInt(3, consumo_inicial);
		call.setInt(4, consumo_final);
		call.execute();
		
		call.close();
	}
	
	public static void deleteElectricReading(Date date, int house) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"EliminarLectura\"(?, ?)}";
		CallableStatement call = connection.prepareCall(function);
		call.setDate(1, date);
		call.setInt(2, house);
		
		call.execute();
		
		call.close();
	}
}
