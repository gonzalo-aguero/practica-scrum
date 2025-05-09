package madstp.backend.project.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import madstp.backend.project.service.TitularService;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the documento value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = TitularDocumentoUnique.TitularDocumentoUniqueValidator.class
)
public @interface TitularDocumentoUnique {

    String message() default "{Exists.titular.documento}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class TitularDocumentoUniqueValidator implements ConstraintValidator<TitularDocumentoUnique, String> {

        private final TitularService titularService;
        private final HttpServletRequest request;

        public TitularDocumentoUniqueValidator(final TitularService titularService,
                                               final HttpServletRequest request) {
            this.titularService = titularService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(titularService.get(Long.parseLong(currentId)).getDocumento())) {
                // value hasn't changed
                return true;
            }
            return !titularService.documentoExists(value);
        }

    }

}
