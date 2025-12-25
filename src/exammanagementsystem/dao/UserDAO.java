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
}
