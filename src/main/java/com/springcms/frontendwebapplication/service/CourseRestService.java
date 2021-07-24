package com.springcms.frontendwebapplication.service;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.springcms.frontendwebapplication.dto.Query;
import com.springcms.frontendwebapplication.entity.Course;

public interface CourseRestService {

	Collection<Course> getUserCourses(HttpServletRequest request);

	Collection<Course> getCourses(HttpServletRequest request);

	void saveUserCourse(Course course, HttpServletRequest request);

	Course getCourse(int courseId, HttpServletRequest request);

	void deleteCourse(int courseId, HttpServletRequest request);

	Collection<Course> getCoursesByPage(int pageId, int total, HttpServletRequest request);

	void enrollUser(int courseId, HttpServletRequest request);

	void unenrollUser(int courseId, HttpServletRequest request);

	Collection<Course> searchCoursesBySearchString(Query query, HttpServletRequest request);


}
