package exammanagementsystem.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import exammanagementsystem.DatabaseConnection;

public class ExamDAO {

	public static class Exam {

		private int id;
		private String title;
		private String description;
		private Timestamp start_time;
		private Timestamp end_time;

		public Exam(int id, String title, String description, Timestamp start_time, Timestamp end_time) {
			this.id = id;
			this.title = title;
			this.description = description;
			this.start_time = start_time;
			this.end_time = end_time;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Timestamp getStart_time() {
			return start_time;
		}

		public void setStart_time(Timestamp start_time) {
			this.start_time = start_time;
		}

		public Timestamp getEnd_time() {
			return end_time;
		}

		public void setEnd_time(Timestamp end_time) {
			this.end_time = end_time;
		}
	}

	public void create(Exam exam) throws SQLException {
		String sql = "INSERT INTO exams (title, description, start_time, end_time) VALUES (?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement create_exam = conn.prepareStatement(sql);
			// create_exam.setInt(1, exam.id);
			create_exam.setString(1, exam.title);
			create_exam.setString(2, exam.description);
			create_exam.setTimestamp(3, exam.start_time);
			create_exam.setTimestamp(4, exam.end_time);

			create_exam.executeUpdate();
		}
	}

	public List<Exam> readAll() throws SQLException {
		List<Exam> result = new ArrayList<>();
		String sql = "SELECT * FROM exams";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_exams = conn.prepareStatement(sql);
			ResultSet exams = find_exams.executeQuery();
			while (exams.next()) {
				result.add(new Exam(
						exams.getInt("id"),
						exams.getString("title"),
						exams.getString("description"),
						exams.getTimestamp("start_time"),
						exams.getTimestamp("end_time")));
			}
		}
		return result;
	}

	public Exam readById(int id) throws SQLException {
		Exam result = null;
		String sql = "SELECT * FROM exams WHERE exams.id = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_exam = conn.prepareStatement(sql);
			find_exam.setInt(1, id);
			ResultSet exam = find_exam.executeQuery();
			if (exam.next()) {
				result = new Exam(
						exam.getInt("id"),
						exam.getString("title"),
						exam.getString("description"),
						exam.getTimestamp("start_time"),
						exam.getTimestamp("end_time"));
			}
		}

		return result;
	}

	public List<Exam> readByParticipant(String user_id) throws SQLException {
		List<Exam> result = new ArrayList<>();
		// TODO: This solution isn't good, improve by doing a big single statement
		// using sub joins to filter
		String participant_exams_sql = "SELECT participants.exam_id as exam_id FROM participants WHERE participants.user_id = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_exam_ids = conn.prepareStatement(participant_exams_sql);
			find_exam_ids.setString(1, user_id);

			ResultSet exam_ids = find_exam_ids.executeQuery();

			while (exam_ids.next()) {
				result.add(readById(exam_ids.getInt("exam_id")));
			}
		}
		return result;
	}

	public List<Exam> readBySupervisor(String user_id) throws SQLException {
		List<Exam> result = new ArrayList<>();
		// TODO: This solution isn't good, improve by doing a big single statement
		// using sub joins to filter
		String supervisor_exams_sql = "SELECT supervisors.exam_id as exam_id FROM supervisors WHERE supervisors.user_id = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_exam_ids = conn.prepareStatement(supervisor_exams_sql);
			find_exam_ids.setString(1, user_id);

			ResultSet exam_ids = find_exam_ids.executeQuery();

			while (exam_ids.next()) {
				result.add(readById(exam_ids.getInt("exam_id")));
			}
		}
		return result;
	}

	public void update(Exam exam) throws SQLException {
		String sql = "UPDATE exams SET "
				+ "exams.id = ?, exams.title = ?, exams.description = ?, exams.start_time = ?, exams.end_time = ? "
				+ "WHERE exams.id = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement update_exam = conn.prepareStatement(sql);
			update_exam.setInt(1, exam.id);
			update_exam.setString(2, exam.title);
			update_exam.setString(3, exam.description);
			update_exam.setTimestamp(4, exam.start_time);
			update_exam.setTimestamp(5, exam.end_time);
			update_exam.setInt(6, exam.id);

			update_exam.executeUpdate();
		}
	}

	/**
	 * Deletes the exam and all its dependents, including participants, supervisors,
	 * results, and questions
	 * 
	 * @param id Exam ID to delete
	 * @throws SQLException
	 */
	public void delete(int id) throws SQLException {
		String sql_exam = "DELETE FROM exams WHERE exams.id = ?";
		String sql_participants = "DELETE FROM participants WHERE participants.exam_id = ?";
		String sql_supervisors = "DELETE FROM supervisors WHERE supervisors.exam_id = ?";
		String sql_results = "DELETE FROM results WHERE results.exam_id = ?";
		String sql_questions = "DELETE FROM questions WHERE questions.exam_id = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement delete_exam = conn.prepareStatement(sql_exam);
			PreparedStatement delete_participants = conn.prepareStatement(sql_participants);
			PreparedStatement delete_supervisors = conn.prepareStatement(sql_supervisors);
			PreparedStatement delete_results = conn.prepareStatement(sql_results);
			PreparedStatement delete_questions = conn.prepareStatement(sql_questions);

			delete_exam.setInt(1, id);
			delete_participants.setInt(1, id);
			delete_supervisors.setInt(1, id);
			delete_results.setInt(1, id);
			delete_questions.setInt(1, id);

			delete_exam.executeUpdate();
			delete_participants.executeUpdate();
			delete_supervisors.executeUpdate();
			delete_results.executeUpdate();
			delete_questions.executeUpdate();
		}
	}
}
