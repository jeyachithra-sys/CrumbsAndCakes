package com.crumbs.cakes;

import com.crumbs.cakes.model.User;
import com.crumbs.cakes.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    // Serve the login page
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        return "login"; // points to login.html in templates
    }

    // Handle login submission
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model
    ) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            model.addAttribute("errorMessage", "No user found with this email.");
            return "login";
        }

        // Verify hashed password
        if (!BCrypt.checkpw(password, user.getPassword())) {
            model.addAttribute("errorMessage", "Incorrect password. Please try again.");
            return "login";
        }

        // Save user details to session
        session.setAttribute("user_id", user.getId());
        session.setAttribute("user_name", user.getFullName());

        // Redirect to index2.html (secured area)
        return "redirect:/index2";
    }

    // Logout endpoint
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
