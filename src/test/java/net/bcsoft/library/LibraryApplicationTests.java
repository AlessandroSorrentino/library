package net.bcsoft.library;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



// @SpringBootTest viene utilizzata per caricare l'intera applicazione Spring Boot in un contesto di test.
// Ciò significa che vengono caricati tutti i bean, le configurazioni e le proprietà dell'applicazione,
// come se si stesse eseguendo l'applicazione stessa.
// Questa annotazione è utile per testare il comportamento dell'applicazione a un livello più elevato,
// ad esempio per testare la gestione delle richieste HTTP o l'integrazione tra diversi componenti dell'applicazione.
//
//@AutoConfigureMockMvc viene utilizzata per configurare un oggetto MockMvc per i test di integrazione basati su Spring MVC.
// MockMvc è un'implementazione di Spring MVC che consente di simulare le richieste HTTP e di testare i controller dell'applicazione
// senza dover eseguire l'applicazione stessa in un server.
// Questa annotazione è quindi utile per testare il comportamento dei controller dell'applicazione,
// in modo da poter simulare le richieste HTTP e verificare che i controller restituiscano le risposte corrette.
@SpringBootTest
class LibraryApplicationTests {


    @Test
     void contextLoads() {
    }
}

    /*
    @Autowired
    private MockMvc mockMvc; // MockMvc è un'utility che consente di simulare richieste HTTP per testare le API REST.
    @Test
    void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")) // viene eseguita una richiesta HTTP GET al percorso "/", utilizzando il mockMvc creato
                .andDo(print()) // stampa a console l'output della richiesta, per aiutare nella diagnosi di eventuali problemi.
                .andExpect(status().isOk()) // equivalente di un assert che verifica che la risposta HTTP abbia uno stato di "200 OK"
                .andExpect(content().string(containsString("Hello, World"))); // e che contenga la stringa "Hello, World"
    }
}

    @Test
    public void getPersonTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/getPerson").accept(MediaType.APPLICATION_JSON)) //effettua una GET
                .andDo(print()) //effettua una stampa della risposta
                .andExpect(status().isOk()) //equivalente di un assert che si aspetta una risposta positiva (200)
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Domenico")); // equivalente di un assert che verifica l'uguaglianza tra due stringhe
    }

    @Test
    public void postTest() throws Exception {
        Person = new Person();
        p.setNome("Domenico");
        p.setCognome("D'Antonio");
        p.setAnnoNascita(1990);

        this.mockMvc
                .perform( MockMvcRequestBuilders.post("/post")
                        .content(new ObjectMapper().writeValueAsString(p)) //Traduco l'oggetto persona in JSON
                        .contentType(MediaType.APPLICATION_JSON) //indico che passerò un JSON nel corpo della richiesta
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) //effettua una stampa della risposta
                .andExpect(status().isCreated()) //equivalente di un assert che si aspetta una risposta positiva (200)
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Sig. Domenico")); // equivalente di un assert che verifica la presenza di un campo nome nel JSON di ritoro
    }
}

 */
