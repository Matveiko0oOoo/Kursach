package com.example.buysell.controllers;

import com.example.buysell.models.City;
import com.example.buysell.models.Product;
import com.example.buysell.models.User;
import com.example.buysell.services.CityService;
import com.example.buysell.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CityService cityService;

    @GetMapping("/")
    public String products(
            @RequestParam(name = "searchWord", required = false) String title,
            @RequestParam(name = "searchCity", required = false) String city,
            @RequestParam(name = "sortOrder", required = false) String sortOrder,
            Model model, Principal principal) {

        List<Product> products = productService.listProducts(title, city, sortOrder);
        model.addAttribute("products", products);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);
        model.addAttribute("searchCity", city != null ? city : "");
        model.addAttribute("sortOrder", sortOrder != null ? sortOrder : "");
        model.addAttribute("cities", cityService.getAllCities());

        return "products";
    }




    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal){
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());
        return "product-info";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3, Product product, Principal principal) throws IOException {
        if (principal == null) {
            return "redirect:/login";
        }

        productService.saveProduct(principal, product, file1, file2, file3);
        return "redirect:/my/products";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Перенаправление на страницу входа, если пользователь не авторизован
        }

        productService.deleteProduct(id);
        return "redirect:/my/products"; // Перенаправление обратно к товарам пользователя
    }

    @GetMapping("/my/products")
    public String userProducts(Principal principal, Model model) {
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        List<City> cities = cityService.getAllCities();
        model.addAttribute("cities", cities);
        return "my-products";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Перенаправление на страницу входа
        }
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("user", productService.getUserByPrincipal(principal));

        return "edit-product";
    }

    @PostMapping("/product/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("price") float price,
                                @RequestParam("city") String city,
                                @RequestParam(value = "file1", required = false) MultipartFile file1,
                                @RequestParam(value = "file2", required = false) MultipartFile file2,
                                @RequestParam(value = "file3", required = false) MultipartFile file3,
                                Principal principal) throws IOException {
        if (principal == null) {
            return "redirect:/login";
        }

        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/my/products";
        }

        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setCity(city);

        // Обновление изображений
        productService.saveProduct(principal, product, file1, file2, file3);
        return "redirect:/my/products";
    }

    @PostMapping("/product/fix-images/{id}")
    public String fixImages(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        productService.fixOrphanImages(id);
        return "redirect:/my/products";
    }


}

