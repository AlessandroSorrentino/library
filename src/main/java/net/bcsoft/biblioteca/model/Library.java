package net.bcsoft.biblioteca.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "libraries")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Column(columnDefinition = "integer default 0")
    private int amount;


}