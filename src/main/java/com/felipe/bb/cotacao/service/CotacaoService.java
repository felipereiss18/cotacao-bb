package com.felipe.bb.cotacao.service;

import com.felipe.bb.cotacao.entity.Cotacao;
import com.felipe.bb.cotacao.vo.BancoCentralVO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class CotacaoService {

    @Inject
    EntityManager em;

    private static final String url = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/" +
            "CotacaoDolarDia(dataCotacao=@dataCotacao)?@dataCotacao='{data}'" +
            "&$top=100" +
            "&$format=json" +
            "&$select=cotacaoCompra,cotacaoVenda,dataHoraCotacao";

    @Transactional
    public Cotacao buscarCotacao(LocalDate data){

        String dataString = data.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")).toString();

        Client bancoCentral = ClientBuilder.newClient();

        BancoCentralVO bacenVO = bancoCentral.target(url.replace("{data}", dataString))
                .request(MediaType.APPLICATION_JSON).get().readEntity(BancoCentralVO.class);

        bancoCentral.close();
        Cotacao cotacao = null;
        if(bacenVO != null && bacenVO.getValue() != null && !bacenVO.getValue().isEmpty()){

            cotacao = new Cotacao(
                    null,
                    LocalDateTime.now(),
                    bacenVO.getValue().get(0).getDataHoraCotacao().toLocalDate(),
                    bacenVO.getValue().get(0).getCotacaoCompra(),
                    bacenVO.getValue().get(0).getCotacaoVenda(),
                    bacenVO.getValue().get(0).getDataHoraCotacao()
            );
            em.persist(cotacao);
        }

        return cotacao;
    }


}
