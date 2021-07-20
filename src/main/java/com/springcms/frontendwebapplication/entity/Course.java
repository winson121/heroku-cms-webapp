package com.springcms.frontendwebapplication.entity;

import java.util.Collection;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(scope=Course.class,
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Course {

	private int id;
	
	@NotNull(message="is required")
	@Size(min=1, message="is required")
	private String title;
	
	@NotNull(message="is required")
	@Size(min=1, message="is required")
	private String description;
	
	private User instructor;
	
	private Collection<User> users;
	
	public Course() {}
	
	public Course(String title, String description) {
		this.title = title;
		this.description = description;
	}
	
	public Course(int id, String title, String description) {
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getInstructor() {
		return instructor;
	}

	public void setInstructor(User instructor) {
		this.instructor = instructor;
	}
	
	
	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", title=" + title + ", description=" + description + "]";
	}

	@Override
	public boolean equals(Object obj) {
		// if both object reference the same object return true
		if(this == obj) return true;
		
		// check if arguments if of the type of the same class
		if(obj == null || obj.getClass() != this.getClass()) return false;
		
		// if object is the type geek, check the state of object
		Course course = (Course) obj;
		
		// comparing state of obj to 'this' obj
		return (course.title.equals(this.title) && course.id == this.id);
	}
	
	@Override
	public int hashCode() {
		return (this.id+this.title).hashCode();
	}
}
