package com.felipe.bb.cotacao.resource;

import com.felipe.bb.cotacao.entity.Cotacao;
import com.felipe.bb.cotacao.service.CotacaoService;
import com.felipe.bb.cotacao.vo.CotacaoVO;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Path("/cotacao")
@Produces(MediaType.APPLICATION_JSON)
public class CotacaoResource {

    @Inject
    CotacaoService service;

    @GET
    @Path("{data}")
    @Operation(summary = "Buscar cotação do dólar por uma data determinada.",
            description = "Buscar cotação do dólar comercial (compra e venda) por uma data determinada na API do BACEN.")
    @APIResponse(responseCode = "200", name = "Cotação",
                content = @Content(mediaType = MediaType.APPLICATION_JSON,
                        schema = @Schema(type = SchemaType.OBJECT, implementation = CotacaoVO.class)))
    @APIResponse(responseCode = "412", description = "Formato de entrada é inválido.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "400", description = "Cenário de execução previsto.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @Counted(name = "Acessos cotacao por data")
    @Timed(name = "Duracao cotacao por data")
    public Response cotacaoPorData(@Parameter(in = ParameterIn.PATH, name = "Data", required = true,
                                                example = "24-05-2020", description = "Formato válido: dd-MM-yyyy")
                                        @PathParam("data") String data) {

        if(!data.matches("^([123]0|[012][1-9]|31)-(0[1-9]|1[012])-(19[0-9]{2}|2[0-9]{3})$")){
            return Response.status(Response.Status.PRECONDITION_FAILED).entity("Data inválida.").build();
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate ld = LocalDate.parse(data, dtf);

        Cotacao cotacao = service.buscarCotacao(ld);

        if(cotacao == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Cotação não encontrada.").build();
        }

        return Response.ok(new CotacaoVO(
                cotacao.getValorVenda(),
                cotacao.getValorCompra(),
                cotacao.getDataHora()
                )
            ).build();
    }
}