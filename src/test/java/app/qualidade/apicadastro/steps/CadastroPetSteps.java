package app.qualidade.apicadastro.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

public class CadastroPetSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;
    private String nome;
    private String raca;
    private Integer idade;

    // ========== DADO ==========

    @Dado("que eu crio um pet com nome {string}")
    public void que_eu_crio_um_pet_com_nome(String nome) {
        this.nome = nome;
    }

    @Dado("que eu crio um pet sem nome")
    public void que_eu_crio_um_pet_sem_nome() {
        this.nome = null;
    }

    @Dado("com raça {string}")
    public void com_raca(String raca) {
        this.raca = raca;
    }

    @Dado("sem raça")
    public void sem_raca() {
        this.raca = null;
    }

    @Dado("com idade {int}")
    public void com_idade(Integer idade) {
        this.idade = idade;
    }

    @Dado("sem idade")
    public void sem_idade() {
        this.idade = null;
    }

    // ========== QUANDO ==========

    @Quando("eu cadastro o pet")
    public void eu_cadastro_o_pet() {
        try {
            // Constrói o JSON baseado nos campos
            StringBuilder jsonBuilder = new StringBuilder("{");

            if (nome != null) {
                jsonBuilder.append("\"nome\":\"").append(nome).append("\"");
            }

            if (raca != null) {
                if (jsonBuilder.length() > 1) jsonBuilder.append(",");
                jsonBuilder.append("\"raca\":\"").append(raca).append("\"");
            }

            if (idade != null) {
                if (jsonBuilder.length() > 1) jsonBuilder.append(",");
                jsonBuilder.append("\"idade\":").append(idade);
            }

            jsonBuilder.append("}");

            String json = jsonBuilder.toString();
            String url = "http://localhost:" + port + "/pets";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(json, headers);

            System.out.println("📤 Enviando POST para: " + url);
            System.out.println("📦 Body: " + json);

            response = restTemplate.postForEntity(url, request, String.class);

            System.out.println("📥 Response Status: " + response.getStatusCode());
            System.out.println("📥 Response Body: " + response.getBody());

        } catch (Exception e) {
            System.out.println("❌ Erro na requisição: " + e.getMessage());
            throw e;
        }
    }

    // ========== ENTÃO ==========

    @Entao("deve retornar status {int}")
    public void deve_retornar_status(Integer statusEsperado) {
        Integer statusRecebido = response.getStatusCode().value();
        assertEquals(statusEsperado, statusRecebido,
                "Status esperado: " + statusEsperado + ", recebido: " + statusRecebido);
    }

    @Entao("deve conter um ID")
    public void deve_conter_um_id() {
        String body = response.getBody();
        assertTrue(body.contains("\"id\""),
                "Response não contém ID. Body: " + body);
    }

    @Entao("o nome deve ser {string}")
    public void o_nome_deve_ser(String nomeEsperado) {
        String body = response.getBody();
        assertTrue(body.contains("\"nome\":\"" + nomeEsperado + "\""),
                "Nome '" + nomeEsperado + "' não encontrado. Body: " + body);
    }

    @Entao("a raça deve ser {string}")
    public void a_raca_deve_ser(String racaEsperada) {
        String body = response.getBody();
        assertTrue(body.contains("\"raca\":\"" + racaEsperada + "\""),
                "Raça '" + racaEsperada + "' não encontrada. Body: " + body);
    }

    @Entao("a idade deve ser {int}")
    public void a_idade_deve_ser(Integer idadeEsperada) {
        String body = response.getBody();
        assertTrue(body.contains("\"idade\":" + idadeEsperada),
                "Idade " + idadeEsperada + " não encontrada. Body: " + body);
    }
}