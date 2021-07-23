package com.springcms.frontendwebapplication.controller;

import java.util.Collection;
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
		
		Set<Course> userCourses = (Set<Course>) courseService.getUserCourses(request);
		
		// add userCourses to model for checking if user already enrolled to the course or not
		model.addAttribute("userCourses", userCourses);
		
		// add courses to the model
		model.addAttribute("courses", courses);
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
		
		return getPreviousPageByRequest(request).orElse("/home");
	}
	
	@GetMapping("/unenroll")
	public String unenroll(@RequestParam("courseId") int courseId, HttpServletRequest request) {
		courseService.unenrollUser(courseId, request);
		
		return getPreviousPageByRequest(request).orElse("/home");
	}
	
	// return the referer path if the "Referer" key is available in the request header
	// otherwise, return empty Optional
	protected Optional<String> getPreviousPageByRequest(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:"+ requestUrl);
	}
}
