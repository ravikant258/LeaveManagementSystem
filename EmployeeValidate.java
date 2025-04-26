package lms.leaveManagement.validate;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import lms.leaveManagement.dao.EmployeeDao;
import lms.leaveManagement.dao.EmployeeDaoImpl;
import lms.leaveManagement.model.Employee;

public class EmployeeValidate {

	static EmployeeDao emplDao;

	static {
		emplDao = new EmployeeDaoImpl();
	}

//	String showAllEmploy() throws SQLException, ClassNotFoundException;

	public String showAllEmployDaoVal() throws ClassNotFoundException, SQLException {
		return this.emplDao.showAllEmployDao();
	}

//	String searchEmployDao(int empno) throws ClassNotFoundException, SQLException;
	
	public String searchEmployDaoVal(int empno) throws ClassNotFoundException, SQLException {
		return this.emplDao.searchEmployDao(empno);
	}

//	String applyLeaveDao(int empId ,String  DateLeaveStartDate,String LeaveEndDate,String LeaveType,String  LeaveReason) throws ClassNotFoundException, SQLException;
//
	public String applyLeaveDaoVal(int empId, String DateLeaveStartDate, String LeaveEndDate, String LeaveType,
			String LeaveReason) throws ClassNotFoundException, SQLException {
		
		return this.emplDao.applyLeaveDao(empId,DateLeaveStartDate,LeaveEndDate,LeaveType,LeaveReason);
	}

//	//	String leaveHistory (select * from LeaveHistory where empId=1000)
//	String showleaveHistory(int empId) throws ClassNotFoundException, SQLException;
//	
	public String leaveHistroyByIdDaoVal(int empId) throws ClassNotFoundException, SQLException {
		return this.emplDao.showleaveHistoryDao(empId);
	}
//
//		3) Pending Leave History (DIsplay Managers Subordinates pending leaves history)

	public String showPendingLeaveHistoryDaoVal(int menagerId) throws SQLException, ClassNotFoundException {
		return this.emplDao.showPendingLeaveHistoryDao(menagerId);
	}

//	Accept/Reject (leaveId,ManagerId,Yes/No,ManagerComments)
//	String showPendingLeaveHistory(int mangerId) throws SQLException;
	

	public String leaveApproveByManagerDaoVal(int managerId) {
		return this.leaveApproveByManagerDaoVal(managerId);
	}

	public boolean validateLeaveRequest(String startDateStr, String endDateStr, int leaveBalance) {
		try {
			LocalDate startDate = LocalDate.parse(startDateStr);
			LocalDate endDate = LocalDate.parse(endDateStr);
			LocalDate today = LocalDate.now();

			// 1. Start/End date should not be before today
			if (startDate.isBefore(today) || endDate.isBefore(today)) {
				System.out.println("Leave start or end date cannot be in the past.");
				return false;
			}

			// 2. Start date cannot be after end date
			if (startDate.isAfter(endDate)) {
				System.out.println("Leave start date cannot be after end date.");
				return false;
			}

			// 3. Days must be within available leave balance
			long noOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

			if (noOfDays > leaveBalance) {
				System.out.println(
						"❌ Requested leave days (" + noOfDays + ") exceed available balance (" + leaveBalance + ").");
				return false;
			}

			System.out.println("✅ All validations passed. Total leave days: " + noOfDays);
			return true;

		} catch (DateTimeParseException e) {
			System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
			return false;
		}
	}

}