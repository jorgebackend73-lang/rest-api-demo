package com.example;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.entities.Presentation;
import com.example.entities.Product;
import com.example.services.PresentationService;
import com.example.services.ProductService;

@Configuration
public class CreatesSamplesData {

    @Bean // anotación de método, le dice a spring que use setter para inyectar 
    // dependencia en el método run. (tu te has enterado, yo tampoco.)
    public CommandLineRunner samplesData(ProductService productService,
        PresentationService presentationService) {

            return args -> {

                // devuelve el cuerpo del metodo run
                // crearemos dos presentaciones, por unidad y por decenas, para los productos
                presentationService.save(Presentation.builder()
                    .name("unidad").description("por unidades").build());
                
                presentationService.save(Presentation.builder()
                    .name("decenas").description("por decenas").build());

                // persistimos varios productos que tengan las presentaciones anteriores

                productService.save(Product.builder()
                    .name("rezma de papel")
                    .description("Description")
                    .price(new BigDecimal(3.75))
                    .stock(10)
                    .presentation(presentationService.findById(1))
                    .build());

                productService.save(Product.builder()
                    .name("cartas")
                    .description("Description")
                    .price(new BigDecimal(1.00))
                    .stock(10)
                    .presentation(presentationService.findById(2))
                    .build());

                productService.save(Product.builder()
                    .name("guitarra de Juguete")
                    .description("Description")
                    .price(new BigDecimal(15.75))
                    .stock(3)
                    .presentation(presentationService.findById(1))
                    .build());

                productService.save(Product.builder()
                    .name("teclado laptop")
                    .description("Description")
                    .price(new BigDecimal(35.30))
                    .stock(5)
                    .presentation(presentationService.findById(1))
                    .build());

                productService.save(Product.builder()
                    .name("altavoces bluetooth")
                    .description("Description")
                    .price(new BigDecimal(20.75))
                    .stock(7)
                    .presentation(presentationService.findById(1))
                    .build());

                productService.save(Product.builder()
                    .name("lapices 2b")
                    .description("Description")
                    .price(new BigDecimal(1.50))
                    .stock(4)
                    .presentation(presentationService.findById(2))
                    .build());

                productService.save(Product.builder()
                    .name("boligrafos")
                    .description("de color azul")
                    .price(new BigDecimal(1.75))
                    .stock(10)
                    .presentation(presentationService.findById(1))
                    .build());

                productService.save(Product.builder()
                    .name("monitor 15 pulgadas")
                    .description("Description")
                    .price(new BigDecimal(40))
                    .stock(5)
                    .presentation(presentationService.findById(1))
                    .build());

                productService.save(Product.builder()
                    .name("cargador movil")
                    .description("para terminal samsung")
                    .price(new BigDecimal(13.75))
                    .stock(10)
                    .presentation(presentationService.findById(1))
                    .build());

                productService.save(Product.builder()
                    .name("mouse")
                    .description("ratón Apple")
                    .price(new BigDecimal(23.75))
                    .stock(7)
                    .presentation(presentationService.findById(1))
                    .build());

                productService.save(Product.builder()
                    .name("joystick gaming")
                    .description("USB 3.1")
                    .price(new BigDecimal(63.55))
                    .stock(2)
                    .presentation(presentationService.findById(1))
                    .build());


            };

        }
    

}
