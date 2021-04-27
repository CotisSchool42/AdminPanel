package ru.controllers;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
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
    public  String index(Model model, Model imgUtil) {
        model.addAttribute("products", productService.index());
        imgUtil.addAttribute("imgUtil", new ImageUtil());
        return "products/index";
    }

    @GetMapping("/products/add")
    public String newProduct(Model product, Model category) {
        product.addAttribute("product", new Product());
        category.addAttribute("category", categoriesDao.index());
        return "products/add";
    }

    public static class ImageUtil { public String getImgData(byte[] byteData) { return Base64.getMimeEncoder().encodeToString(byteData); } }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam("image") MultipartFile file,
                             @RequestParam("category") String categoryName, @Value("${upload.path}")  String uploadPath,
                             BindingResult errors, Model model, Model mo) throws IOException {

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

            String photoUrl = categoryId + "/" + UUID + ".png";
            Files.write(Paths.get( (uploadPath + categoryId + "/") + UUID + ".png"), file.getBytes());
            product.setPhoto_url(uploadPath + photoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(product.getPhoto_url());
        product.setCategoryName(categoryName);
        product.setCategoryId(categoryId);
        byte[] imageBytes = IOUtils.toByteArray(new URL("file://" + product.getPhoto_url()));
        product.setBytea(imageBytes);
        productService.addProduct(product);
        ImageUtil k = new ImageUtil();

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + k.getImgData(product.getBytea()));


 //      String base64 = Base64.getEncoder().encodeToString(imageBytes);

   //     System.out.println(base64);

        mo.addAttribute("imgUtil", new ImageUtil());
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
