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
		printQueryNations();
		printNationDetails();
	}

	private static void printQueryNations() {
		Scanner in = new Scanner(System.in);
		System.out.print("Cerca: ");
		String searchValue = in.nextLine();
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			final String sqlNat = "SELECT country_id,countries.name AS country_name, regions.name AS regions_name, continents.name AS continents_name \r\n"
					+ "FROM countries \r\n" + "JOIN regions \r\n" + "ON countries.region_id = regions.region_id \r\n"
					+ "JOIN continents \r\n" + "ON regions.continent_id = continents.continent_id \r\n"
					+ "WHERE countries.name " + "LIKE " + "'%" + searchValue + "%'";

			try (PreparedStatement ps = con.prepareStatement(sqlNat)) {
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {

						int id = rs.getInt(1);
						String name = rs.getString(2);
						String region = rs.getString(3);
						String continent = rs.getString(4);
						System.out.println("[" + id + "] " + "Nazione: " + name + "/" + " Regione: " + region + "/"
								+ " Continente:  " + continent);
					}
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	private static void printNationDetails() {
		Scanner in= new Scanner(System.in);
		System.out.print("Inserisci l'id di un paese: ");
		String searchValue = in.nextLine();
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			final String sqlLang = "SELECT countries.name,languages.language, country_stats.year , country_stats.population, country_stats.gdp \r\n"
					+ "FROM countries \r\n"
					+ "JOIN country_languages \r\n"
					+ "ON countries.country_id = country_languages.country_id \r\n"
					+ "JOIN languages \r\n"
					+ "ON country_languages.language_id=languages.language_id \r\n"
					+ "JOIN country_stats \r\n"
					+ "ON countries.country_id = country_stats.country_id \r\n"
					+ "WHERE countries.country_id =" + searchValue + "\r\n"
					+ "ORDER BY country_stats.year DESC";

			try (PreparedStatement ps = con.prepareStatement(sqlLang)) {
				try (ResultSet rs = ps.executeQuery()) {
					boolean firstNameRow = true;
					boolean firstStatsRow = true;
					StringBuilder languages = new StringBuilder();
					while (rs.next()) {
						String name = rs.getString(1);
						String language = rs.getString(2);
						int year = rs.getInt(3);
						String population = rs.getString(4);
						String GDP = rs.getString(5);
						
						if(languages.indexOf(language) == -1) {
							languages.append(language);
							languages.append(", ");
						}
						
						if (firstNameRow) {
							System.out.println("Details for country: " + name);
							firstNameRow = false;
						}
						if(firstStatsRow) {
							System.out.println("Most recent stats" + "\n" 
									+ "Year: " + year + "\n"
									+ "Population: " + population + "\n"
									+ "GDP: " + GDP);
							firstStatsRow= false; 
						}
					}
					languages.deleteCharAt(languages.length()-2);
					System.out.println("Languages: " + languages);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}

