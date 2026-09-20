package com.crumbs.cakes;

import com.crumbs.cakes.model.ContactMessage;
import com.crumbs.cakes.repository.ContactMessageRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContactController {

    @Autowired
    private ContactMessageRepository contactRepo;

    // ✅ Serve contact page with username if logged in
    @GetMapping("/contact")
    public String contactPage(HttpSession session, Model model) {
        // Load form object
        model.addAttribute("contactMessage", new ContactMessage());

        // Get username from session (if logged in)
        String userName = (String) session.getAttribute("user_name");

        if (userName != null) {
            model.addAttribute("user_name", userName);
        }

        return "contact"; // contact.html (Thymeleaf)
    }

    // ✅ Handle form submission from Thymeleaf form
    @PostMapping("/contact")
    public String submitMessage(@ModelAttribute ContactMessage contactMessage,
                                HttpSession session, Model model) {
        contactRepo.save(contactMessage);

        model.addAttribute("contactMessage", new ContactMessage());
        model.addAttribute("success", true);

        // Keep showing username if available
        String userName = (String) session.getAttribute("user_name");
        if (userName != null) {
            model.addAttribute("user_name", userName);
        }

        return "contact";
    }

    // ✅ Optional: For Postman JSON submissions
    @PostMapping(value = "/contact", consumes = "application/json")
    @ResponseBody
    public String submitJsonMessage(@RequestBody ContactMessage contactMessage) {
        contactRepo.save(contactMessage);
        return "Message saved successfully";
    }

    
}
