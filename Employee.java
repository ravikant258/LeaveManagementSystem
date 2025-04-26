package lms.leaveManagement.model;

import java.sql.DatabaseMetaData;
import java.sql.Date;

import lombok.Data;

@Data
public class Employee {

 private	int empid;
 private  String empname;
 private String email;
 private String mobno;
 private Date dateOfJoining;
 private String  dept;
 private int managerId;
 private int leaveAailable;
 
 
 
	
}
