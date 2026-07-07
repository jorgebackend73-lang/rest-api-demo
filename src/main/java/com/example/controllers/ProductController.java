package com.example.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entities.Product;
import com.example.models.FileUploadResponse;
import com.example.services.ProductService;
import com.example.utilities.FileDownloadUtil;
import com.example.utilities.FileUploadUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

    private final FileUploadUtil fileUploadUtil;
    private final FileDownloadUtil fileDownloadUtil;

     /**
     * 
     * IMPORTANTE!!!
     * 
     * Una API REST tiene que devolver informacion respecto a como ha sido
     * solucionada la peticion (request),
     * por ejemplo: el codigo 200 significa estado OK de la peticion, el codido 201
     * significaria CREATED,
     * el codigo 500 significaria que el servidor no ha podido cumplimentar la
     * peticion, el codigo 401 NO ENCONTRADO,
     * el codigo 403 prohibido, etc. Todos estos codigos se pueden encontrar en el
     * sitio de W3Schools
     * 
     * https://www.w3schools.com/tags/ref_httpmessages.asp
     * 
     */

    /**
     * El metodo siguiente va a responder a una peticion (request) del tipo:
     * 
     * http://localhost:8080/products?page=0&size=3
     * 
     * Donde los parametros page y size seran utilizados para la paginacion, y no
     * seran requeridos, es decir,
     * que no son obligatorios que se suministren. Y en caso de NO ser suministrados
     * (page y size),
     * los productos se van a devolver ordenados.
     * 
     */

    // Metodo que recibe una peticion para devolver un listado de todos los productos
    @GetMapping
    public ResponseEntity<Map<String, Object>> dameProductos(
        @RequestParam(name = "page", required = false) Integer page,
        @RequestParam(name = "size", required = false) Integer size ) {

        List<Product> products = null;
        Map<String, Object> responseAsMap = new HashMap<>();
        Sort sort = Sort.by("name");

        // Comprobar si en la peticion (request) me han suministrado los parametros page y size
        if (page != null && size != null) {

            Pageable pageable = PageRequest.of(page, size, sort);

            // Implica devolver los productos paginados, es decir, una pagina de Product
            Page<Product> productPage = productService.findAll(pageable);
            products = productPage.getContent();
            responseAsMap.put("productos", products);

        } else {

            // Devolver los productos ordenados, por nombre (name), por ejemplo
            products = productService.findAll(sort);
            responseAsMap.put("productos", products);
        }
        
        return new ResponseEntity<>(responseAsMap, HttpStatus.OK);
    }

    /* 
    Método público que recupera un producto por el id que se recibe como una variable en la ruta,
    mediante un end point (url o uri) que tiene el formato siguiente:

    http://localhost:8080/productos/1

    Donde el 1 al final será el id del producto
    
    */

    @GetMapping("/{id}") 
    public ResponseEntity<Map<String, Object>> findProductById(
        @PathVariable(name = "id", required = true) int product_id) {

            // Devolvemos objeto si ha ido bien
            Map<String, Object> responseAsMap = new HashMap<>();

            // Devolvemos objeto si ha ido mal
            ResponseEntity<Map<String, Object>> responseEntity = null;

        try {
            Product product = productService.findById(product_id);

            if (product != null){
                String successMessage = "El producto con id " + product_id + " ha sido encontrado.";

                responseAsMap.put("mensaje todo OK", successMessage);
                responseAsMap.put("producto encontrado: ", product);
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
            

            return responseEntity;

        }

        /*
        Método que recibe por post el producto para ser persistido(guardado) y que valida el JSON recibido
        para controlar si esta bien formado o no.

        Primero hay que cambiar lo que recibe el metodo saveProduct, pq ya el producto no viene ocupando todo el 
        cuerpo de la petición (request), si no una parte y la otra parte la ocupa la imagen del producto.

        Muy importante, q no se nos olvide anotar este mdtodo y todos los que insertan, crean, eliminan regisstros
        en las tablas con la anotación @Transactional, también hay que especificar el tipo se archivo que va 
        consumir este metodo.

        */

        @PostMapping(consumes = "multipart/form-data") // no recibe nada pq ya lo trae el mapa de productos 
        @Transactional
        // cambiamos RequestBody por RequestPart para traer por partes. 
        // En RequestPart(name = "file -> o como sea que le hayamos llamdo en postman") 
        public ResponseEntity<Map<String, Object>> saveProduct(@Valid @RequestPart Product product, 
            BindingResult result, 
            @RequestPart(name = "file", required = false) MultipartFile imagenDelProducto) throws IOException {
            // este método debe recibir a través de requestBody el producto a guardar en el cuerpo de la DB
            // con @valid de jakarta validation lo validamos
            // con BindinResult guardamos esa validación. Etoy comentando lo de arriba ^^


            List<String> mensajesDeError = new ArrayList<>(); // aquí guardaremos los errores recibidos desde el if de más abajo.

            // mandamos de vuelta to lo malo que hemos encontrado
            Map<String, Object> responseAsMap = new HashMap<>(); // sin orden ninguno por ser HashMap
            ResponseEntity<Map<String, Object>> responseEntity = null;

            //Lo de abajo, comprobar si hay errores en el producto recibido
            if (result.hasErrors()) {
                // Recuperamos los errores del producto recibido y se lo informamos al
                // que realizo la petición (request) de persistir el producto
                List<ObjectError> objectErrors = result.getAllErrors(); // listado de errores recibidos, que es lo que se manda

                objectErrors.stream().forEach(objectError -> mensajesDeError.add(objectError.getDefaultMessage()));

                // la respuesta para el responseAsMap
                responseAsMap.put("El producto tiene los siguientes errores: ", mensajesDeError);
                responseAsMap.put("Producto mal formado: ", product);

                responseEntity = new ResponseEntity<>(responseAsMap, HttpStatus.BAD_REQUEST);

                return responseEntity;
            }

            // persistimos el producto, pq si hemos llegado hasta aquí es que esta bien
            // pero por si las moscas try - catch.
            // Y antes tb comprobar que hemos recibido imagen del producto para guardarla en el sistema de archivos.

            if (imagenDelProducto != null && !imagenDelProducto.isEmpty()) {

                /*Para guardar la imagen del producto, primero agregarle como prefijo un código alfanumérico generado 
                aleatoriamente a partir de un metodo que se encuentra en la biblio apache commons text, que hay que descargar 
                la Depend del Repo de Maven y ponerla en el pon.xml*/

                /*@Service beans de servicio / @Repository quiero repo / @Controller quiero ...
                Vamos a crear un componente en un paquete: com.example.utilities, con un método para guardar la imagen recibida 
                en una carpeta del file sistem y devolver un código alfanumerico generado aleatoriamente que lleve como prefijo 
                el nombre del fichero de imagen recibido. La carpeta será la que deseemos y se hara uso intensivo de NIO.2 y se 
                comprobará si la carpeta existe o no para crearla si es el caso. */

                String fileCode = fileUploadUtil
                    .saveFile(imagenDelProducto.getOriginalFilename(), imagenDelProducto);

                product.setProductImage(fileCode + imagenDelProducto.getOriginalFilename());
                // con esto el producto tendría la imagen subida

                // como es una api rest hay que devolver información al que realizó la request
                // respecto de la imagen subida. 
                // para ello en un paquete computo.example.models creamos un record donde devolveremos 
                // la respuesta con la info de la imagen subida.

                FileUploadResponse fileUploadResponse = new FileUploadResponse(
                    fileCode + '-' + imagenDelProducto.getOriginalFilename(), "/products/fileDownload",
                    imagenDelProducto.getSize()
                );

                responseAsMap.put("información de la imagen del producto", fileUploadResponse);

            }

            try {
                Product productoPersistido = productService.save(product);
                responseAsMap.put("mensaje: ", "¡Producto persistido exitosamente!");
                responseAsMap.put("producto persistido: ", productoPersistido);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap,HttpStatus.CREATED);
                
            } catch (DataAccessException e) {
                responseAsMap.put("Error grave", "No ha podido ser guardado el producto y la causa más probable es: " 
                    + e.getMostSpecificCause().getMessage());
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
            
            }

            return responseEntity;
        }

        /* Método que recupera imagen a partir del codigo que tiene como prefijo y que generamos
        aleatoriamente 
        Hay que meter el GetMapping con el fileDowload o como fuera que lo llamamos en el post más el codigo 
        ramdom asignado*/
        @GetMapping("/fileDownload/{fileCode}")
        public ResponseEntity<?> downLoadFile(@PathVariable String fileCode) {

            Resource resource = null;

            try {
                resource = fileDownloadUtil.getFileAsResource(fileCode);
            } catch (IOException ioe) {
               return ResponseEntity
                .internalServerError()
                .build(); // construimos el objeto sin llamar a new
            }

            if (resource == null) 
                return new ResponseEntity<>("Imagen del producto no encontrada ",
                    HttpStatus.NOT_FOUND);

            /*En este punto hemos encontrado el fichero imagen del producto y podemos
            enviarlo como respuesta a la petición. Irá como fichero adjunto
            en el cuerpo de la respuesta */

            String contentType = "application/octet-stream";
            String headerValue = "attachment; fileName=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);            
        }

}
