package net.bcsoft.library.controller.advice;

import net.bcsoft.library.exception.BadRequestException;
import net.bcsoft.library.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

// La classe GlobalExceptionHandler fornisce un modo per gestire le eccezioni in modo centralizzato all'interno dell'applicazione
// Una volta configurato, il meccanismo di gestione degli errori gestisce automaticamente le eccezioni all'interno dell'applicazione. Quando viene sollevata un'eccezione, Spring cerca automaticamente un gestore di eccezioni configurato per gestirla
@ControllerAdvice // è un componente che fornisce consigli (advice) per il controllo delle eccezioni
// ci permette d'intercettare e modificare i valori restituiti dai metodi del controller.
@ResponseBody // i metodi restituiranno il contenuto della risposta anziché una view.
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundException.class)  // indica che il metodo gestirà una determinata eccezione
    @ResponseStatus(HttpStatus.NOT_FOUND) // imposta lo stato HTTP della risposta.
    String notFoundHandler(NotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badRequestHandler(BadRequestException ex) {
        return ex.getMessage();
    }
}
