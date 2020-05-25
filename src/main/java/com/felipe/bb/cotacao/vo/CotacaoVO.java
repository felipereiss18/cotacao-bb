package com.felipe.bb.cotacao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CotacaoVO {

    private Double cotacaoVenda;
    private Double cotacaoCompra;

    private LocalDateTime dataHoraCotacao;


    public void setDataHoraCotacao(String dataHoraCotacao) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            this.dataHoraCotacao = LocalDateTime.parse(dataHoraCotacao, dtf);
        }catch (DateTimeParseException e){
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
            this.dataHoraCotacao = LocalDateTime.parse(dataHoraCotacao, dtf);
        }
    }
}
