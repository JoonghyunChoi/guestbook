package kr.or.connect.guestbook.dao;


import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import kr.or.connect.guestbook.dto.Guestbook;
import static kr.or.connect.guestbook.dao.GuestbookDaoSqls.*;


@Repository
public class GuestbookDao  {
	
	public static Connection getConnection() throws Exception {
		
		String url = "jdbc:mysql://localhost:3306/connectdb?useUnicode=true&characterEncoding=utf8&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&useSSL=false";
		String user = "connectuser";
		String password = "1234";
		
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection(url, user, password);
		
		return connection;
	}
	
	public static void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		
		if (resultSet != null) {
			try {
				resultSet.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (connection != null) {
			try {
				connection.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
 	
	
	public List<Guestbook> selectAll(Integer start, Integer limit) {
		List<Guestbook> list = new ArrayList<>();
		Guestbook guestbook = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = getConnection(); 
			
			String sql = SELECT_PAGING;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, start);
			preparedStatement.setInt(2, limit);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				guestbook = new Guestbook();
				guestbook.setId(resultSet.getLong(1));
				guestbook.setName(resultSet.getString(2));
				guestbook.setContent(resultSet.getString(3));
				guestbook.setRegdate(resultSet.getDate(4));
				
				list.add(guestbook);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection, preparedStatement, resultSet);
		}
		return list;
	}
	
	public long insert(Guestbook guestbook) {
		long result = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = getConnection();
			
			Date date = guestbook.getRegdate();
			java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
			
			String sql = "insert into guestbook(name, content, regdate) values (?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, guestbook.getName());
			preparedStatement.setString(2, guestbook.getContent());
			preparedStatement.setTimestamp(3, sqlDate);

			result = preparedStatement.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection, preparedStatement, resultSet);
		}
		return result;
	}
	
	public int deleteById(Long id) {
		int result = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
				
		try {
			connection = getConnection();
			
			String sql = DELETE_BY_ID;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, id);

			result = preparedStatement.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection, preparedStatement, resultSet);
		}
		return result;
	}
	
	public int selectCount() {
		int count = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = getConnection(); 
			
			String sql = SELECT_COUNT;
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
		}
		catch(Exception e) {
			e.printStackTrace();			
		}
		finally {
			closeConnection(connection, preparedStatement, resultSet);
		}
		return count;
	}
}

