package org.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	private final static String url = "jdbc:mysql://localhost:3306/db-nations";
	private final static String user = "root";
	private final static String password = "root";

	public static void main(String[] args) {
		printQuery1();
	}

	private static void printQuery1() {
		Scanner in = new Scanner(System.in);
		System.out.print("Cerca: ");
		String searchValue = in.nextLine();
		in.close();
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			final String sql = "SELECT country_id,countries.name AS country_name, regions.name AS regions_name, continents.name AS continents_name \r\n"
					+ "FROM countries \r\n"
					+ "JOIN regions \r\n"
					+ "ON countries.region_id = regions.region_id \r\n"
					+ "JOIN continents \r\n"
					+ "ON regions.continent_id = continents.continent_id \r\n"
					+ "WHERE countries.name "
					+ "LIKE " + "'%" + searchValue + "%'";
			
			try(PreparedStatement ps = con.prepareStatement(sql)){
				try(ResultSet rs = ps.executeQuery()){
					while(rs.next()) {
			    		
			    		int id = rs.getInt(1);
			    		String name = rs.getString(2);
			    		String region = rs.getString(3);
			    		String continent = rs.getString(4);
			    		
			    		System.out.println("[" + id + "] " +  "Nazione: " +name + "/" + " Regione: " + region + "/" + " Continente:  " + continent);
			    	}
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
