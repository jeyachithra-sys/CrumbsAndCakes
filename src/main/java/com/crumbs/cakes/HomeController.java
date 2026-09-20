package com.crumbs.cakes;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/index2")
    public String userDashboard(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("user_name");

        if (userName == null) {
            return "redirect:/login";
        }

        model.addAttribute("user_name", userName);
        return "index2";
    }
}
