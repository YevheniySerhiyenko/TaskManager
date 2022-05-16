package com.taskmanager.resolver.exception;

import com.taskmanager.exception.DuplicateException;
import com.taskmanager.exception.NotEmptyException;
import com.taskmanager.exception.NotFoundException;
import com.taskmanager.exception.WrongArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class GlobalExceptionHandler extends DefaultHandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveException(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception
    ) {
        ModelAndView parentResult = super.doResolveException(request, response, handler, exception);

        if (parentResult != null) {
            return parentResult;
        }

        try {

            if (exception instanceof DuplicateException) {
                return handleDuplicateException((DuplicateException) exception, response);
            }
            if (exception instanceof NotFoundException) {
                return handleNotFoundException((NotFoundException) exception, response);
            }
            if (exception instanceof NotEmptyException) {
                return handleNotEmptyException((NotEmptyException) exception, response);
            }
            if (exception instanceof WrongArgumentException) {
                return handleWrongIndexException((WrongArgumentException) exception, response);
            }

        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return new ModelAndView();
    }

    private ModelAndView handleNotFoundException(
            NotFoundException exception,
            HttpServletResponse response
    ) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());

        return new ModelAndView();
    }

    private ModelAndView handleNotEmptyException(
            NotEmptyException exception,
            HttpServletResponse response
    ) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());

        return new ModelAndView();
    }

    private ModelAndView handleDuplicateException(
            DuplicateException exception,
            HttpServletResponse response
    ) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value(), exception.getMessage());

        return new ModelAndView();
    }


    private ModelAndView handleWrongIndexException(
            WrongArgumentException exception,
            HttpServletResponse response
    ) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value(), exception.getMessage());

        return new ModelAndView();
    }
}
