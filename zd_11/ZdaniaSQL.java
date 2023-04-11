import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;

//autor: Jakub Marczyk

public class ZdaniaSQL implements GeneratorZdan {
	
	Connection c;

	@Override
	public void plikBazyDanych(String filename) {
		try {
			SQLiteConfig cf = new SQLiteConfig();
			cf.setEncoding(SQLiteConfig.Encoding.UTF8);
			c = DriverManager.getConnection(("jdbc:sqlite:"+ filename), cf.toProperties());
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String zbudujZdanie(int zdanieID) {
		String zdanie=null;
		ResultSet rs = null;
		int imieID, czynnoscID, przedmiotID, plec;
		String imie, czynnosc, przedmiot;
		
		try {
			Statement s = c.createStatement();
			rs = s.executeQuery("SELECT ImieID, CzynnoscID, PrzedmiotID FROM Zdanie WHERE ZdanieID=" + zdanieID);
			imieID = rs.getInt("ImieID");
			czynnoscID = rs.getInt("CzynnoscID");
			przedmiotID = rs.getInt("PrzedmiotID");
			
			rs = s.executeQuery("SELECT Imie, Plec FROM Imie where ImieID="+Integer.toString(imieID));
			
			imie = rs.getString("Imie");
			plec = rs.getInt("Plec");
			
			rs = s.executeQuery("SELECT Nazwa FROM Czynnosc WHERE CzynnoscID="+ Integer.toString(czynnoscID));
			czynnosc = rs.getString("Nazwa");
			
			rs = s.executeQuery("SELECT Nazwa FROM Przedmiot WHERE PrzedmiotID="+ Integer.toString(przedmiotID));
			przedmiot = rs.getString("Nazwa");
			
			if(plec==0)
				czynnosc+="a";
			
			zdanie = imie + " " + czynnosc + " " + przedmiot + ".";
			System.out.println(zdanie);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return zdanie;
	}

}
