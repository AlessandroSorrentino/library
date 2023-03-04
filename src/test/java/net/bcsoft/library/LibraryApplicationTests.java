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



}


