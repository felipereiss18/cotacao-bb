package com.felipe.bb.cotacao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="cotacaoSeq")
    private Long id;

    private LocalDateTime requisicao;

    private LocalDate data;

    private Double valorCompra;

    private Double valorVenda;

    private LocalDateTime dataHora;
}
