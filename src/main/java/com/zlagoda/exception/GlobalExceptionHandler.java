package com.zlagoda.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", e);
        modelAndView.setViewName("error/404");

        log.debug("[EXCEPTION_HANDLER] handled {}", e.getClass().getName());
        return modelAndView;
    }

    @ExceptionHandler(value = EntityCreationException.class)
    public ModelAndView handleNotFoundException(EntityCreationException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", e);
        modelAndView.setViewName("error/400");

        log.debug("[EXCEPTION_HANDLER] handled {}", e.getClass().getName());
        return modelAndView;
    }

}
