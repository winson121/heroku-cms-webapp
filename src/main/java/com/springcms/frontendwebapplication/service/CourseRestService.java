package com.springcms.frontendwebapplication.service;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.springcms.frontendwebapplication.entity.Course;

public interface CourseRestService {

	Collection<Course> getUserCourses(HttpServletRequest request);

	Collection<Course> getCourses(HttpServletRequest request);

	void saveUserCourse(Course course, HttpServletRequest request);

	Course getCourse(int courseId, HttpServletRequest request);

	void deleteCourse(int courseId, HttpServletRequest request);


}
