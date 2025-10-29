package app.qualidade.apicadastro.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultaPetSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> response;
    private Integer lastCreatedId;

    // ========== HELPERS ==========

    private String buildJson(String nome, String raca, Integer idade) {
        StringBuilder jsonBuilder = new StringBuilder("{");
        boolean first = true;
        if (nome != null) {
            jsonBuilder.append("\"nome\":\"").append(nome).append("\"");
            first = false;
        }
        if (raca != null) {
            if (!first) jsonBuilder.append(",");
            jsonBuilder.append("\"raca\":\"").append(raca).append("\"");
            first = false;
        }
        if (idade != null) {
            if (!first) jsonBuilder.append(",");
            jsonBuilder.append("\"idade\":").append(idade);
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    private Integer extractIdFromBody(String body) {
        if (body == null) return null;
        Pattern p = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");
        Matcher m = p.matcher(body);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return null;
    }

    private ResponseEntity<String> postPet(String nome, String raca, Integer idade) {
        String json = buildJson(nome, raca, idade);
        String url = "http://localhost:" + port + "/pets";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }

    // ========== DADO ==========

    @Dado("que eu crio um pet com nome {string} e raça {string} e idade {int}")
    public void que_eu_crio_um_pet_com_todos_campos(String nome, String raca, Integer idade) {
        ResponseEntity<String> resp = postPet(nome, raca, idade);
        lastCreatedId = extractIdFromBody(resp.getBody());
    }

    @Dado("que existem os seguintes pets:")
    public void que_existem_os_seguintes_pets(io.cucumber.datatable.DataTable table) {
        List<List<String>> rows = table.asLists();
        int startRow = 0;
        // se primeira linha for cabeçalho, pula
        if (rows.size() > 0) {
            List<String> first = rows.get(0);
            if (first.get(0).equalsIgnoreCase("nome")
                    || first.get(1).equalsIgnoreCase("raca")
                    || first.get(2).equalsIgnoreCase("idade")) {
                startRow = 1;
            }
        }
        for (int i = startRow; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            String nome = row.get(0);
            String raca = row.get(1);
            Integer idade = Integer.valueOf(row.get(2));
            postPet(nome, raca, idade);
        }
    }

    @Dado("que não existe um pet com ID {int}")
    public void que_nao_existe_um_pet_com_id(Integer id) {
        // deixa vazio — assumimos que esse ID é inexistente
    }

    // ========== QUANDO ==========

    @Quando("eu realizo a requisição de listagem de pets na consulta")
    public void eu_realizo_a_requisicao_de_listagem_de_pets_na_consulta() {
        String url = "http://localhost:" + port + "/pets";
        response = restTemplate.getForEntity(url, String.class);
    }

    @Quando("eu consulto o pet pelo ID na consulta")
    public void eu_consulto_o_pet_pelo_id_na_consulta() {
        assertNotNull(lastCreatedId, "Nenhum pet criado anteriormente para obter o ID.");
        String url = "http://localhost:" + port + "/pets/" + lastCreatedId;
        response = restTemplate.getForEntity(url, String.class);
    }

    @Quando("eu consulto o pet pelo ID {int} na consulta")
    public void eu_consulto_o_pet_pelo_id_informado_na_consulta(Integer id) {
        String url = "http://localhost:" + port + "/pets/" + id;
        response = restTemplate.getForEntity(url, String.class);
    }

    @Quando("eu consulto pets com nome {string} na consulta")
    public void eu_consulto_pets_com_nome_na_consulta(String nome) {
        String url = "http://localhost:" + port + "/pets?nome=" + nome;
        response = restTemplate.getForEntity(url, String.class);
    }

    @Quando("eu consulto pets com raça {string} na consulta")
    public void eu_consulto_pets_com_raca_na_consulta(String raca) {
        String url = "http://localhost:" + port + "/pets?raca=" + raca;
        response = restTemplate.getForEntity(url, String.class);
    }

    // ========== ENTÃO (UNIQUE) ==========

    @Entao("na consulta deve retornar status {int}")
    public void na_consulta_deve_retornar_status(Integer statusEsperado) {
        Integer statusRecebido = response != null ? response.getStatusCode().value() : null;
        assertEquals(statusEsperado, statusRecebido,
                "Status esperado: " + statusEsperado + ", recebido: " + statusRecebido);
    }

    @Entao("na consulta deve conter ao menos {int} pets")
    public void na_consulta_deve_conter_ao_menos_pets(Integer quantidadeEsperada) {
        String body = response != null ? response.getBody() : null;
        int ocorrencias = 0;
        if (body != null) {
            Pattern p = Pattern.compile("\"id\"\\s*:\\s*\\d+");
            Matcher m = p.matcher(body);
            while (m.find()) ocorrencias++;
        }
        assertTrue(ocorrencias >= quantidadeEsperada,
                "Esperava ao menos " + quantidadeEsperada + " pets, mas encontrou " + ocorrencias + ". Body: " + body);
    }

    @Entao("na consulta deve conter um ID")
    public void na_consulta_deve_conter_um_id() {
        Integer id = extractIdFromBody(response != null ? response.getBody() : null);
        assertNotNull(id, "Response não contém ID. Body: " + (response != null ? response.getBody() : "null"));
    }

    @Entao("na consulta deve conter um pet com nome {string}")
    public void na_consulta_deve_conter_um_pet_com_nome(String nomeEsperado) {
        String body = response != null ? response.getBody() : null;
        assertTrue(body != null && body.contains("\"nome\":\"" + nomeEsperado + "\""),
                "Nome '" + nomeEsperado + "' não encontrado. Body: " + body);
    }

    @Entao("na consulta todos os itens retornados devem ter raça {string}")
    public void na_consulta_todos_os_itens_retornados_devem_ter_raca(String racaEsperada) {
        String body = response != null ? response.getBody() : null;
        assertNotNull(body, "Body da resposta é nulo.");
        Pattern p = Pattern.compile("\"raca\"\\s*:\\s*\"([^\"]+)\"");
        Matcher m = p.matcher(body);
        boolean foundAtLeastOne = false;
        while (m.find()) {
            foundAtLeastOne = true;
            String r = m.group(1);
            assertEquals(racaEsperada, r, "Encontrada raça diferente: " + r + ". Body: " + body);
        }
        assertTrue(foundAtLeastOne, "Nenhuma raça encontrada na resposta. Body: " + body);
    }

    @Entao("na consulta o nome deve ser {string}")
    public void na_consulta_o_nome_deve_ser(String nomeEsperado) {
        String body = response != null ? response.getBody() : null;
        assertTrue(body != null && body.contains("\"nome\":\"" + nomeEsperado + "\""),
                "Nome '" + nomeEsperado + "' não encontrado. Body: " + body);
    }

    @Entao("na consulta a raça deve ser {string}")
    public void na_consulta_a_raca_deve_ser(String racaEsperada) {
        String body = response != null ? response.getBody() : null;
        assertTrue(body != null && body.contains("\"raca\":\"" + racaEsperada + "\""),
                "Raça '" + racaEsperada + "' não encontrada. Body: " + body);
    }

    @Entao("na consulta a idade deve ser {int}")
    public void na_consulta_a_idade_deve_ser(Integer idadeEsperada) {
        String body = response != null ? response.getBody() : null;
        assertTrue(body != null && body.contains("\"idade\":" + idadeEsperada),
                "Idade " + idadeEsperada + " não encontrada. Body: " + body);
    }
}
