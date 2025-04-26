package lms.leaveManagement.main;
 
import lms.leaveManagement.validate.*;
import java.sql.SQLException;
import java.util.Scanner;

import lms.leaveManagement.dao.EmployeeDao;
import lms.leaveManagement.dao.EmployeeDaoImpl;
import lms.leaveManagement.model.Employee;
import lms.leaveManagement.model.LeaveHistroy;
import lms.leaveManagement.model.LeaveStatus;
import lms.leaveManagement.model.LeaveType;
import lms.leaveManagement.util.ConnectionHelper;


public class Main {
	static EmployeeValidate vlayer;
	static Scanner sc;

	static {
		vlayer = new EmployeeValidate();
		sc = new Scanner(System.in);

	}

	public static void main(String[] args) {
		System.out.println("Hello world!");

		 try {
			showPendingLeaveHistoryMain();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int choice;

		do {
			System.out.println("\n--- Leave Management System ---");
			System.out.println("1. Show All Employees");
			System.out.println("2. Search Employee by ID");
			System.out.println("3. Apply Leave");
			System.out.println("4. Show Leave History");
			System.out.println("5. Show Pending Leave History");
			System.out.println("6. Approve/Reject Leave");
			System.out.println("7. Exit");
			System.out.print("Enter your choice: ");
			choice = sc.nextInt();

			try {
				switch (choice) {
				case 1:
					showAllEmployeeDaoValMain();
					break;
				case 2:
					searchEmployeeByIdMain();
					break;
				case 3:
					applyLeaveDaoValMain();
					break;
				case 4:
					showleaveHistoryByEmpIdMain();
					break;
					
				case 5:
					showPendingLeaveHistoryMain();
					break;
				case 6:
					leaveApproveByManagerMain();
					break;
				case 7:
					System.out.println("Exiting...");
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
				}
			} catch (Exception e) {
				System.out.println("An error occurred: " + e.getMessage());
			}

		} while (choice != 7);
	}

	
 
	public static void showAllEmployeeDaoValMain() throws ClassNotFoundException, SQLException {
		System.out.println("=======show all employee====");
		System.out.println(vlayer.showAllEmployDaoVal());
	}
 	public static void showleaveHistoryByEmpIdMain() throws ClassNotFoundException, SQLException {
		System.out.println("========show the leave history======");
		System.out.println("Enter the empId:");
		Scanner sc = new Scanner(System.in);
		int empid = sc.nextInt();
		System.out.println(vlayer.leaveHistroyByIdDaoVal(empid));
	}

	

	// Search employee by ID
	public static void searchEmployeeByIdMain() throws SQLException, ClassNotFoundException {
		System.out.println("======== Search Employee ========");
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Employee ID: ");
		int empId = sc.nextInt();
		
		System.out.println(vlayer.searchEmployDaoVal(empId));
	}

	// Apply leave
	public static void applyLeaveDaoValMain() throws SQLException, ClassNotFoundException {
		System.out.println("======== Apply Leave ========");
		  LeaveHistroy leave=new LeaveHistroy();
		 try {
	            System.out.print("Enter Leave ID: ");
	            leave.setLeaveId(sc.nextInt());

	            System.out.print("Enter Employee ID: ");
	            leave.setEmpId(sc.nextInt());

	            System.out.print("Enter Number of Leave Days: ");
	            leave.setLeaveNoOfDays(sc.nextInt());
	            sc.nextLine(); // consume newline

	            System.out.print("Enter Leave Reason: ");
	            leave.setLeaveReason(sc.nextLine());

	            System.out.print("Enter Manager Comments: ");
	            leave.setLeaveMngrComments(sc.nextLine());

	            System.out.print("Enter Leave Type (e.g., EL): ");
	            leave.setLeaveType(LeaveType.valueOf(sc.nextLine().toUpperCase()));

	            System.out.print("Enter Leave Status (PENDING, APPROVED, DENIED): ");
	            leave.setLeaveStatus(LeaveStatus.valueOf(sc.nextLine().toUpperCase()));

	            System.out.print("Enter Leave Start Date (yyyy-MM-dd): ");
	            String startDateStr = sc.nextLine();
	            leave.setLeaveStartDate(startDateStr);
	            System.out.print("Enter Leave End Date (yyyy-MM-dd): ");
	            String endDateStr = sc.nextLine();
 	            leave.setLeaveEndDate(endDateStr);

	            System.out.println("\nLeave Details Entered:");
	            System.out.println(leave);
		
		
		
		Employee employee=new Employee();
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Employee ID: ");
		int empId = sc.nextInt();
		sc.nextLine(); // consume newline
		System.out.print("Enter Leave Start Date (yyyy-mm-dd): ");
		String startDate = sc.nextLine();
		System.out.print("Enter Leave End Date (yyyy-mm-dd): ");
		String endDate = sc.nextLine();
		System.out.print("Enter Leave Type (e.g., EL): ");
		String leaveType = sc.nextLine();
		System.out.print("Enter Leave Reason: ");
		String leaveReason = sc.nextLine();
		// Call your DAO method here
		
		System.out.println(vlayer.applyLeaveDaoVal(employee));
  	}



	// Show pending leave history for manager
	public static void showPendingLeaveHistoryMain() throws SQLException, ClassNotFoundException {
		System.out.println("======== Show Pending Leave History ========");
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Manager ID: ");
		int managerId = sc.nextInt();
	    
		System.out.println(vlayer.showPendingLeaveHistoryDaoVal(managerId));
	}

	// Approve or reject leave
	public static void leaveApproveByManagerMain() throws SQLException, ClassNotFoundException {
		System.out.println("======== Approve/Reject Leave ========");
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Manager ID: ");
		int managerId = sc.nextInt();
		
		System.out.println(vlayer.leaveApproveByManagerDaoVal(managerId));
	}

}
