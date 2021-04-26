package ru.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.dao.ProductsDao;

@Service
public class ProductService extends ProductsDao {

    public ProductService(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }
}
