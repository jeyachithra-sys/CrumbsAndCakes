package com.crumbs.cakes;

import com.crumbs.cakes.model.Shopping;
import com.crumbs.cakes.model.Cart;
import com.crumbs.cakes.repository.CartRepository;
import com.crumbs.cakes.repository.ShoppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingController {

    @Autowired
    private ShoppingRepository shoppingRepository;

    @Autowired
    private CartRepository cartRepository;

    // Display shopping/checkout page
    @GetMapping("/shopping")
    public String showShoppingPage(Model model, HttpSession session) {
        String userName = (String) session.getAttribute("user_name");
        if (userName == null) {
            userName = "Guest";
        }

        List<Cart> cartItems = cartRepository.findAll();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user_name", userName);
        return "shopping";
    }

    // Delete a single item from cart
    @PostMapping("/cart/delete/{id}")
    public String deleteCartItem(@PathVariable int id) {
        cartRepository.deleteById(id);
        return "redirect:/shopping";
    }

    // Increase quantity of a cart item
    @PostMapping("/cart/increase/{id}")
    public String increaseQuantity(@PathVariable int id) {
        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart != null) {
            int currentQty = cart.getQuantity() != null ? cart.getQuantity() : 1;
            cart.setQuantity(currentQty + 1);

            // Recalculate total_amount for this cake
            double pricePerCake = cart.getTotal_amount() / currentQty;
            cart.setTotal_amount(pricePerCake * (currentQty + 1));

            cartRepository.save(cart);
        }
        return "redirect:/shopping";
    }

    // Decrease quantity of a cart item
    @PostMapping("/cart/decrease/{id}")
    public String decreaseQuantity(@PathVariable int id) {
        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart != null && cart.getQuantity() != null && cart.getQuantity() > 1) {
            int currentQty = cart.getQuantity();
            cart.setQuantity(currentQty - 1);

            // Recalculate total_amount for this cake
            double pricePerCake = cart.getTotal_amount() / currentQty;
            cart.setTotal_amount(pricePerCake * (currentQty - 1));

            cartRepository.save(cart);
        }
        return "redirect:/shopping";
    }

    // Submit the order (checkout)
    @PostMapping("/submitOrder")
    public String submitOrder(@ModelAttribute Shopping shopping, Model model) {
        shoppingRepository.save(shopping);

        // Clear cart after successful checkout
        cartRepository.deleteAll();

        model.addAttribute("message", "Order placed successfully!");
        return "redirect:/shop"; // or any success page
    }

    // Cancel all items in cart (single button scenario)
    @PostMapping("/cancelOrder")
    public String cancelOrder() {
        cartRepository.deleteAll();
        return "redirect:/shop"; // back to shop page
    }
}
