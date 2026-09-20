package com.crumbs.cakes;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestimonialController {

    @GetMapping("/testimonial")
    public String showTestimonialPage(HttpSession session, Model model) {
        // Check if user is logged in
        Object userId = session.getAttribute("user_id");

        if (userId == null) {
            // Redirect to login if not logged in
            return "redirect:/logins";
        }

        // Get user name from session
        String userName = (String) session.getAttribute("user_name");
        if (userName == null) {
            userName = "Guest";
        }

        // Add user name to model for Thymeleaf to display
        model.addAttribute("user_name", userName);

        // Render testimonial.html from templates folder
        return "testimonial";
    }
}
