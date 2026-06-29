package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.dao.ProductDao;
import com.example.entities.Product;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    // Hay que inyectar por constructor la dependencia de ProductDao
    private final ProductDao productDao;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return productDao.findAll(pageable);
    }

    @Override
    public List<Product> finAll(Sort sort) {
        // TODO Auto-generated method stub
        return productDao.findAll(sort);
    }

    @Override
    public Product findById(int id) {
        // TODO Auto-generated method stub
        return productDao.findById(id);
    }

    @Override
    public Product save(Product product) {
        // TODO Auto-generated method stub
        return productDao.save(product);
    }

    @Override
    public void delete(Product product) {
        // TODO Auto-generated method stub
        productDao.delete(product); // no return que es void!!
    }

    @Override
    public List<Product> findAll() {
        // TODO Auto-generated method stub
        return productDao.findAll();
    }

}
