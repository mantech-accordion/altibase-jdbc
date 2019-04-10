package com.mantech.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JdbcController {
	
	private static final Logger logger = LoggerFactory.getLogger(JdbcController.class);
	
	/**
	 * Simply selects the index view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Locale locale, Model model) {
		logger.info("index page");
		return "index";
	}
	
	/**
	 * sql execute
	 */
	@RequestMapping(value = "execute", method = RequestMethod.POST)
	public ModelAndView execute(@RequestParam(value="crud") String crud,
								@RequestParam(value="serverIp") String serverIp, @RequestParam(value="serverPort") String serverPort,
								@RequestParam(value="id") String id, @RequestParam(value="password") String password,
								@RequestParam(value="jdbcDriver") String jdbcDriver, @RequestParam(value="schema") String schema,
								@RequestParam(value="sql") String sql) {
		ModelAndView mav= new ModelAndView();
		String error = null;
		
		logger.info("sql execute");
		logger.info("crud = {}", crud);
		logger.info("serverIp = {}", serverIp);
		logger.info("serverPort = {}", serverPort);
		logger.info("id = {}", id);
		logger.info("password = {}", password);
		logger.info("jdbcDriver = {}", jdbcDriver);
		logger.info("schema = {}", schema);
		logger.info("sql = {}", sql);
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName(jdbcDriver);
			
			//String url = "jdbc:Altibase://"+serverIp+":"+serverPort+"/" + schema;
			String url = "jdbc:mysql://"+serverIp+":"+serverPort+"/" + schema;
			con = DriverManager.getConnection(url, id, password);
			
			ResultSet rs = null;
			List<String> columeList = new ArrayList<String>();
			List<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
			
			pstmt = con.prepareStatement(sql);
			
			int successRow = 0;
			if("s".equals(crud)) {
				rs = pstmt.executeQuery();
				
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCtn = rsmd.getColumnCount();
				
				for(int i = 0; i < columnCtn; i++) {
					columeList.add(rsmd.getColumnName(i+1));
				}
				
				ArrayList<String> data = null;
				
				while(rs.next() && rs.getRow() <= 10) {
					data = new ArrayList<String>();
					for(int i = 0; i < columnCtn; i++) {
						data.add(rs.getString(i + 1));
					}
					dataList.add(data);
				}
				
				mav.addObject("colums", columeList);
				mav.addObject("datas", dataList);
			} else {
				successRow = pstmt.executeUpdate();
				mav.addObject("row", successRow);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getMessage();
		} finally {
			try {
				if(con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		mav.addObject("error", error);
		mav.setViewName("jsonView");
		logger.info(mav.toString());
		return mav;
	}
	
	/**
	 * monitoring
	 */
	@RequestMapping(value = "monitoring", method = RequestMethod.POST)
	public ModelAndView monitoring(HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		String error = null;
		
		logger.info("monitoring page");
		String serverIp = request.getParameter("serverIp");
		String serverPort = request.getParameter("serverPort");
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String jdbcDriver = request.getParameter("jdbcDriver");
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		List<String> sessionColList = new ArrayList<String>();
		List<String> transactionColList = new ArrayList<String>();
		List<String> memoryColList = new ArrayList<String>();
		List<String> tablespaceColList = new ArrayList<String>();
		List<String> replicationColList = new ArrayList<String>();
		
		List<ArrayList<String>> sessionList = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> transactionList = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> memoryList = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> tablespaceList = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> replicationList = new ArrayList<ArrayList<String>>();
		
		try{
			String sessionSql = null;
			String transactionSql = null;
			String memorySql = null;
			String tablespaceSql = null;
			String replicationSql = null;
			
			Class.forName(jdbcDriver);
			
			String url = "jdbc:Altibase://"+serverIp+":"+serverPort+"/mydb";
			con = DriverManager.getConnection(url, id, password);
			
			ResultSet rs = null;
			ArrayList<String> data = null;
			
			// session info
			sessionSql = "SELECT A.ID SESSION_ID ";
			sessionSql += "	, A.DB_USERNAME USER_NAME ";
			sessionSql += "	, REPLACE2(REPLACE2(A.COMM_NAME, 'SOCKET-', NULL), '-SERVER', NULL) CLIENT_IP ";
			sessionSql += "	, A.CLIENT_APP_INFO ";
			sessionSql += "	, A.CLIENT_PID ";
			sessionSql += "	, A.SESSION_STATE ";
			sessionSql += "	, DECODE(A.AUTOCOMMIT_FLAG, 1, 'ON', 'OFF') AUTOCOMMIT ";
			sessionSql += "	, DECODE(A.LOGIN_TIME, 0, '-', TO_CHAR(TO_DATE('1970010109', 'YYYYMMDDHH') + A.LOGIN_TIME / (24*60*60), 'YY/MM/DD HH:MI:SS')) LOGIN_TIME ";
			sessionSql += "	, DECODE(A.IDLE_START_TIME, 0, '-', TO_CHAR(TO_DATE('1970010109', 'YYYYMMDDHH') + A.IDLE_START_TIME / (24*60*60), 'YY/MM/DD HH:MI:SS')) IDLE_TIME ";
			sessionSql += "	, NVL(LTRIM(B.QUERY), 'NONE') CURRENT_QUERY ";
			sessionSql += "FROM V$SESSION A LEFT OUTER JOIN V$STATEMENT B ON A.CURRENT_STMT_ID = B.ID ;";
			
			rs = execute(con, sessionSql);
			
			sessionColList = getColList(rs);
			sessionList= getList(sessionColList, rs);
			
			transactionSql = "SELECT TX.ID TX_ID ";
			transactionSql += "     , WAIT_FOR_TRANS_ID BLOCKED_TX_ID ";
			transactionSql += "	     , DECODE(TX.STATUS, ";
			transactionSql += "	                 0, 'BEGIN', ";
			transactionSql += "	                 1, 'PRECOMMIT', ";
			transactionSql += "	                 2, 'COMMIT_IN_MEMORY', ";
			transactionSql += "	                 3, 'COMMIT', ";
			transactionSql += "	                 4, 'ABORT', ";
			transactionSql += "	                 5, 'BLOCKED', ";
			transactionSql += "	                 6, 'END') STATUS ";
			transactionSql += "	     , DECODE(TX.LOG_TYPE, 0, U1.USER_NAME, 'REPLICATION') USER_NAME ";
			transactionSql += "	     , DECODE(TX.LOG_TYPE, 0, TX.SESSION_ID, RT.REP_NAME) SESSION_ID ";
			transactionSql += "	     , DECODE(TX.LOG_TYPE, 0, ST.COMM_NAME, RR.PEER_IP) CLIENT_IP ";
			transactionSql += "	     , DECODE(ST.AUTOCOMMIT_FLAG, 1, 'ON', 'OFF') AUTOCOMMIT ";
			transactionSql += "	     , L.LOCK_DESC ";
			transactionSql += "	     , DECODE(TX.FIRST_UPDATE_TIME, ";
			transactionSql += "	                 0, '0', ";
			transactionSql += "	                 TO_CHAR(TO_DATE('1970010109', 'YYYYMMDDHH') + TX.FIRST_UPDATE_TIME / (60*60*24), 'MM/DD HH:MI:SS')) FIRST_UPDATE_TIME ";
			transactionSql += "	     , U2.USER_NAME||'.'||T.TABLE_NAME TABLE_NAME ";
			transactionSql += "	     , DECODE(TX.LOG_TYPE, 0, SUBSTR(ST.QUERY, 1, 10), 'REMOTE TX_ID '||REMOTE_TID) CURRENT_QUERY ";
			transactionSql += "	     , DECODE(TX.DDL_FLAG, 0, 'NON-DDL', 'DDL') DDL ";
			transactionSql += "	     , DECODE(TX.FIRST_UNDO_NEXT_LSN_FILENO, -1, '-', TX.FIRST_UNDO_NEXT_LSN_FILENO) 'LOGFILE#' ";
			transactionSql += "	  FROM V$TRANSACTION TX, ";
			transactionSql += "	       V$LOCK L ";
			transactionSql += "	       LEFT OUTER JOIN (SELECT ST.*, SS.AUTOCOMMIT_FLAG, SS.DB_USERID, SS.COMM_NAME ";
			transactionSql += "	                          FROM V$STATEMENT ST, V$SESSION SS ";
			transactionSql += "	                         WHERE SS.ID = ST.SESSION_ID ";
			transactionSql += "	                           AND SS.CURRENT_STMT_ID = ST.ID) ST ON L.TRANS_ID = ST.TX_ID ";
			transactionSql += "	       LEFT OUTER JOIN V$REPRECEIVER_TRANSTBL RT ON L.TRANS_ID = RT.LOCAL_TID ";
			transactionSql += "	       LEFT OUTER JOIN V$REPRECEIVER RR ON RT.REP_NAME = RR.REP_NAME ";
			transactionSql += "	       LEFT OUTER JOIN V$LOCK_WAIT LW ON L.TRANS_ID = LW.TRANS_ID ";
			transactionSql += "	       LEFT OUTER JOIN SYSTEM_.SYS_USERS_ U1 ON ST.DB_USERID = U1.USER_ID, ";
			transactionSql += "	       SYSTEM_.SYS_TABLES_ T ";
			transactionSql += "	       LEFT OUTER JOIN SYSTEM_.SYS_USERS_ U2 ON T.USER_ID = U2.USER_ID ";
			transactionSql += "	 WHERE TX.ID = L.TRANS_ID ";
			transactionSql += "	   AND T.TABLE_OID = L.TABLE_OID ";
			transactionSql += "	   AND TX.STATUS != 6 ";
			transactionSql += "	ORDER BY TX.ID, ST.ID, TX.FIRST_UPDATE_TIME DESC ;  ";
			
			rs = execute(con, transactionSql);
			
			transactionColList = getColList(rs);
			transactionList= getList(transactionColList, rs);
			
			memorySql = "SELECT NAME ";
			memorySql += "     , ROUND(ALLOC_SIZE/1024/1024) 'ALLOC(M)' ";
			memorySql += "	   , ROUND(MAX_TOTAL_SIZE/1024/1024) 'MAX_TOTAL(M)' ";
			memorySql += " FROM V$MEMSTAT ";
			memorySql += "ORDER BY 3 DESC ;";
			
			rs = execute(con, memorySql);
			
			memoryColList = getColList(rs);
			memoryList= getList(memoryColList, rs);
			
			tablespaceSql = "SELECT ID TBS_ID ";
			tablespaceSql += "      , DECODE(TYPE, 0, 'MEMORY_DICTIONARY', 1, 'MEMORY_SYS_DATA', 2, 'MEMORY_USER_DATA', 8, 'VOLATILE_USER_DATA') TBS_TYPE ";
			tablespaceSql += "     , NAME TBS_NAME ";
			tablespaceSql += "     , ROUND( DECODE(M.MAXSIZE, 140737488322560, D.MEM_MAX_DB_SIZE , 0 , T.TOTAL_PAGE_COUNT * T.PAGE_SIZE, M.MAXSIZE) /1024/1024, 2 ) 'MAX(M)' ";
			tablespaceSql += "     , ROUND( M.ALLOC_PAGE_COUNT * T.PAGE_SIZE / 1024 / 1024, 2) 'TOTAL(M)' ";
			tablespaceSql += "     , ROUND(NVL(M.ALLOC_PAGE_COUNT-M.FREE_PAGE_COUNT,T.TOTAL_PAGE_COUNT)*PAGE_SIZE/1024/1024, 2) 'ALLOC(M)' ";
			tablespaceSql += "     , NVL(MT.USED, 0) 'USED(M)' ";
			tablespaceSql += "     , ROUND(DECODE(MAXSIZE, 140737488322560, (M.ALLOC_PAGE_COUNT-M.FREE_PAGE_COUNT)*T.PAGE_SIZE/ D.MEM_MAX_DB_SIZE ,0, (M.ALLOC_PAGE_COUNT-M.FREE_PAGE_COUNT) / T.TOTAL_PAGE_COUNT , (M.ALLOC_PAGE_COUNT-M.FREE_PAGE_COUNT) * T.PAGE_SIZE/ M.MAXSIZE) * 100 , 2) 'USAGE(%)' ";
			tablespaceSql += "     , DECODE(T.STATE,1,'OFFLINE',2,'ONLINE',5,'OFFLINE BACKUP',6,'ONLINE BACKUP',128,'DROPPED', 'DISCARDED') STATE ";
			tablespaceSql += "     , DECODE(M.AUTOEXTEND_MODE,1,'ON','OFF') 'AUTOEXTEND' ";
			tablespaceSql += "  FROM V$DATABASE D ";
			tablespaceSql += "     , V$TABLESPACES T ";
			tablespaceSql += "     , (SELECT SPACE_ID ";
			tablespaceSql += "             , SPACE_NAME ";
			tablespaceSql += "             , ALLOC_PAGE_COUNT ";
			tablespaceSql += "             , FREE_PAGE_COUNT ";
			tablespaceSql += "             , DECODE(MAX_SIZE, 0, (SELECT VALUE1 FROM V$PROPERTY WHERE NAME = 'VOLATILE_MAX_DB_SIZE'), MAX_SIZE) AS MAXSIZE ";
			tablespaceSql += "             , AUTOEXTEND_MODE ";
			tablespaceSql += "          FROM V$VOL_TABLESPACES ";
			tablespaceSql += "         UNION ";
			tablespaceSql += "        SELECT SPACE_ID ";
			tablespaceSql += "             , SPACE_NAME ";
			tablespaceSql += "             , ALLOC_PAGE_COUNT ";
			tablespaceSql += "             , FREE_PAGE_COUNT ";
			tablespaceSql += "             , MAXSIZE ";
			tablespaceSql += "             , AUTOEXTEND_MODE ";
			tablespaceSql += "          FROM V$MEM_TABLESPACES ) M LEFT OUTER JOIN(SELECT TABLESPACE_ID, ROUND(SUM((FIXED_USED_MEM + VAR_USED_MEM))/(1024*1024),3) USED ";
			tablespaceSql += "          FROM V$MEMTBL_INFO ";
			tablespaceSql += "         GROUP BY TABLESPACE_ID ) MT ON M.SPACE_ID = MT.TABLESPACE_ID ";
			tablespaceSql += " WHERE T.ID = M.SPACE_ID;";
			
			rs = execute(con, tablespaceSql);
			
			tablespaceColList = getColList(rs);
			tablespaceList= getList(tablespaceColList, rs);
			
			replicationSql = "SELECT REPLICATION_NAME REP_NAME ";
			replicationSql += "      , LOCAL_USER_NAME||'.'||LOCAL_TABLE_NAME LOCAL_TBL ";
			replicationSql += "     , REMOTE_USER_NAME||'.'||REMOTE_TABLE_NAME REMOTE_TBL ";
			replicationSql += "  FROM SYSTEM_.SYS_REPL_ITEMS_ ";
			replicationSql += " ORDER BY 1, 2 ;";
			
			rs = execute(con, replicationSql);
			
			replicationColList = getColList(rs);
			replicationList= getList(replicationColList, rs);
		
		}catch (Exception e) {
			e.printStackTrace();
			error = e.getMessage();
		} finally {
			try {
				if(con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		mav.addObject("sessionColList", sessionColList);
		mav.addObject("sessionList", sessionList);
		mav.addObject("transactionColList", transactionColList);
		mav.addObject("transactionList", transactionList);
		mav.addObject("memoryColList", memoryColList);
		mav.addObject("memoryList", memoryList);
		mav.addObject("tablespaceColList", tablespaceColList);
		mav.addObject("tablespaceList", tablespaceList);
		mav.addObject("replicationColList", replicationColList);
		mav.addObject("replicationList", replicationList);
		
		mav.setViewName("monitoring");
		
		return mav;
	}
	
	private ResultSet execute(Connection con, String sql){
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;
		try {
		
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	
	
	private List<String> getColList(ResultSet rs){
		
		List<String> cols = new ArrayList<String>();
		try {
		
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCtn = rsmd.getColumnCount();
			
			for(int i = 0; i < columnCtn; i++) {
					cols.add(rsmd.getColumnName(i+1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cols;
		
	}
	
	private List<ArrayList<String>> getList(List<String> cols, ResultSet rs) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		ArrayList<String> data = new ArrayList<String>();
		
		try {
		
			int cSize = cols.size();
			
			while(rs.next() && rs.getRow() <= 10) {
				data = new ArrayList<String>();
				for(int i = 0; i < cSize; i++) {
					data.add(rs.getString(i + 1));
				}
				list.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	

	
}
