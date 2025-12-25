package exammanagementsystem.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exammanagementsystem.DatabaseConnection;

public class UserDAO {

	public enum Roles {
		ADMIN(1),
		SUPERVISOR(2),
		PARTICIPANT(3);

		final int value;

		Roles(int i) {
			this.value = i;
		}

		public static Roles fromInt(int i) {
			switch (i) {
				case 1:
					return ADMIN;
				case 2:
					return SUPERVISOR;
				case 3:
					return PARTICIPANT;
			}
			return null;
		}
	}

	public static class User {
		private String id;
		private String pass;
		private Roles role;

		public User(String id, String pass, Roles role) {
			this.id = id;
			this.pass = pass;
			this.role = role;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Roles getRole() {
			return role;
		}

		public void setRole(Roles role) {
			this.role = role;
		}

		public String getPass() {
			return pass;
		}

		public void setPass(String pass) {
			this.pass = pass;
		}

	}

	public void create(User user) throws SQLException {
		String sql = "INSERT INTO users (id, password, role_id) VALUES (?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement create_user = conn.prepareStatement(sql);
			create_user.setString(1, user.id);
			create_user.setString(2, user.pass);
			create_user.setInt(3, user.role.value);

			create_user.executeUpdate();
		}
	}

	public List<User> readNonAdmins() throws SQLException {
		List<User> result = new ArrayList<>();
		String sql = "SELECT users.id as id, users.password as password, users.role_id as role_id FROM users WHERE users.role_id <> 1";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_non_admins = conn.prepareStatement(sql);
			ResultSet found_users = find_non_admins.executeQuery();
			while (found_users.next()) {
				result.add(new User(
						found_users.getString("id"),
						found_users.getString("password"),
						Roles.fromInt(found_users.getInt("role_id"))));
			}
		}
		return result;
	}

	public User readById(String id) throws SQLException {
		User result = null;
		String sql = "SELECT * FROM users WHERE users.id = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_user = conn.prepareStatement(sql);
			find_user.setString(1, id);
			ResultSet found_users = find_user.executeQuery();
			if (found_users.next()) {
				result = new User(
						found_users.getString("id"),
						found_users.getString("password"),
						Roles.fromInt(found_users.getInt("role_id")));
			}
		}

		return result;
	}

	public List<User> readParticipantsByExamId(int id) throws SQLException {
		List<User> result = new ArrayList<>();
		String sql = """
					SELECT users.id as id, users.role_id as role_id
						JOIN (SELECT participants.user_id FROM participants WHERE participants.exam_id = ?) AS sub_participants
					ON users.id = sub_participants.user_id
				""";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_exam_participants = conn.prepareStatement(sql);
			find_exam_participants.setInt(1, id);
			ResultSet found_users = find_exam_participants.executeQuery();
			while (found_users.next()) {
				result.add(new User(
						found_users.getString("id"),
						null,
						Roles.fromInt(found_users.getInt("role_id"))));
			}
		}
		return result;
	}

	public List<User> readSupervisorsByExamId(int id) throws SQLException {
		List<User> result = new ArrayList<>();
		String sql = """
					SELECT users.id as id, users.role_id as role_id
						JOIN (SELECT supervisors.user_id FROM supervisors WHERE supervisors.exam_id = ?) AS sub_supervisors
					ON users.id = sub_supervisors.user_id
				""";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_exam_supervisors = conn.prepareStatement(sql);
			find_exam_supervisors.setInt(1, id);
			ResultSet found_users = find_exam_supervisors.executeQuery();
			while (found_users.next()) {
				result.add(new User(
						found_users.getString("id"),
						null,
						Roles.fromInt(found_users.getInt("role_id"))));
			}
		}
		return result;
	}

	public void update(User user) throws SQLException {
		String sql = "UPDATE users SET users.id = ?, users.password = ?, users.role_id = ? WHERE users.id = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement update_user = conn.prepareStatement(sql);
			update_user.setString(1, user.id);
			update_user.setString(2, user.pass);
			update_user.setInt(3, user.role.value);
			update_user.setString(4, user.id);

			update_user.executeUpdate();
		}
	}

	/**
	 * Delete users and all the associated data: participation/supervision and
	 * results
	 * 
	 * @param id
	 * @throws SQLException
	 */
	public void delete(String id) throws SQLException {
		String sql = "DELETE FROM users WHERE users.id = ?";
		String sql_participants = "DELETE FROM participants WHERE participants.user_id = ?";
		String sql_superivors = "DELETE FROM supervisors WHERE supervisors.user_id = ?";
		String sql_results = "DELETE FROM results WHERE results.user_id = ?";

		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement delete_user = conn.prepareStatement(sql);
			PreparedStatement delete_participants = conn.prepareStatement(sql_participants);
			PreparedStatement delete_supervisors = conn.prepareStatement(sql_superivors);
			PreparedStatement delete_results = conn.prepareStatement(sql_results);
			delete_user.setString(1, id);
			delete_participants.setString(1, id);
			delete_supervisors.setString(1, id);
			delete_results.setString(1, id);

			delete_user.executeUpdate();
			delete_participants.executeUpdate();
			delete_supervisors.executeUpdate();
			delete_results.executeUpdate();
		}
	}
}
