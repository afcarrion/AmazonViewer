package com.anncode.amazonviewer.dao;

import java.util.ArrayList;

import com.anncode.amazonviewer.db.IDBConnection;
import com.anncode.amazonviewer.model.Movie;
import com.anncode.util.AmazonUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import static com.anncode.amazonviewer.db.DataBase.*;

public interface MovieDAO extends IDBConnection{
	
	default Movie setmovieViewed(Movie movie) {
		try(Connection connection = connectToDB()) {
			
			Statement statement = connection.createStatement();
			
			String query = "INSERT INTO " + TVIEWED +
					" (" + TVIEWED_IDMATERIAL + ", "+TVIEWED_IDELEMENT +","+TVIEWED_IDUSUARIO+","+ TVIEWED_FECHA+ ")"+
					" VALUES("+ID_TMATERIALS[0]+", " +movie.getId()+", "+TUSER_IDUSUARIO+", '"+ AmazonUtil.getCurrentDate() +"')";
			System.out.println(query);
			
			if(statement.executeUpdate(query) > 0) {
				System.out.println("Se marco como vista!!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movie;
	}
	
	default ArrayList<Movie> read(){
		ArrayList<Movie> movies = new ArrayList<Movie>();
		try(Connection connection = connectToDB()){
			String query = "SELECT * FROM " + TMOVIE;
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				Movie movie = new Movie(
						rs.getString(TMOVIE_TITLE),
						rs.getString(TMOVIE_GENRE),
						rs.getString(TMOVIE_CREATOR),
						Integer.valueOf(rs.getString(TMOVIE_DURATION)),
						Short.valueOf(rs.getString(TMOVIE_YEAR)));
				
				movie.setId(Integer.valueOf(rs.getString(TMOVIE_ID)));
				movie.setViewed(getMovieViewed(
						preparedStatement, connection, movie.getId()
						));
				movies.add(movie);
			}
			
		}catch(SQLException ex) {
			
		}
		return movies;
	}
	
	private boolean getMovieViewed(PreparedStatement preparedStatement, Connection connection, int id_movie) {
		
		boolean viewed = false;
		ResultSet rs = null;
		
		String query = "SELECT * FROM " + TVIEWED +
				" WHERE " + TVIEWED_IDMATERIAL + "= ?" +
				" AND " + TVIEWED_IDELEMENT + "= ?" +
				" AND " + TVIEWED_IDUSUARIO + "= ?";
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, ID_TMATERIALS[0]);
			preparedStatement.setInt(2, id_movie);
			preparedStatement.setInt(3, TUSER_IDUSUARIO);
			
			rs = preparedStatement.executeQuery();
			
			viewed = rs.next();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return viewed;
	}
	
	private boolean getMovieViewedByDay(PreparedStatement preparedStatement, Connection connection, int id_movie) {
		 boolean viewed = false;
		 String query = "SELECT * FROM " + TVIEWED + " WHERE " + TVIEWED_IDMATERIAL + "= ?" + " AND "
		 		+ TVIEWED_IDELEMENT + "= ?" + " AND " + TVIEWED_IDUSUARIO + "= ?" +" AND " + TVIEWED_FECHA +"=\""+AmazonUtil.getCurrentDate()+"\"";
		 ResultSet rs = null;
		 try {
		 	preparedStatement = connection.prepareStatement(query);
		 	preparedStatement.setInt(1, ID_TMATERIALS[0]);
		 	preparedStatement.setInt(2, id_movie);
		 	preparedStatement.setInt(3, TUSER_IDUSUARIO);
		 	System.out.println(query);
		 	rs = preparedStatement.executeQuery();
		 	viewed = rs.next();
		 } catch (Exception e) {
		 	// TODO: handle exception
		 }

		 return viewed;
	}
	
	default ArrayList<Movie> readByDay() {
		
		ArrayList<Movie> movies = new ArrayList<>();
		try (Connection connection = connectToDB()) {
			String query = "SELECT * FROM " + TMOVIE;
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				Movie movie = new Movie(rs.getString(TMOVIE_TITLE), rs.getString(TMOVIE_GENRE),
						rs.getString(TMOVIE_CREATOR), Integer.valueOf(rs.getString(TMOVIE_DURATION)),
						Short.valueOf(rs.getString(TMOVIE_YEAR)));
				movie.setId(Integer.valueOf(rs.getString(TMOVIE_ID)));
				movie.setViewed(
						getMovieViewedByDay(preparedStatement, connection, Integer.valueOf(rs.getString(TMOVIE_ID))));
				movies.add(movie);
			}
		} catch (SQLException e) {
		// TODO: handle exception
		}
		return movies;
	}

}
