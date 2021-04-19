package ru.controllers;

import org.imgscalr.Scalr;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.models.Category;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import static org.imgscalr.Scalr.resize;

@Controller
public class ProductsController {

    @GetMapping("/products")
    public  String index() {
        return "products";
    }

    public static void resizeImageTo600x600(BufferedImage image) throws IOException {
        ImageIO.write(resize(image, Scalr.Method.ULTRA_QUALITY, 300, Scalr.OP_ANTIALIAS), "JPG", new File("/path/to/file.jpg"));
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public String createImage(@RequestParam("image") MultipartFile image) {

        try {
            byte[] design = image.getBytes();

            UUID uuid = UUID.randomUUID();
            String UUID = uuid.toString();


            if (design.length == 0) {
                System.out.println("design is empty");
            }

            Files.write(Paths.get("/Users/over/IdeaProjects/TelegramBot/Архив/src/main/resources/drinks/" + UUID + ".png"), image.getBytes());
            System.out.println(Arrays.toString(design));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/products";
    }
}
