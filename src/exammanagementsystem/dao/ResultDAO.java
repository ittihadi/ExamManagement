package exammanagementsystem.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exammanagementsystem.DatabaseConnection;

public class ResultDAO {

	public static class Result {
		private int exam_id;
		private String user_id;
		private int number;
		private String answer;
		private float score;

		public Result(int exam_id, String user_id, int number, String answer, float score) {
			this.exam_id = exam_id;
			this.user_id = user_id;
			this.number = number;
			this.answer = answer;
			this.score = score;
		}

		public int getExam_id() {
			return exam_id;
		}

		public void setExam_id(int exam_id) {
			this.exam_id = exam_id;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}

		public float getScore() {
			return score;
		}

		public void setScore(float score) {
			this.score = score;
		}

	}

	public void create(Result result) throws SQLException {
		String sql = "INSERT INTO results (exam_id, user_id, number, answer, score) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement create_result = conn.prepareStatement(sql);
			create_result.setInt(1, result.exam_id);
			create_result.setString(2, result.user_id);
			create_result.setInt(3, result.number);
			create_result.setString(4, result.answer);
			create_result.setFloat(5, result.score);

			create_result.executeUpdate();
		}
	}

	public List<Result> readByExam(int exam_id) throws SQLException {
		List<Result> result = new ArrayList<>();
		String sql = "SELECT * FROM results WHERE results.exam_id = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_results = conn.prepareStatement(sql);
			find_results.setInt(1, exam_id);
			ResultSet found_results = find_results.executeQuery();
			while (found_results.next()) {
				result.add(new Result(
						found_results.getInt("exam_id"),
						found_results.getString("user_id"),
						found_results.getInt("number"),
						found_results.getString("answer"),
						found_results.getFloat("score")));
			}
		}
		return result;
	}

	public void update(Result result) throws SQLException {
		String sql = "UPDATE results SET results.exam_id = ?, results.user_id = ?, results.number = ?, results.answer = ?, results.score = ? WHERE results.exam_id = ? AND results.user_id = ? AND results.number = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement update_result = conn.prepareStatement(sql);
			update_result.setInt(1, result.exam_id);
			update_result.setString(2, result.user_id);
			update_result.setInt(3, result.number);
			update_result.setString(4, result.answer);
			update_result.setFloat(5, result.score);
			update_result.setInt(6, result.exam_id);
			update_result.setString(7, result.user_id);
			update_result.setInt(8, result.number);

			update_result.executeUpdate();
		}
	}

	/**
	 * Deletes specific user result
	 * 
	 * @param exam_id
	 * @param user_id
	 * @param number
	 * @throws SQLException
	 */
	public void delete(int exam_id, String user_id, int number) throws SQLException {
		String sql = "DELETE FROM results WHERE results.exam_id = ? AND results.user_id = ? AND results.number = ?";

		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement delete_result = conn.prepareStatement(sql);
			delete_result.setInt(1, exam_id);
			delete_result.setString(2, user_id);
			delete_result.setInt(3, number);

			delete_result.executeUpdate();
		}
	}
        
        public List<Result> readByExamAndUser(int examId, String userId) throws SQLException {
            List<Result> list = new ArrayList<>();
            String sql = "SELECT * FROM results WHERE exam_id = ? AND user_id = ?";

            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, examId);
                ps.setString(2, userId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    list.add(new Result(
                        rs.getInt("exam_id"),
                        rs.getString("user_id"),
                        rs.getInt("number"),
                        rs.getString("answer"),
                        rs.getFloat("score")
                    ));
                }
            }
            return list;
        }
}
