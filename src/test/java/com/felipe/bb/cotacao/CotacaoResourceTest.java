package com.felipe.bb.cotacao;

import com.felipe.bb.cotacao.vo.CotacaoVO;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CotacaoResourceTest {

    @Test
    public void testDataInvalida() {
        given()
          .when().get("/cotacao/34-05-2020")
          .then()
             .statusCode(412)
             .body(is("Data inválida."));
    }

    @Test
    public void testCotacaoNaoEncontrada(){
        given()
                .when().get("/cotacao/23-05-2020")
                .then()
                .statusCode(400)
                .body(is("Cotação não encontrada."));
    }

    @Test
    public void testCotacaoExistente(){
        given()
                .when().get("/cotacao/22-05-2020")
                .then()
                .statusCode(200)
                .body(is("{\"cotacaoCompra\":5.5802," +
                        "\"cotacaoVenda\":5.5808," +
                        "\"dataHoraCotacao\":\"2020-05-22T13:08:59.533\"}"));
    }
}