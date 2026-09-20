package com.crumbs.cakes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.crumbs.cakes.repository.*;
import com.crumbs.cakes.model.*;
import java.util.*;

@Controller
public class ShopController {

    @Autowired
    private BirthdayCakeRepository birthdayRepo;
    @Autowired
    private WeddingCakeRepository weddingRepo;
    @Autowired
    private CustomCakeRepository customRepo;
    @Autowired
    private CartRepository cartRepo;

    @GetMapping("/shop")
    public String showShopPage(Model model) {
        model.addAttribute("birthdayCakes", birthdayRepo.findAll());
        model.addAttribute("weddingCakes", weddingRepo.findAll());
        model.addAttribute("customCakes", customRepo.findAll());
        return "shop";
    }

    @PostMapping("/submitCart")
    @ResponseBody
    public String submitCart(@RequestBody Map<String, Object> cartData) {
        List<Map<String, Object>> items = (List<Map<String, Object>>) cartData.get("items");

        for (Map<String, Object> item : items) {
            String cakeName = item.get("name").toString();
            Double price = Double.valueOf(item.get("price").toString()); // price per cake
            Integer quantity = Integer.valueOf(item.get("quantity").toString());

            Cart cart = new Cart();
            cart.setCake_names(cakeName);
            cart.setQuantity(quantity);
            cart.setTotal_amount(price * quantity);  // total for this cake item
            cartRepo.save(cart);
        }

        return "Cart submitted successfully";
    }
}
