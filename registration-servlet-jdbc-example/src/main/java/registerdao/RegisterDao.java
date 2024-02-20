package registerdao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import registerbean.RegisterBean;

//public class RegisterDao {
//
//	public boolean validate(RegisterBean loginBean) throws ClassNotFoundException {
//		boolean status = false;
//
//		Class.forName("com.mysql.jdbc.Driver");
//        String url = "jdbc:mysql://localhost:3306/login_app";
//        String username = "YZ1";
//        String password = "ILoveInfosys";
//
//		try (Connection connection = DriverManager.getConnection(url, username, password);
//				// Step 2:Create a statement using connection object
//				PreparedStatement preparedStatement = connection
//						.prepareStatement("select * from users where username = ? and password = ? ")) {
//			preparedStatement.setString(1, loginBean.getUsername());
//			preparedStatement.setString(2, loginBean.getPassword());
//
//			System.out.println(preparedStatement);
//			ResultSet rs = preparedStatement.executeQuery();
//	        if (rs.next()) {
//	            status = true;
//	            loginBean.setName(rs.getString("name"));
//	            loginBean.setPhoneNumber(rs.getString("phone_number")); // Assuming it's a String
//	            System.out.println(loginBean.getPhoneNumber());
//	            System.out.println(loginBean.getName());
//	            
//	        }
//
//		} catch (SQLException e) {
//			// process sql exception
//			printSQLException(e);
//		}
//		return status;
//	}
//
//	private void printSQLException(SQLException ex) {
//		for (Throwable e : ex) {
//			if (e instanceof SQLException) {
//				e.printStackTrace(System.err);
//				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
//				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
//				System.err.println("Message: " + e.getMessage());
//				Throwable t = ex.getCause();
//				while (t != null) {
//					System.out.println("Cause: " + t);
//					t = t.getCause();
//				}
//			}
//		}
//	}
//}


public class RegisterDao {
    private static final String URL = "jdbc:mysql://localhost:3306/user_registry";
    private static final String USERNAME = "YZ1";
    private static final String PASSWORD = "ILoveInfosys";

    // SQL queries
    private static final String INSERT_USER_QUERY = "INSERT INTO registered_users (name, phone_number, address) VALUES (?, ?, ?)";
//    private static final String GET_USER_ID_QUERY = "SELECT LAST_INSERT_ID() AS last_id";
    private static final String CHECK_USER_EXISTS_QUERY = "SELECT user_id FROM registered_users WHERE name = ? AND phone_number = ?";
    private static final String CHECK_SALARY_EXISTS_QUERY = "SELECT COUNT(*) AS count FROM salary_details WHERE user_id = ?";
    private static final String UPDATE_SALARY_QUERY = "UPDATE salary_details SET salary = salary + 10000 WHERE user_id = ?";

    public int registerUser(RegisterBean registerBean) throws ClassNotFoundException, SQLException {
        int userId = checkExistingUser(registerBean.getName(), registerBean.getPhoneNumber());
        if (userId != -1) {
            // User already exists, return their userId
            return userId;
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, registerBean.getName());
            preparedStatement.setString(2, registerBean.getPhoneNumber());
            preparedStatement.setString(3, registerBean.getAddress());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                // Retrieve the generated user ID
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        userId = resultSet.getInt(1);
                    }
                }
            }
            connection.close();
        }
        
        return userId;
    }

    private int checkExistingUser(String name, String phoneNumber) throws SQLException, ClassNotFoundException {
    	Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_EXISTS_QUERY)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phoneNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("user_id");
                }
            }
        }
        return -1; // User does not exist
    }

    public boolean incrementSalary(int userId) throws ClassNotFoundException, SQLException {
        boolean updated = false;
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement checkStatement = connection.prepareStatement(CHECK_SALARY_EXISTS_QUERY)) {
            checkStatement.setInt(1, userId);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt("count") > 0) {
                    // If salary record exists, update the salary by adding 10000
                    try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_SALARY_QUERY)) {
                        updateStatement.setInt(1, userId);
                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            updated = true;
                        }
                    }
                }
            }
            connection.close();
        }
        return updated;
    }

}