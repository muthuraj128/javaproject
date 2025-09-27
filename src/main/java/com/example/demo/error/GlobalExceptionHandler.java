package com.example.demo.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ModelAndView build(String view, HttpStatus status, String message, Throwable ex) {
        ModelAndView mv = new ModelAndView(view);
        mv.addObject("status", status.value());
        mv.addObject("error", status.getReasonPhrase());
        mv.addObject("message", message);
        mv.addObject("errorId", UUID.randomUUID());
        if (ex != null) {
            log.error("ErrorId: {} {} {}", mv.getModel().get("errorId"), status, message, ex);
        } else {
            log.error("ErrorId: {} {} {}", mv.getModel().get("errorId"), status, message);
        }
        return mv;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView notFound(NoHandlerFoundException ex) {
        return build("error/404", HttpStatus.NOT_FOUND, "The page you requested was not found.", ex);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView generic(Exception ex) {
        return build("error/500", HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", ex);
    }
}
