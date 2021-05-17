package ru.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.models.Product;

import java.util.List;

@Repository
public class ProductsDao {
    private final JdbcTemplate jdbcTemplate;

    public ProductsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> index() {
        return jdbcTemplate.query("SELECT * FROM product ORDER BY id", new BeanPropertyRowMapper<>(Product.class));
    }

    public Product findProductById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM product WHERE id=?", new Object[]{id} , BeanPropertyRowMapper.newInstance(Product.class));
    }


    public void addProduct(Product product) {
        jdbcTemplate.update("INSERT INTO product(photo_url, category, price, product_description, name, category_name, bytea)" +
                        " VALUES (?,?,?,?,?,?,?)", product.getPhoto_url(), product.getCategoryId(), product.getPrice(),
                product.getProduct_description(), product.getName(), product.getCategoryName(), product.getBytea());
    }

/*
    public void deleteCategory(int id) {
        jdbcTemplate.update("DELETE FROM categories WHERE id=?", id);
    }
*/
    public Product editProduct(int id) {
        return jdbcTemplate.query("SELECT * FROM product  WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Product.class))
                .stream().findAny().orElse(null);
    }

    public void updateProduct(int id, Product product) {
        jdbcTemplate.update("UPDATE product SET name=?,photo_url=?, category_name=?, category=?, " +
                        "product_description=?, price=?, bytea=? WHERE id=?", product.getName(), product.getPhoto_url(),
                product.getCategoryName(), product.getCategoryId(), product.getProduct_description(), product.getPrice(),
                product.getBytea(), id);
        }

    public void updateProductWithoutImg(int id, Product product) {
        jdbcTemplate.update("UPDATE product SET name=?, category_name=?, category=?, product_description=?, price=? WHERE id=?",
                product.getName(), product.getCategoryName(), product.getCategoryId(), product.getProduct_description(), product.getPrice(), id);
    }


    public void deleteProduct(int id) {
        jdbcTemplate.update("DELETE FROM product WHERE id=?", id);
    }
}