package de.uko.takeaway.api.config;

import de.uko.takeaway.domain.exception.NotFoundException;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
public class DefaultControllerAdvice implements ProblemHandling {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Problem> handleNotFoundException(final NotFoundException exception, final NativeWebRequest request) {
        return handleProblem(Problem.builder()
            .withStatus(Status.NOT_FOUND)
            .withDetail(exception.getMessage())
            .withTitle("Not Found")
            .build(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Problem> handleIllegalARgumentException(final IllegalArgumentException exception, final NativeWebRequest request) {
        return handleProblem(Problem.builder()
            .withStatus(Status.BAD_REQUEST)
            .withDetail(exception.getMessage())
            .withTitle("Bad Request")
            .build(), request);
    }

    @Override
    public URI defaultConstraintViolationType() {
        return URI.create("https://www.lieferando.de/problem/constraint-violation");
    }
}
