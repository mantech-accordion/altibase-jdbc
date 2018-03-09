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
			
			String url = "jdbc:Altibase://"+serverIp+":"+serverPort+"/" + schema;
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
	
}
