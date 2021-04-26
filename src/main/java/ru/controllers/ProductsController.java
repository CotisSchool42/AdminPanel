package ru.controllers;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.dao.CategoriesDao;
import ru.models.Product;
import ru.service.ProductService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Data
@Controller
public class ProductsController {
    private final ProductService productService;
    private final CategoriesDao categoriesDao;
    private Product product;


    public ProductsController(ProductService productService, CategoriesDao categoriesDao) {
        this.productService = productService;
        this.categoriesDao = categoriesDao;
    }


    @GetMapping("/products")
    public  String index(Model model) {
        model.addAttribute("products", productService.index());
        return "products/index";
    }

    @GetMapping("/products/add")
    public String newProduct(Model product, Model category) {
        product.addAttribute("product", new Product());
        category.addAttribute("category", categoriesDao.index());
        return "products/add";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam("image") MultipartFile file,
                             @RequestParam("category") String categoryName, @Value("${upload.path}")  String uploadPath,
                             BindingResult errors, Model model){

        int categoryId = categoriesDao.findByName(categoryName);
        // Проверочки
        Path categoryPath = Paths.get(uploadPath + categoryId);
        if (!Files.exists(categoryPath)) {
            try {
                Files.createDirectories(categoryPath);
            } catch(Exception e ){
                e.printStackTrace();
            }
        }

        try {
            byte[] design = file.getBytes();

            UUID uuid = UUID.randomUUID();
            String UUID = uuid.toString();

            if (design.length == 0) {
                System.out.println("design is empty");
            }
            System.out.println(uploadPath + categoryId + UUID + ".png");
            String photoUrl = categoryId + "/" + UUID + ".png";
            Files.write(Paths.get( (uploadPath + categoryId + "/") + UUID + ".png"), file.getBytes());
            product.setPhoto_url("images/productImages/" + photoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(product.getPhoto_url());
        product.setCategoryName(categoryName);
        product.setCategoryId(categoryId);
        productService.addProduct(product);
        model.addAttribute("products", productService.index());

        return "redirect:/products";
    }


/*    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String createImage(@RequestParam("image") MultipartFile image) {

        try {
            byte[] design = image.getBytes();

            UUID uuid = UUID.randomUUID();
            String UUID = uuid.toString();


            if (design.length == 0) {
                System.out.println("design is empty");
            }

            Files.write(Paths.get("/Users/over/SpringMvcAdminPanel/src/main/resources/drinks/" + UUID + ".png"), image.getBytes());
            System.out.println(Arrays.toString(design));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/products";
    }*/
}
