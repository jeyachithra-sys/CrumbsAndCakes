package com.crumbs.cakes;

import com.crumbs.cakes.model.Shopping;
import com.crumbs.cakes.repository.ShoppingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminOrderController {

    @Autowired
    private ShoppingRepository shoppingRepository;

    // ✅ Display all orders
    @GetMapping("/manage_orders")
    public String showOrders(HttpSession session, Model model) {
        // Check if admin is logged in
        Object adminName = session.getAttribute("admin_name");
        if (adminName == null) {
            return "redirect:/admin/login";
        }

        List<Shopping> orders = shoppingRepository.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("adminName", adminName);
        model.addAttribute("statusMessage", session.getAttribute("status_message"));
        session.removeAttribute("status_message");

        return "manage_orders"; // refers to templates/manage_orders.html
    }

    // ✅ Update order status
    @PostMapping("/admin/update-order-status")
    public String updateOrderStatus(
            @RequestParam("order_id") int orderId,
            @RequestParam("order_status") String orderStatus,
            HttpSession session
    ) {
        Shopping order = shoppingRepository.findById(orderId).orElse(null);
        if (order != null) {
            try {
                order.setOrder_status(Shopping.OrderStatus.valueOf(orderStatus));
                shoppingRepository.save(order);
                session.setAttribute("status_message", "Order status updated successfully!");
            } catch (IllegalArgumentException e) {
                session.setAttribute("status_message", "Invalid order status provided!");
            }
        } else {
            session.setAttribute("status_message", "Order not found!");
        }

        return "redirect:/manage_orders";
    }
}
