package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dbconn.Dbconn;
import domain.BoardVo;
import domain.Criteria;
import domain.SearchCriteria;


public class BoardDao {

	private Connection conn;
	private PreparedStatement pstmt;

	public BoardDao() {
		Dbconn dbconn = new Dbconn();
		this.conn = dbconn.getConnect();
	}
	
	public ArrayList<BoardVo> boardList(SearchCriteria scri){

		String str="";
		if (scri.getSearchType().equals("subject")) {   //선택한 제목이 넘어오면
			str= "and subject like ?";			
		}else {
			str= "and writer like ?";
		}		
		ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
				
	//	String sql="select *  from board0817 where delyn = 'N' order by originbidx desc, depth asc";
	// 페이징하는 쿼리로 만든다
		
		String sql ="SELECT B.* FROM ("
				+ "	SELECT ROWNUM AS rnum, A.* from ("
				+ "		SELECT * FROM board0817 WHERE DELYN= 'N' "+str+"ORDER BY ORIGINBIDX DESC, DEPTH ASC"
				+ "		)A"
				+ "	)B WHERE B.rnum BETWEEN ? AND ?";	
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try{
			pstmt  = conn.prepareStatement(sql);
			pstmt.setString(1,  "%"+ scri.getKeyword()+"%"  );
			pstmt.setInt(2, (scri.getPage()-1)*scri.getPerPageNum()+1);
			pstmt.setInt(3, scri.getPage()*scri.getPerPageNum());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
			BoardVo bv = new BoardVo();   //객체를 생성을 하고 옮겨담는다
			bv.setBidx(rs.getInt("bidx"));
			bv.setSubject(rs.getString("subject"));
			bv.setWriter(rs.getString("writer"));
			bv.setWriteday(rs.getString("writeday"));
			bv.setViewcnt(rs.getInt("viewcnt"));
			bv.setLevel_(rs.getInt("level_"));
			alist.add(bv);   //각 생성된 객체(bv)를 ArrayList alist 추가한다  				
			}				
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
			//	conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}		
		return alist;
		}	
	
		public int boardInsert(String subject,String contents,String writer,String ip,int midx, String fileName) {
			
			int value=0;
			String sql="insert into board0817(bidx,originbidx,depth,level_,subject,contents,writer,writeday,ip,midx,filename)"
							+"values(bidx_seq.nextval,bidx_seq.nextval,0,0,?,?,?,sysdate,?,?,?)";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, subject);
				pstmt.setString(2, contents);
				pstmt.setString(3, writer);
				pstmt.setString(4, ip);
				pstmt.setInt(5, midx);
				pstmt.setString(6, fileName);
				value = pstmt.executeUpdate();
				
			} catch (SQLException e) {			
				e.printStackTrace();
			}			
			return value;
		}
	
		public BoardVo boardSelectOne(int bidx) {
			BoardVo bv = null;
			ResultSet rs = null;
			String sql = "select * from board0817 where bidx = ?";
			String sql2 = "update board0817 set viewcnt = viewcnt+1 where bidx=?";
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bidx);
				rs  = pstmt.executeQuery();
				
				if (rs.next()) {//커서가 이동을 하고 다음 데이터가 존재한다면 True
					bv = new BoardVo();
					bv.setBidx(rs.getInt("bidx"));   // rs에 있는 값을 bv에 옮겨 담는다
					bv.setSubject(rs.getString("subject"));
					bv.setContents(rs.getString("contents"));
					bv.setWriter(rs.getString("writer"));
					bv.setWriteday(rs.getString("writeday"));
					bv.setOriginbidx(rs.getInt("originbidx"));
					bv.setDepth(rs.getInt("depth"));
					bv.setLevel_(rs.getInt("level_"));
					bv.setViewcnt(rs.getInt("viewcnt"));
					bv.setFilename(rs.getString("filename"));
				}					
				pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, bidx);
				pstmt.executeUpdate();				
				
			} catch (SQLException e) {				
				e.printStackTrace();
			} finally {				
				try {
					rs.close();
					pstmt.close();
					conn.close();
				} catch (SQLException e) {					
					e.printStackTrace();
				}				
			}				
			return bv;
		}
	
		public int boardUpdate(String subject,String contents,String writer,int bidx) {
			
			int value=0;			
			String sql="update board0817 set subject=?, contents=?, writer=? where bidx=?";
				
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, subject);
				pstmt.setString(2, contents);
				pstmt.setString(3, writer);
				pstmt.setInt(4, bidx);
				value = pstmt.executeUpdate();
				
			} catch (SQLException e) {			
				e.printStackTrace();
			}			
			return value;
		}
	
		
		public int boardDelete(int bidx) {
			
			int value=0;
			String sql="UPDATE BOARD0817 SET delYn='Y', writeday=SYSDATE WHERE bidx = ?";
			
			//쿼리화 한다
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bidx);
				value = pstmt.executeUpdate();
				
			} catch (SQLException e) {				
				e.printStackTrace();
			} finally {					
				try {
					pstmt.close();
					conn.close();
				} catch (SQLException e) {					
					e.printStackTrace();
				}
			}
			
			return value;
		}
		
		
		public int boardReply(BoardVo bv) {
			int value=0;
			
			String sql = "update board0817 set depth= depth+1 where originbidx=? and depth >?";
			String sql2 = "insert into board0817(bidx,originbidx,depth,level_,subject,contents,writer,writeday,midx) "+
			                   " values(bidx_seq.nextval,?,?,?,?,?,?,sysdate,? )";
			
			try{
				//트랜잭션 수동
				conn.setAutoCommit(false);
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bv.getOriginbidx());
				pstmt.setInt(2, bv.getDepth());
				pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, bv.getOriginbidx());
				pstmt.setInt(2, bv.getDepth()+1);
				pstmt.setInt(3, bv.getLevel_()+1);
				pstmt.setString(4, bv.getSubject());
				pstmt.setString(5, bv.getContents());
				pstmt.setString(6, bv.getWriter());
				pstmt.setInt(7, bv.getMidx());
				value = pstmt.executeUpdate();
				
				//트랜잭션 일괄실행
				conn.commit();
				
			} catch (SQLException e) {		
				try {
					conn.rollback();
				} catch (SQLException e1) {					
					e1.printStackTrace();
				}
				e.printStackTrace();
			}finally {
				 try {
					 pstmt.close();
					 conn.close();
				} catch (SQLException e) {				
					e.printStackTrace();
				}
			}				
			return value;
		}
	
	public int boardTotal(SearchCriteria scri) {
		
		String str ="";
		if (scri.getSearchType().equals("subject")) {
			str = "and subject like ?";
		}else {
			str = "and writer like ?";
		}	
		
		int cnt= 0;
		String sql="select count(*) as cnt from board0817 where delyn='N' "+str;
		ResultSet rs =null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+scri.getKeyword()+"%");
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				cnt = rs.getInt("cnt");
			}			
		} catch (SQLException e) {		
			e.printStackTrace();
		} finally {			
				try {
					rs.close();
					pstmt.close();
					conn.close();
				} catch (SQLException e) {					
					e.printStackTrace();
				}
		}			
		return cnt;
	}
	
	
	
	
}
