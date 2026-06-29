package com.example.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Product implements Serializable {
// Serializar es para convertir de objeto a flujo y sobre todo viceversa.
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotNull(message = "El producto tiene que tener un nombre.")
    @NotEmpty(message = "El nombre del producto no puede estar vacio.")
    @Size(min = 4, max = 30, message = "El nombre del producto no puede tener menos de 4 caracteres ni más de 25.")
    private String name;

    @NotNull(message = "La descripción del producto es requerida.")
    @NotBlank(message = "La descripción del producto no puede estar en blanco.")
    @Size(max = 45, message = "La descripcion no puede superar los 45 caracteres.")
    private String description;

    @Min(value = 0, message = "El stock no puede ser negativo.")
    private int stock;

    @Min(value = 0, message = "El precio del producto no puede ser negativo.")
    private BigDecimal price;
    

    // @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Presentation presentation;

}
