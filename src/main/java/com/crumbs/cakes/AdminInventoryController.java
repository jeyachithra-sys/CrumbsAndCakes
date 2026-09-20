package com.crumbs.cakes;

import com.crumbs.cakes.model.BirthdayCake;
import com.crumbs.cakes.model.WeddingCake;
import com.crumbs.cakes.model.CustomCake;
import com.crumbs.cakes.repository.BirthdayCakeRepository;
import com.crumbs.cakes.repository.WeddingCakeRepository;
import com.crumbs.cakes.repository.CustomCakeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminInventoryController {

    @Autowired
    private BirthdayCakeRepository birthdayCakeRepository;

    @Autowired
    private WeddingCakeRepository weddingCakeRepository;

    @Autowired
    private CustomCakeRepository customCakeRepository;

    // ✅ Display manage inventory page
    @GetMapping("/manage_inventory")
    public String showInventory(Model model, HttpSession session,
                                @RequestParam(value = "message", required = false) String message) {

        // Check admin login session
        Object adminName = session.getAttribute("admin_name");
        if (adminName == null) {
            return "redirect:/admin/login";
        }

        List<BirthdayCake> birthdayCakes = birthdayCakeRepository.findAll();
        List<WeddingCake> weddingCakes = weddingCakeRepository.findAll();
        List<CustomCake> customCakes = customCakeRepository.findAll();

        model.addAttribute("adminName", adminName);
        model.addAttribute("message", message);
        model.addAttribute("birthdayCakes", birthdayCakes);
        model.addAttribute("weddingCakes", weddingCakes);
        model.addAttribute("customCakes", customCakes);

        return "manage_inventory"; // templates/manage_inventory.html
    }

    // ✅ Add new product
    @PostMapping("/admin/inventory/add")
    public String addProduct(@RequestParam("cake_type") String cakeType,
                             @RequestParam("name") String name,
                             @RequestParam("price") double price,
                             @RequestParam("description") String description,
                             @RequestParam("image_src") MultipartFile imageFile,
                             Model model) throws IOException {

        if (imageFile.isEmpty()) {
            model.addAttribute("message", "Image file is required.");
            return "redirect:/manage_inventory";
        }

        String uploadDir = new File(System.getProperty("user.dir"), "uploads/img").getAbsolutePath();
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String filePath = new File(dir, imageFile.getOriginalFilename()).getAbsolutePath();
        imageFile.transferTo(new File(filePath));

        String dbPath = "/uploads/img/" + imageFile.getOriginalFilename();

        switch (cakeType) {
            case "birthday" -> {
                BirthdayCake cake = new BirthdayCake();
                cake.setName(name);
                cake.setPrice(price);
                cake.setDescription(description);
                cake.setImage_src(dbPath);
                birthdayCakeRepository.save(cake);
            }
            case "wedding" -> {
                WeddingCake cake = new WeddingCake();
                cake.setName(name);
                cake.setPrice(price);
                cake.setDescription(description);
                cake.setImage_src(dbPath);
                weddingCakeRepository.save(cake);
            }
            case "custom" -> {
                CustomCake cake = new CustomCake();
                cake.setName(name);
                cake.setPrice(price);
                cake.setDescription(description);
                cake.setImage_src(dbPath);
                customCakeRepository.save(cake);
            }
            default -> {
                model.addAttribute("message", "Invalid cake type selected.");
                return "redirect:/manage_inventory";
            }
        }

        return "redirect:/manage_inventory?message=Product added successfully!";
    }

    // ✅ Delete product (AJAX)
    @PostMapping("/admin/inventory/delete")
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@RequestParam("id") int id,
                                           @RequestParam("type") String type) {
        try {
            switch (type) {
                case "birthday" -> birthdayCakeRepository.deleteById(id);
                case "wedding" -> weddingCakeRepository.deleteById(id);
                case "custom" -> customCakeRepository.deleteById(id);
                default -> {
                    return ResponseEntity.badRequest()
                            .body("{\"success\":false,\"message\":\"Invalid cake type.\"}");
                }
            }
            return ResponseEntity.ok("{\"success\":true,\"message\":\"Product deleted successfully.\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"success\":false,\"message\":\"Error deleting product.\"}");
        }
    }

    // ✅ Fetch product for edit (open edit form)
    @GetMapping("/admin/inventory/edit/{type}/{id}")
    public String editProductPage(@PathVariable("type") String type,
                                  @PathVariable("id") int id,
                                  Model model,
                                  HttpSession session) {
        Object adminName = session.getAttribute("admin_name");
        if (adminName == null) {
            return "redirect:/admin/login";
        }

        switch (type) {
            case "birthday" -> {
                Optional<BirthdayCake> cake = birthdayCakeRepository.findById(id);
                cake.ifPresent(c -> model.addAttribute("cake", c));
            }
            case "wedding" -> {
                Optional<WeddingCake> cake = weddingCakeRepository.findById(id);
                cake.ifPresent(c -> model.addAttribute("cake", c));
            }
            case "custom" -> {
                Optional<CustomCake> cake = customCakeRepository.findById(id);
                cake.ifPresent(c -> model.addAttribute("cake", c));
            }
            default -> {
                return "redirect:/manage_inventory?message=Invalid cake type";
            }
        }

        model.addAttribute("type", type);
        model.addAttribute("adminName", adminName);
        return "edit_inventory"; // new HTML page templates/edit_inventory.html
    }

    // ✅ Handle update after edit form submission
    @PostMapping("/admin/inventory/update")
    public String updateProduct(@RequestParam("id") int id,
                                @RequestParam("type") String type,
                                @RequestParam("name") String name,
                                @RequestParam("price") double price,
                                @RequestParam("description") String description,
                                @RequestParam(value = "image_src", required = false) MultipartFile imageFile)
            throws IOException {

        String path = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            String uploadDir = new File(System.getProperty("user.dir"), "uploads/img").getAbsolutePath();
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String filePath = new File(dir, imageFile.getOriginalFilename()).getAbsolutePath();
            imageFile.transferTo(new File(filePath));

            path = "/uploads/img/" + imageFile.getOriginalFilename();
        }

        // ✅ make path final for lambda usage
        final String dbPath = path;

        switch (type) {
            case "birthday" -> birthdayCakeRepository.findById(id).ifPresent(cake -> {
                cake.setName(name);
                cake.setPrice(price);
                cake.setDescription(description);
                if (dbPath != null) cake.setImage_src(dbPath);
                birthdayCakeRepository.save(cake);
            });
            case "wedding" -> weddingCakeRepository.findById(id).ifPresent(cake -> {
                cake.setName(name);
                cake.setPrice(price);
                cake.setDescription(description);
                if (dbPath != null) cake.setImage_src(dbPath);
                weddingCakeRepository.save(cake);
            });
            case "custom" -> customCakeRepository.findById(id).ifPresent(cake -> {
                cake.setName(name);
                cake.setPrice(price);
                cake.setDescription(description);
                if (dbPath != null) cake.setImage_src(dbPath);
                customCakeRepository.save(cake);
            });
        }

        return "redirect:/manage_inventory?message=Product updated successfully!";
    }
    @GetMapping("/admin/inventory/get")
@ResponseBody
public ResponseEntity<?> getProduct(@RequestParam("id") int id,
                                    @RequestParam("type") String type) {
    switch (type) {
        case "birthday":
            return birthdayCakeRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        case "wedding":
            return weddingCakeRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        case "custom":
            return customCakeRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        default:
            return ResponseEntity.badRequest().body("Invalid cake type");
    }
}


}
