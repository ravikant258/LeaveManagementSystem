package lms.leaveManagement.model;

import java.sql.Date;

import lombok.Data;

@Data
public class LeaveHistroy {
	 private int leaveId;
	    private int leaveNoOfDays;
	    private String leaveMngrComments;
	    private int empId;
	    private Date leaveStartDate;
	    private Date leaveEndDate;
	    private LeaveType leaveType;
	    private LeaveStatus leaveStatus;
	    private String leaveReason;

}
