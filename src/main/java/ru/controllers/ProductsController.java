package ru.controllers;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.dao.CategoriesDao;
import ru.models.Client;
import ru.models.Product;
import ru.service.ProductService;
import ru.utils.imageProcessing.ImageProcessing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
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
    public String index(Model model, Model imgUtil) {
        model.addAttribute("products", productService.index());
        imgUtil.addAttribute("imgUtil", new ImageProcessing());
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
                             @RequestParam("category") int categoryId,
                             @Value("${upload.path}") String uploadPath, Model model) {

        log.info("Path for product {} was added: {}", product.getName(), ImageProcessing.checkDir(uploadPath, categoryId));

        product.setCategoryName(categoriesDao.findById(categoryId));
        product.setCategoryId(categoryId);
        product.setBytea(ImageProcessing.writeImage(file, uploadPath, categoryId, product));
        productService.addProduct(product);

        model.addAttribute("products", productService.index());

        return "redirect:/products";
    }


    @GetMapping("/products/{id}/edit")
    public String editProduct(@PathVariable("id") int id, Model model) throws IOException {
        byte[] fileContent = ImageProcessing.ImgData(id, productService);

        model.addAttribute("photoData", ImageProcessing.getImgData(fileContent));
        model.addAttribute("editProduct", productService.editProduct(id));
        model.addAttribute("category", categoriesDao.index());

        return "products/edit";
    }


    @PatchMapping("/products/{id}")
    public String updateProduct(@ModelAttribute("editProduct") Product product, @PathVariable("id") int id,
                                @RequestParam("image") MultipartFile file, @RequestParam("category") int categoryId,
                                @Value("${upload.path}") String uploadPath) {

        log.info("Path for product {} exists: {}", product.getName(), ImageProcessing.checkDir(uploadPath, categoryId));

        product.setCategoryName(categoriesDao.findById(categoryId));
        product.setCategoryId(categoryId);

        if (!file.isEmpty()) {
            String pathname = productService.findProductById(id).getPhoto_url();
            ImageProcessing.deleteImgData(pathname);
            product.setBytea(ImageProcessing.writeImage(file, uploadPath, categoryId, product));
            productService.updateProduct(id, product);
        } else
            productService.updateProductWithoutImg(id, product);
        return "redirect:/products";
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        String pathname = productService.findProductById(id).getPhoto_url();
        ImageProcessing.deleteImgData(pathname);
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
