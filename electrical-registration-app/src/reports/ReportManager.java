package reports;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;
import visual.Main;
import visual.dialogs.DialogBox;

public class ReportManager {
	private static ReportManager manager = null;
	public static ReportManager getInstance(){
		if(manager == null)
			manager = new ReportManager();
		return manager;
	}
	
	private static java.sql.Connection myConnection = null;
	private static final String ADDRESS_TO_SERVER = "jdbc:postgresql://localhost:5432/ConsumosElectricos";
	private static final String USER_NAME = "postgres";
	private static final String USER_PASSWORD = "postgres";

	private ReportManager(){
		try{
			myConnection = DriverManager.getConnection(ADDRESS_TO_SERVER, USER_NAME, USER_PASSWORD);
		} catch(SQLException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	public void TenHousesWithMoreRealConsumption(){
		try{
			Class.forName("org.postgresql.Driver");
			@SuppressWarnings("rawtypes")
			HashMap parametros = new HashMap();
			//parametros.put("user", f.getText());
			JasperFillManager.fillReportToFile("src/reports/10CasasConMayorConsumoReal.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/10CasasConMayorConsumoReal.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
	public void AccountantsAndReadingsByHouse(Main main){
		try{
			Class.forName("org.postgresql.Driver");
			@SuppressWarnings("rawtypes")
			HashMap parametros = new HashMap();
			//parametros.put("user", f.getText());
			JasperFillManager.fillReportToFile("src/reports/ContadoresYLecturasPorHogar2.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/ContadoresYLecturasPorHogar2.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.setMain(main);
			diag.show();
		}
	}
	
	public void housesWithBadRealConsumption(Main main){
		try{
			Class.forName("org.postgresql.Driver");
			@SuppressWarnings("rawtypes")
			HashMap parametros = new HashMap();
			//parametros.put("user", f.getText());
			JasperFillManager.fillReportToFile("src/reports/HogaresConconsumoAlterado.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/HogaresConconsumoAlterado.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.setMain(main);
			diag.show();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void electricReadingByHouse(int hogar, int valor){
		try{
			Class.forName("org.postgresql.Driver");			
			HashMap parametros = new HashMap();
			parametros.put("hogar", hogar);
			parametros.put("valor", valor);
			JasperFillManager.fillReportToFile("src/reports/LecturasDeHogarConConsumoMayorQueValor.jasper", parametros, myConnection);
			JasperViewer.viewReport("src/reports/LecturasDeHogarConConsumoMayorQueValor.jrprint", false);
		} catch (ClassNotFoundException | JRException e){
			DialogBox diag = new DialogBox(e.getMessage());
			diag.show();
		}
	}
	
}