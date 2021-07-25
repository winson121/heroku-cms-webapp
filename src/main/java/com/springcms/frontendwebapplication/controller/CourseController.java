package com.springcms.frontendwebapplication.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springcms.frontendwebapplication.dto.Query;
import com.springcms.frontendwebapplication.entity.Course;
import com.springcms.frontendwebapplication.entity.User;
import com.springcms.frontendwebapplication.service.CourseRestService;

@Controller
@RequestMapping("/courses")
public class CourseController {
	
	@Autowired
	private CourseRestService courseService;
	
	// add support to trim empty string to null
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/userCourses")
	public String showUserCourses(Model model, HttpServletRequest request) {
		
		// get courses from service
		Collection<Course> courses = courseService.getUserCourses(request);
		
		Set<Course> userCourses = (Set<Course>) courses;
		
		// add userCourses to model for checking if user already enrolled to the course or not
		model.addAttribute("userCourses", userCourses);
		
		// add courses to the model
		model.addAttribute("courses", courses);
		
		// add query to the model
		Query query = new Query();
		query.setColumnName("title");
		model.addAttribute("query", query);
		
		return "user-course-catalog";
	}
	
	@GetMapping("/courseCatalog")
	public String showCourses(Model model, HttpServletRequest request) {
		// get courses from the course service
		
		Collection<Course> courses = courseService.getCourses(request);
		
		// get user's courses from our service
		Set<Course> userCourses = (Set<Course>) courseService.getUserCourses(request);
		
		// add userCourses to model for checking if user already enrolled to the course or not
		model.addAttribute("userCourses", userCourses);
				
		// add courses to the model
		model.addAttribute("courses", courses);

		// add query to the model
		Query query = new Query();
		query.setColumnName("title");
		model.addAttribute("query", query);

		return "course-catalog";
	}
	
	@PostMapping("/saveUserCourse")
	public String saveUserCourse(@Valid @ModelAttribute("course") Course course, BindingResult bindingResult, HttpServletRequest request) {
		
		// form validation
		if(bindingResult.hasErrors()) {
			return "course-form";
		}

		// save course using service
		
		courseService.saveUserCourse(course, request);
		
		return "redirect:/courses/userCourses";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("courseId") int courseId, 
									Model model, HttpServletRequest request) {
		
		// get course from our service
		Course course = courseService.getCourse(courseId, request);
		
		// set Course as model attribute to pre-populate the form
		model.addAttribute("course", course);
		
		// send over to our form
		return "course-form";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model model, HttpServletRequest request) {
		Course course = new Course();
		
		// get current logged in user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		course.setInstructor(user);
		model.addAttribute("course", course);
		return "course-form";
	}
	
	@GetMapping("/courseDetails")
	public String showCourseDetails(@RequestParam("courseId")int courseId, Model model, HttpServletRequest request) {
		// get course from our service
		Course course = courseService.getCourse(courseId, request);
		
		// get user's courses from our service
		Set<Course> userCourses = (Set<Course>) courseService.getUserCourses(request);
		
		// add userCourses to model for checking if user already enrolled to the course or not
		model.addAttribute("userCourses", userCourses);
		
		// set course as model attribute for our course details page
		model.addAttribute("course", course);
		
		// send over to our form
		return "course-detail";
	}
	
	@GetMapping("/delete") 
	public String deleteCourse(@RequestParam("courseId") int courseId, HttpServletRequest request) {
		
		courseService.deleteCourse(courseId, request);
		
		return "redirect:/courses/userCourses";
	}
	
	@GetMapping("/enroll")
	public String enroll(@RequestParam("courseId") int courseId, HttpServletRequest request) {
		courseService.enrollUser(courseId, request);
		
		return getPreviousPageByRequest(request).orElse("/courseCatalog");
	}
	
	@GetMapping("/unenroll")
	public String unenroll(@RequestParam("courseId") int courseId, HttpServletRequest request) {
		courseService.unenrollUser(courseId, request);
		
		return getPreviousPageByRequest(request).orElse("/courseCatalog");
	}
	
	// return the referer path if the "Referer" key is available in the request header
	// otherwise, return empty Optional
	protected Optional<String> getPreviousPageByRequest(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:"+ requestUrl);
	}
	
	@GetMapping("/search")
	public String searchCourses(@RequestParam(name="courseString", required=false) String searchString, @ModelAttribute("query") Query query ,
								Model model, HttpServletRequest request) {
		// pass the query from request parameter to the query object
		query.setQuery(searchString);

		// search course string from service
		Collection<Course> courses = courseService.searchCoursesBySearchString(query, request);
		
		// add courses to model
		model.addAttribute("courses", courses);
		
		// get user's courses from our service
		Set<Course> userCourses = (Set<Course>) courseService.getUserCourses(request);
		
		// add userCourses to model for checking if user already enrolled to the course or not
		model.addAttribute("userCourses", userCourses);

		return "course-catalog";
	}
	
	@GetMapping("/searchUserCourses")
	public String searchUserCourses(@RequestParam(name="courseString", required=false) String searchString, @ModelAttribute("query") Query query ,
										   Model model, HttpServletRequest request) {
		
		// pass the query from request parameter to the query object
		query.setQuery(searchString);
		
		// search course string from service
		Collection<Course> courses = courseService.searchCoursesBySearchString(query, request);
		
		// search users' courses from service
		Set<Course> userCourses = (Set<Course>)courseService.getUserCourses(request);
		
		// check if searched result is in userCourses, add this to the search result that we want to display
		Set<Course> userCoursesFiltered = new HashSet<>();
		for(Course result: courses) {
			if (userCourses.contains(result)) {
				userCoursesFiltered.add(result);
			}
		}
		
		// add filtered results to model
		model.addAttribute("courses", userCoursesFiltered);
		
		return "user-course-catalog";
	}
	
}
