package lms.leaveManagement.model;

import java.sql.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
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
