package ru.utils.imageProcessing;

import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;
import ru.models.Product;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;


public class ImageProcessing {

    public static boolean checkDir(String uploadPath, int categoryId) {
        Path categoryPath = Paths.get(uploadPath + categoryId);

        if (!Files.exists(categoryPath)) {
            try {
                Files.createDirectories(categoryPath);
            } catch (IOException e) {
                e.printStackTrace();
                return false; }}
        return true;
    }

    public static byte[] resizeImage(byte[] image) {
        try {
            InputStream is = new ByteArrayInputStream(image);
            BufferedImage originalImage = ImageIO.read(is);

            originalImage = Scalr.resize(originalImage, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, 200, 200);
            //To save with original ratio uncomment next line and comment the above.
            //originalImage= Scalr.resize(originalImage, 153, 128);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] writeImage(MultipartFile file, String uploadPath, int categoryId, Product product) {
        try {
            byte[] design = file.getBytes();

            UUID uuid = UUID.randomUUID();
            String UUID = uuid.toString();

            if (design.length == 0) {
                System.out.println("design is empty");
            }

            String photoUrl = categoryId + "/" + UUID + ".png";
            Files.write(Paths.get((uploadPath + categoryId + "/") + UUID + ".png"), file.getBytes());
            product.setPhoto_url(uploadPath + photoUrl);

            return resizeImage(design);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public  static String getImgData(byte[] byteData) {
        return Base64.getMimeEncoder().encodeToString(byteData);
    }

}
