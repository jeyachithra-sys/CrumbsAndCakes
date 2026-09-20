package com.crumbs.cakes;

import com.crumbs.cakes.model.Admin;
import com.crumbs.cakes.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ---------- LOGIN PAGE ----------
    @GetMapping("/admin/login")
    public String showLoginPage() {
        return "admin_login";
    }

    // ---------- LOGIN SUBMIT ----------
    @PostMapping("/admin/login")
    public String loginAdmin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session
    ) {
        Admin admin = adminRepository.findByUsername(username);

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            // ✅ Store login session
            session.setAttribute("admin_id", admin.getAdmin_id());
            session.setAttribute("admin_name", admin.getUsername());
            return "redirect:/admin/dashboard"; // redirect to dashboard
        } else {
            model.addAttribute("error", "Invalid admin username or password");
            return "admin_login";
        }
    }

    // ---------- DASHBOARD PAGE ----------
    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // ✅ Check if admin is logged in
        Object adminName = session.getAttribute("admin_name");
        if (adminName == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("adminName", adminName);
        return "admin_dashboard"; // loads templates/admin_dashboard.html
    }

    // ---------- LOGOUT ----------
    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // destroy session like PHP
        return "redirect:/admin/login";
    }
}
