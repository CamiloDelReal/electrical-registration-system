package services;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.Receipt;

import utils.Connection;

public class ReceiptServices {
	
	public static List<Receipt> getReceipts() throws SQLException{
		List<Receipt> receipts = new LinkedList<Receipt>();
		
		java.sql.Connection connection = Connection.getConnection();
		connection.setAutoCommit(false);
		
		String function = "{ ? = call \"ObtenerRecibos\"() }";
		CallableStatement call = connection.prepareCall(function);
		call.registerOutParameter(1, java.sql.Types.OTHER);
		call.execute();
		ResultSet rs = (ResultSet)call.getObject(1);
		
		while(rs.next()){
			receipts.add(new Receipt(rs.getInt("idRecibo"),rs.getInt("consumo"),
						rs.getInt("mes"),rs.getInt("anyo"),
						"No. "+rs.getInt("numero")+ " "+ "Direccion: "+ rs.getString("direccion"), rs.getInt("idHogar")));
		}
		
		call.close();
		rs.close();
		
		return receipts;
	}
	
	public static void insertReceipt(int idHogar, int consumption, int month, int year ) throws SQLException{
		java.sql.Connection connection = Connection.getConnection();
		
		connection.setAutoCommit(true);
		
		String function= "{call \"InsertarRecibo\"(?, ?, ?, ?)}";
		CallableStatement call = connection.prepareCall(function, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		call.setInt(1, idHogar);
		call.setInt(2, consumption);
		call.setInt(3, month);
		call.setInt(4, year);
		call.execute();
		
		call.close();
	}
	
}
