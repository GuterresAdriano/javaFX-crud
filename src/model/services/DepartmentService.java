package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	
	public List<Department> findAll(){
		List<Department> departments = new ArrayList<Department>();
		
		departments.add(new Department(1,"Books"));
		departments.add(new Department(2,"Computer"));
		departments.add(new Department(3,"Electronics"));		
		
		return departments;
	}

}