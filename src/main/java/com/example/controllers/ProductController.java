package com.example.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Product;
import com.example.services.ProductService;

import lombok.RequiredArgsConstructor;


/**
 * La anotacion @RestController es para que todos los metodos que van a ser
 * creados dentro de este
 * controlador y reciben peticiones a través del protocolo HTTP, mediante los
 * verbos correspondientes
 * (GET, POST, PUT, DELETE, PATCH, etc.) devuelvan o reciban datos en formato de
 * JSON (JavaScript Object Notation)
 */

@RestController

/**
 * Una API REST esta orientada al recurso, es decir, que el controlador necesita
 * que se le especifique
 * que recurso va a responder, por ejemplo en esto seria /products, y en
 * dependencia del verbo del protocolo
 * HTTP se estaria haciendo una peticion (request) contreta. Por ejemplo: Si el
 * verbo es GET, significa
 * que estamos solicitando todos los productos al recurso /products. Si el
 * verbo es POST significa que queremos
 * recibir un producto en formato JSON, en el cuerpo de la peticion (request) y
 * persistirlo (guardarlo)
 * en las tablas correspondientes
 */
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Metodo que recibe una peticion para devolver un listado de todos los productos
    @GetMapping
    public List<Product> getProducts() {

        List<Product> allProducts = productService.findAll();

        return allProducts;
    }

    /*Por aquí me faltan mazo de cosas, pero como no las ha comiteado nos las he podido recuperar*/

    /* 
    Método público que recupera un producto por el id que se recibe como una variable en la ruta,
    mediante un end point (url o uri) que tiene el formato siguiente:

    http://localhost:8080/productos/1

    Donde el 1 al final será el id del producto
    
    */

    @GetMapping("/{id}") 
    public ResponseEntity<Map>String, Object>> findProductByid(
        @PathVariable(name = "id", required = true) int product_id) {

            // Devolvemos objeto si ha ido bien
            Map<String, Object> responseAsMap = new HashMap<>();

            // Devolvemos objeto si ha ido mal
            ResponseEntity<Map<String, Object>> responseEntity = null;

        try {
            Product product = productService.findById(product_id);

            if (product != null){
                String successMessage = "El producto con id " - product_id + " ha sido encontrado.";

                responseAsMap.put("mensaje todo OK", successMessage);
                responseAsMap.put("producto encontrado", product);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);



            } else {
                String failureMessage = "No ha sido encontrado ningun producto con id: " + product_id;
                responseAsMap.put("Error: ", failureMessage);
                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.NOT_FOUND);

            }

        } catch (DataAccessException e) {
            String errorMessage = "Error grave al buscar el producto con id " 
                + product_id + " y la cusa más probable es: " 
                + e.getMostSpecificCause().getMessage();
            
            responseAsMap.put("Error grave: ", errorMessage);
            responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        
        }
            

            return null;

        }

}
