package exammanagementsystem.dao;

import java.util.ArrayList;
import java.util.List;
import java.awt.desktop.QuitStrategy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exammanagementsystem.DatabaseConnection;

public class QuestionDAO {

	public enum QuestionType {
		OBJECTIVE,
		ESSAY;
	}

	public static class Question {
		private int exam_id;
		private int number;
		private String content;
		private String correct_answer;
		private QuestionType type;

		public Question(int exam_id, int number, String content, String correct_answer, QuestionType type) {
			this.exam_id = exam_id;
			this.number = number;
			this.content = content;
			this.correct_answer = correct_answer;
			this.type = type;
		}

		public int getExam_id() {
			return exam_id;
		}

		public void setExam_id(int exam_id) {
			this.exam_id = exam_id;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getCorrectAnswer() {
			return correct_answer;
		}

		public void setCorrectAnswer(String correct_answer) {
			this.correct_answer = correct_answer;
		}

		public QuestionType getType() {
			return type;
		}

		public void setType(QuestionType type) {
			this.type = type;
		}

	}

	public void create(Question question) throws SQLException {
		String sql = "INSERT INTO questions (exam_id, number, content, correct_answer, type) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement create_question = conn.prepareStatement(sql);
			String type = question.type.toString();

			create_question.setInt(1, question.exam_id);
			create_question.setInt(2, question.number);
			create_question.setString(3, question.content);
			create_question.setString(4, question.correct_answer);
			create_question.setString(5, type);

			create_question.executeUpdate();
		}
	}

	public List<Question> readByExam(int exam_id) throws SQLException {
		List<Question> result = new ArrayList<>();
		String sql = "SELECT * FROM questions WHERE questions.exam_id = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_results = conn.prepareStatement(sql);
			find_results.setInt(1, exam_id);
			ResultSet found_results = find_results.executeQuery();
			while (found_results.next()) {
				String type = found_results.getString("type");
				QuestionType qs_type = QuestionType.OBJECTIVE;

				if (type.equals("OBJECTIVE")) {
					qs_type = QuestionType.OBJECTIVE;
				} else if (type.equals("ESSAY")) {
					qs_type = QuestionType.ESSAY;
				}

				result.add(new Question(
						found_results.getInt("exam_id"),
						found_results.getInt("number"),
						found_results.getString("content"),
						found_results.getString("correct_answer"),
						qs_type));
			}
		}
		return result;
	}

	public Question readByExamNumber(int exam_id, int number) throws SQLException {
		Question result = null;
		String sql = "SELECT * FROM questions WHERE questions.exam_id = ? AND questions.number = ?";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement find_results = conn.prepareStatement(sql);
			find_results.setInt(1, exam_id);
			find_results.setInt(2, number);
			ResultSet found_results = find_results.executeQuery();
			if (found_results.next()) {
				String type = found_results.getString("type");
				QuestionType qs_type = QuestionType.OBJECTIVE;

				if (type.equals("OBJECTIVE")) {
					qs_type = QuestionType.OBJECTIVE;
				} else if (type.equals("ESSAY")) {
					qs_type = QuestionType.ESSAY;
				}

				result = new Question(
						found_results.getInt("exam_id"),
						found_results.getInt("number"),
						found_results.getString("content"),
						found_results.getString("correct_answer"),
						qs_type);
			}
		}
		return result;
	}

	public void update(Question question) throws SQLException {
		String sql = """
				UPDATE questions SET
				    exam_id = ?, number = ?, content = ?, correct_answer = ?, type = ?
				WHERE exam_id = ? AND number = ?
				""";
		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement update_question = conn.prepareStatement(sql);
			update_question.setInt(1, question.exam_id);
			update_question.setInt(2, question.number);
			update_question.setString(3, question.content);
			update_question.setString(4, question.correct_answer);
			update_question.setString(5, question.type.toString());
			update_question.setInt(6, question.exam_id);
			update_question.setInt(7, question.number);

			update_question.executeUpdate();
		}
	}

	/**
	 * Delete question and all the associated data: results
	 * 
	 * @throws SQLException
	 */
	public void delete(int exam_id, int number) throws SQLException {
		String sql = "DELETE FROM questions WHERE questions.exam_id = ? AND questions.number = ?";
		String sql_result = "DELETE FROM results WHERE exam_id = ? AND number = ?";

		try (Connection conn = DatabaseConnection.getConnection()) {
			PreparedStatement delete_question = conn.prepareStatement(sql);
			PreparedStatement delete_results = conn.prepareStatement(sql_result);
			delete_question.setInt(1, exam_id);
			delete_question.setInt(2, number);

			delete_results.setInt(1, exam_id);
			delete_results.setInt(2, number);

			delete_question.executeUpdate();
			delete_results.executeUpdate();
		}
	}
}
