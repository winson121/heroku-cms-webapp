package com.springcms.frontendwebapplication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.springcms.frontendwebapplication.entity.Course;

public class Test {

	public static void main(String[] args) {
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(1,"A", "A"));
		courses.add(new Course(1,"A", "A"));
		courses.add(new Course("B", "B"));
		courses.add(new Course("C","C"));
		courses.add(new Course("D", "D"));
		courses.add(new Course("E", "E"));
		
		System.out.println(courses);
		System.out.println(courses.get(0).equals(courses.get(1)));
		Collection<Course> coursesC = new HashSet<>(courses);
		System.out.println(coursesC);
		
		Collection<Course> course = new HashSet<>();
		System.out.println(course);
		
	}

}
