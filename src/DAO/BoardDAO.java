package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DB.DBControll;
import DTO.BoardDTO;
import Interface.IBoardDAO;

public class BoardDAO implements IBoardDAO {

	private static BoardDAO instance = null;
	
	public static BoardDAO getGetInstance() {
		
		if ( instance == null ) {
			instance = new BoardDAO();
		}
		
		return instance;
	}
	
	// 모든 게시글 불러오기 ※ 제외: 삭제된 글
	@Override
	public List<BoardDTO> selectAllBoardList() {
		
		String sql = "SELECT * FROM MY_BOARD "
					+ "WHERE B_DEL = ? "
					+ "ORDER BY B_SEQ DESC";
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		List<BoardDTO> boardList = new ArrayList<BoardDTO>();
		
		try {
			
			conn = DBControll.getConnection();
			psmt = conn.prepareStatement(sql);
			
			psmt.setInt(1, 0);
			
			rs = psmt.executeQuery();
			
			while ( rs.next() ) {
				
				int i = 1;
				BoardDTO bdto = new BoardDTO(
								rs.getInt(i++),
								rs.getString(i++),
								rs.getString(i++),
								rs.getString(i++),
								rs.getString(i++),
								rs.getString(i++),
								rs.getInt(i++),
								rs.getTimestamp(i++),
								rs.getInt(i++),
								rs.getInt(i)
								);
				
				boardList.add(bdto);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBControll.closeDatabase(conn, psmt, rs);
		}
		
		return boardList;
	}

	// 글 저장
	@Override
	public boolean insertBoard(BoardDTO bdto) {
		
		String sql = "INSERT INTO MY_BOARD "
					+ "(B_SEQ, B_ID, B_PW, B_TITLE, B_CONTENT, B_FILENAME, "
					+ "B_READCOUNT, B_WRITEDATE, B_NOTICE, B_DEL) "
					+ "VALUES(SEQ_MY_BOARD.NEXTVAL, ?, ?, ?, ?, ?, "
					+ "0, SYSDATE, 0, 0) ";
		
		Connection conn = null;
		PreparedStatement psmt = null;
		
		int count = 0;
		
		try {
			
			conn = DBControll.getConnection();
			psmt = conn.prepareStatement(sql);
			
			int i = 1;
			psmt.setString(i++, bdto.getId());
			psmt.setString(i++, bdto.getPw());
			psmt.setString(i++, bdto.getTitle());
			psmt.setString(i++, bdto.getContent());
			psmt.setString(i++, bdto.getFilename());
			
			count = psmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBControll.closeDatabase(conn, psmt, null);
		}
		
		return count > 0 ? true : false;
	}

}