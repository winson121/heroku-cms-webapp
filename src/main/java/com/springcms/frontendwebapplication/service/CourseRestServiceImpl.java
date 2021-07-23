package com.springcms.frontendwebapplication.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.springcms.frontendwebapplication.entity.Course;
import com.springcms.frontendwebapplication.handler.ClientStatusErrorHandler;
import com.springcms.frontendwebapplication.restutils.HttpHeadersUtil;

@Service
public class CourseRestServiceImpl implements CourseRestService{
	
	private RestTemplate restTemplate;
	
	private String cmsRestUrl;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	public CourseRestServiceImpl(RestTemplate restTemplate, 
								@Value("${cms.rest.url}") String url, HttpHeadersUtil httpHeadersUtil) {
		
		this.restTemplate = restTemplate;
		this.cmsRestUrl = url;
		
		restTemplate.setErrorHandler(new ClientStatusErrorHandler());
		logger.info("Loaded property: cms.rest.url= " + cmsRestUrl);
	}
	
	@Override
	public Collection<Course> getUserCourses(HttpServletRequest request) {
		// construct http entity with authentication headers of current user session
		HttpEntity<?> httpEntity = HttpHeadersUtil.constructHttpEntity(request);
		
		ResponseEntity<List<Course>> responseEntity = restTemplate.exchange(cmsRestUrl+"/courses/users", HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<List<Course>>() {});
		
		// get the course list and convert to hashset
		Collection<Course> courses;
		if (responseEntity.getBody() == null) {
			courses = new HashSet<Course>();
		} else {
			courses = new HashSet<Course>(responseEntity.getBody());
		}
		
		return courses;
	}

	@Override
	public Collection<Course> getCourses(HttpServletRequest request) {
		// construct http entity with authentication headers of current logged in user
		HttpEntity<?> httpEntity = HttpHeadersUtil.constructHttpEntity(request);
		logger.info("inside getCourses");
		ResponseEntity<List<Course>> responseEntity = restTemplate.exchange(cmsRestUrl+"/courses",
				HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<Course>>() {});
		
		// get the list
		List<Course> courses = responseEntity.getBody();
		 
		return courses; 
	}

	@Override
	public void saveUserCourse(Course course, HttpServletRequest request) {
		// construct http entity with authentication headers of current logged in user
		HttpEntity<?> httpEntity = HttpHeadersUtil.constructHttpEntity(course, request);
		
		int courseId = course.getId();
		
		logger.info("Save or update course: " + course);
		// make REST call to post or put course
		if(courseId == 0) {
			System.out.println("Save course: " + course);
			logger.info(cmsRestUrl+"/courses/users");
			ResponseEntity<Course> responseEntity = restTemplate.exchange(cmsRestUrl+"/courses/users", HttpMethod.POST, httpEntity, Course.class);
			logger.info("Saving Course: " + responseEntity.getBody());
		} else {
			//update Course
			ResponseEntity<Course> responseEntity = restTemplate.exchange(cmsRestUrl+"/courses/users", HttpMethod.PUT, httpEntity, Course.class);
			logger.info("Updating Course: " + responseEntity.getBody());
		}
	}

	@Override
	public Course getCourse(int courseId, HttpServletRequest request) {
		// construct http entity with authentication headers of current user
		HttpEntity<?> httpEntity = HttpHeadersUtil.constructHttpEntity(request);
		
		ResponseEntity<Course> responseEntity = restTemplate.exchange(cmsRestUrl+"/courses/"+courseId, HttpMethod.GET, httpEntity, Course.class);
		
		Course course = responseEntity.getBody();
	
		return course;
	}

	@Override
	public void deleteCourse(int courseId, HttpServletRequest request) {
		// construct http entity with authentication headers of current user session
		HttpEntity<?> httpEntity = HttpHeadersUtil.constructHttpEntity(request);
		
		// make REST call to delete todo with this id
		restTemplate.exchange(cmsRestUrl+"/courses/"+courseId, HttpMethod.DELETE, httpEntity, String.class);
		
	}

	@Override
	public Collection<Course> getCoursesByPage(int pageId, int total, HttpServletRequest request) {
		// construct httpheaders with authentication for logged in user
		HttpEntity<?> httpEntity = HttpHeadersUtil.constructHttpEntity(request);
		
		ResponseEntity<List<Course>> responseEntity = restTemplate.exchange(cmsRestUrl+"/courses/"+pageId+"/"+total, 
				HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<Course>>() {});
		
		// get the list of courses
		Collection<Course> courses = responseEntity.getBody();
		return courses;
	}

	@Override
	public void enrollUser(int courseId, HttpServletRequest request) {
		// construct httpheaders with authentication for logged in user
		HttpEntity<?> httpEntity = HttpHeadersUtil.constructHttpEntity(request);
		
		restTemplate.exchange(cmsRestUrl+"/courses/users/enroll/" + courseId, HttpMethod.GET, httpEntity, Course.class);
		
	}

	@Override
	public void unenrollUser(int courseId, HttpServletRequest request) {
		// construct httpheaders with authentication for logged in user
		HttpEntity<?> httpEntity = HttpHeadersUtil.constructHttpEntity(request);
		
		restTemplate.exchange(cmsRestUrl+"/courses/users/unenroll/" + courseId, HttpMethod.GET, httpEntity, Course.class);
		
	}

}
