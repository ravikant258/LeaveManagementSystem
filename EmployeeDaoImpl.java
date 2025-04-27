package lms.leaveManagement.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

import lms.leaveManagement.model.Employee;
import lms.leaveManagement.model.LeaveHistroy;
import lms.leaveManagement.util.ConnectionHelper;

public class EmployeeDaoImpl implements EmployeeDao {

	Connection connection;
	PreparedStatement pst;
   
	

	@Override
	public String showAllEmployDao() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
		connection = ConnectionHelper.getConnection();
		String cmd = "select * from EMPLOYEE";
		pst = connection.prepareStatement(cmd);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			System.out.println("=========employee details  ======");
			System.out.println("empID :" + rs.getInt("EMP_ID"));
			System.out.println("empName : " + rs.getString("EMP_NAME"));
			System.out.println("empMail" + rs.getString("EMP_MAIL"));
			System.out.println("empMobNo:" + rs.getString("EMP_MOB_NO"));
			System.out.println("empDateJoining" + rs.getDate("EMP_DT_JOIN"));
			System.out.println("empDept : " + rs.getString("EMP_DEPT"));
			System.out.println("empManID :" + rs.getInt("EMP_MANAGER_ID"));
			System.out.println("emp Emp Leave : " + rs.getInt("EMP_AVAIL_LEAVE_BAL"));

		}

		return "operation done";
		
	}
	

	@Override
	public String searchEmployDao(int empno) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub

		connection = ConnectionHelper.getConnection();
		String cmd = "select * from EMPLOYEE where emp_id = ?";
		pst = connection.prepareStatement(cmd);
		pst.setInt(1, empno);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			System.out.println("=========employee details  ======");
			System.out.println("empID :" + rs.getInt("EMP_ID"));
			System.out.println("empName : " + rs.getString("EMP_NAME"));
			System.out.println("empMail" + rs.getString("EMP_MAIL"));
			System.out.println("empMobNo:" + rs.getString("EMP_MOB_NO"));
			System.out.println("empDateJoining" + rs.getDate("EMP_DT_JOIN"));
			System.out.println("empDept : " + rs.getString("EMP_DEPT"));
			System.out.println("empManID :" + rs.getInt("EMP_MANAGER_ID"));
			System.out.println("emp Emp Leave : " + rs.getInt("EMP_AVAIL_LEAVE_BAL"));

			return "added successfully";
		}

		return "not added ";
	}


	public String applyLeaveDao(LeaveHistroy leaveHistroy,int managerId) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionHelper.getConnection();

		int leaveDaysWant = noOfDays(leaveHistroy.getLeaveStartDate(),leaveHistroy.getLeaveEndDate());
		
		int availableleave=getLeaveBalance(leaveHistroy.getEmpId());
		
		if (leaveDaysWant < availableleave) {
			

			updateEmployLeave(leaveDaysWant, leaveHistroy.getEmpId());

           System.out.println( insertInLeaveHistory(leaveHistroy));
			
          return "leave is applied sucessfully";
		}
        return "leave is not  applied sucessfully";

	}
		public String insertInLeaveHistory(LeaveHistroy leaveHistroy) throws ClassNotFoundException, SQLException {
			System.out.println("insert history is called");
			Connection connection = ConnectionHelper.getConnection(); // fixed typo

			String sql = "INSERT INTO leave_history (" +
			             "LEAVE_NO_OF_DAYS, LEAVE_MNGR_COMMENTS, EMP_ID, " +
			             "LEAVE_START_DATE, LEAVE_END_DATE, LEAVE_REASON) " +
			             "VALUES (?, ?, ?, ?, ?, ?)";

			PreparedStatement pstmt = connection.prepareStatement(sql);
			System.out.println("sql statement is: " + pstmt);

			pstmt.setInt(1, leaveHistroy.getLeaveNoOfDays());             // LEAVE_NO_OF_DAYS
			pstmt.setString(2, leaveHistroy.getLeaveMngrComments());      // LEAVE_MNGR_COMMENTS
			pstmt.setInt(3, leaveHistroy.getEmpId());                     // EMP_ID
			pstmt.setDate(4, leaveHistroy.getLeaveStartDate());           // LEAVE_START_DATE
			pstmt.setDate(5, leaveHistroy.getLeaveEndDate());             // LEAVE_END_DATE
			pstmt.setString(6, leaveHistroy.getLeaveReason());            // LEAVE_REASON

			int rowsInserted = pstmt.executeUpdate();
			System.out.println(rowsInserted + " row(s) inserted.");


			if (rowsInserted > 0) {
				System.out.println("Leave request submitted successfully.");
				return "Leave request submitted successfully";
			}

			System.out.println("Leave request not submitted successfully.");
			return "Leave request not submitted successfully";

}

		

	public int getLeaveBalance(int empId) throws SQLException, ClassNotFoundException {
		Connection connection = ConnectionHelper.getConnection();
		String sql = "SELECT EMP_AVAIL_LEAVE_BAL FROM EMPLOYEE WHERE EMP_ID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("EMP_AVAIL_LEAVE_BAL");
			} else {
				throw new SQLException("Employee ID not found: " + empId);
			}

		}

	}

	public void updateEmployLeave(int leaveDays, int empId) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE EMPLOYEE SET EMP_AVAIL_LEAVE_BAL = EMP_AVAIL_LEAVE_BAL - ? " +
	             "WHERE EMP_ID = ?";


		try (Connection connection = ConnectionHelper.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(sql)) {

			pstmt.setInt(1, leaveDays); // Leave to subtract
			pstmt.setInt(2, empId); // Employee ID
//			pstmt.setInt(3, leaveDays); // Condition check (available balance)

			// Execute update
			int rowsUpdated = pstmt.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println("✅ Leave balance updated successfully.");
			} else {
				System.out.println("⚠️ Leave update failed — not enough balance or employee not found.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e; // rethrow if you want the caller to handle it
		}
	}

	public int noOfDays(Date startDate, Date endDate) {
		LocalDate start =(startDate).toLocalDate();
		LocalDate end = (endDate).toLocalDate();

		long days = ChronoUnit.DAYS.between(start, end) + 1;

		return (int) days; 
	}

	@Override
	public String showleaveHistoryDao(int empId) throws ClassNotFoundException, SQLException{

		// TODO Auto-generated method stub

		Connection connection = ConnectionHelper.getConnection();

		String sql = "select * from leave_history where EMP_ID= ?";

		pst = connection.prepareStatement(sql);

		pst.setInt(1, empId);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			int leaveId = rs.getInt("LEAVE_ID");
			int leaveDays = rs.getInt("LEAVE_NO_OF_DAYS");
			String leaveMngrComments = rs.getString("LEAVE_MNGR_COMMENTS");
//        int empId = rs.getInt("EMP_ID");
			Date leaveStartDate = rs.getDate("LEAVE_START_DATE");
			Date leaveEndDate = rs.getDate("LEAVE_END_DATE");
			String leaveType = rs.getString("LEAVE_TYPE");
			String leaveStatus = rs.getString("LEAVE_STATUS");
			String leaveReason = rs.getString("LEAVE_REASON");

			// Printing the fetched data for each row
			System.out.println("Leave ID: " + leaveId);
			System.out.println("Number of Days: " + leaveDays);
			System.out.println("Manager's Comments: " + leaveMngrComments);
			System.out.println("Employee ID: " + empId);
			System.out.println("Leave Start Date: " + leaveStartDate);
			System.out.println("Leave End Date: " + leaveEndDate);
			System.out.println("Leave Type: " + leaveType);
			System.out.println("Leave Status: " + leaveStatus);
			System.out.println("Leave Reason: " + leaveReason);
			System.out.println("----------------------------");
		}
		return null;
	}

	@Override
	public String showPendingLeaveHistoryDao(int mangerId) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Connection connection=ConnectionHelper.getConnection();
		System.out.println("showpending leaveHistory is called");
		
		String sql = "SELECT "
		           + "lh.LEAVE_ID, "
		           + "lh.LEAVE_NO_OF_DAYS, "
		           + "lh.LEAVE_MNGR_COMMENTS, "
		           + "lh.EMP_ID, "
		           + "lh.LEAVE_START_DATE, "
		           + "lh.LEAVE_END_DATE, "
		           + "lh.LEAVE_TYPE, "
		           + "lh.LEAVE_STATUS, "
		           + "lh.LEAVE_REASON "
		           + "FROM LEAVE_HISTORY lh "
		           + "JOIN EMPLOYEE e ON lh.EMP_ID = e.EMP_ID "
		           + "WHERE e.EMP_MANAGER_ID = ?";


		
		pst = connection.prepareStatement(sql);
		
		pst.setInt(1, 2001); // Set the value for the placeholder (?)

		// Step 4: Execute the query
		ResultSet rs = pst.executeQuery();

		System.out.println("result is called");
		// Step 5: Process the result set and print the leave history
		while (rs.next()) {
			// Fetching the result for each column
			int leaveId = rs.getInt("LEAVE_ID");
			int leaveDays = rs.getInt("LEAVE_NO_OF_DAYS");
			String leaveMngrComments = rs.getString("LEAVE_MNGR_COMMENTS");
			int empId = rs.getInt("EMP_ID");
			Date leaveStartDate = rs.getDate("LEAVE_START_DATE");
			Date leaveEndDate = rs.getDate("LEAVE_END_DATE");
			String leaveType = rs.getString("LEAVE_TYPE");
			String leaveStatus = rs.getString("LEAVE_STATUS");
			String leaveReason = rs.getString("LEAVE_REASON");

			// Printing the fetched data for each row
			System.out.println("Leave ID: " + leaveId);
			System.out.println("Number of Days: " + leaveDays);
			System.out.println("Manager's Comments: " + leaveMngrComments);
			System.out.println("Employee ID: " + empId);
			System.out.println("Leave Start Date: " + leaveStartDate);
			System.out.println("Leave End Date: " + leaveEndDate);
			System.out.println("Leave Type: " + leaveType);
			System.out.println("Leave Status: " + leaveStatus);
			System.out.println("Leave Reason: " + leaveReason);
			System.out.println("----------------------------");
		}

		return "all epmployee of managerId"+mangerId;
	}


	@Override

    public 	String leaveApproveByManagerDao(int managerId) throws SQLException, ClassNotFoundException{

		String sql = "SELECT " + "lh.LEAVE_ID, " + "lh.LEAVE_NO_OF_DAYS, " + "lh.LEAVE_MNGR_COMMENTS, " + "lh.EMP_ID, "
				+ "lh.LEAVE_START_DATE, " + "lh.LEAVE_END_DATE, " + "lh.LEAVE_TYPE, " + "lh.LEAVE_STATUS, "
				+ "lh.LEAVE_REASON " + "FROM LEAVE_HISTORY lh " + "JOIN EMPLOYEE e ON lh.EMP_ID = e.EMP_ID "
				+ "WHERE e.EMP_MANAGER_ID = ?";

		pst = connection.prepareStatement(sql);
		pst.setInt(1, managerId); // Set the value for the placeholder (?)

		pst = connection.prepareStatement(sql);
		pst.setInt(1, managerId); 


		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			int empId = rs.getInt("EMP_ID");
			int leaveId = rs.getInt("LEAVE_ID");
			Date leaveStartDate = rs.getDate("LEAVE_START_DATE");
			Date leaveEndDate = rs.getDate("LEAVE_END_DATE");
			String leaveType = rs.getString("LEAVE_TYPE");
			String leaveStatus = rs.getString("LEAVE_STATUS");
			String leaveReason = rs.getString("LEAVE_REASON");
            System.out.println("Leave ID: " + leaveId);
            System.out.println("Employee ID: " + empId);
			System.out.println("Leave Start Date: " + leaveStartDate);
			System.out.println("Leave End Date: " + leaveEndDate);
			System.out.println("Leave Type: " + leaveType);
			System.out.println("Leave Status: " + leaveStatus);
			System.out.println("Leave Reason: " + leaveReason);
			System.out.println("----------------------------");
			
			if(helpLeaveApproveToManagerDao(leaveId)) {
				System.out.print("check leave history");
			}
			else {
				System.out.println("mangager not accepted ");
			}
		}

		return "manger respnsed your leave";
	}

	public boolean helpLeaveApproveToManagerDao(int leaveId) throws ClassNotFoundException, SQLException {
		
		Connection connection = ConnectionHelper.getConnection();
		Scanner sc = new Scanner(System.in);

		System.out.println("enter the you response enum('PENDING','APPROVED','DENIED')");

		String LEAVE_STATUS = sc.next();
		if (LEAVE_STATUS.equals("PENDING")) {
			return false;
		}
		System.out.println("Enter the your  LEAVE_MNGR_COMMENTS");
		String LEAVE_MNGR_COMMENTS = sc.next();
		

		String sql = "UPDATE leave_history SET LEAVE_MNGR_COMMENTS = ?, LEAVE_STATUS = ? WHERE LEAVE_ID = ?";

		
		pst = connection.prepareStatement(sql);

		pst.setString(1, LEAVE_MNGR_COMMENTS);
		pst.setString(2, LEAVE_STATUS);
		pst.setInt(3, leaveId);

		int leaveUpdate = pst.executeUpdate();

		if (leaveUpdate > 0) {
			return true;
		}

		return false;
		
	}


}
