package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entities.Product;

//
public interface ProductDao extends JpaRepository<Product, Integer> {

    /**
     * Fetch type LAZY trae el producto y después
     * en una subconsulta la presentación. No es muy eficiente, 
     * es mejor que todo se haga en una sola consulta.
     * 
     * Vamos a ncesitar tres métodos personalizados, que recuperen
     * la presentación en una sola consulta, que es más rápido.
     * No es más eficiente, se consumen más recursos, se lo trae todo, 
     * se necesite o no. Lazy solo lo trae cuando hace falta, pero como hemos
     * constatado mediante más consultas.
     * 
     * Y los métodos personalizados serán los siguientes:
     * 
     * 1- Método que recupera los productos paginados, de 10 en 10 o de 20 en 20, etc.
     * (este puede estar ordenado también).
     * 2- Método que recupera productos ordenados, sin páginación. Ordenados por uno o 
     * varios campos.
     * 3- Método que dado el id del producto, lo recupere con su presentación correspondiente.
     * Los métodos anteriores se implementan mediante Hibernate o HQL/JPQL. Lenguajes basados en 
     * SQL orientados a las entidades no a las bases de datos. 
     */
    
    // Método 1 recupero productos paginados. Anotado con Query para traer todo lo relacionado
    // a las entidades que digamos, de una vez.
    @Query(value = "select p from Product p left join fetch p.presentation", 
        countQuery = "select count(p) from Product p left join p.presentation")
    public Page<Product> findAll(Pageable pageable);

    // Método 2 recupera productos ordenados sin paginación. Lo personalizamos con una consulta
    // @Query, si no ni falta nos haria pq ya está incluido.
    @Query(value = "select p from Product p left join fetch p.presentation")
    public List<Product> findAll(Sort sort);

    //Método 3 recupera desde el id de un producto, recupera el producto y su presentación.
    @Query(value = "select p from Product p left join fetch p.presentation where p.id = :id")
    public Product findById(int id);

}

