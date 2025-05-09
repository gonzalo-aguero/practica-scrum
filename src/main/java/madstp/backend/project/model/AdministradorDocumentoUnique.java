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
import madstp.backend.project.service.AdministradorService;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the documento value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = AdministradorDocumentoUnique.AdministradorDocumentoUniqueValidator.class
)
public @interface AdministradorDocumentoUnique {

    String message() default "{Exists.administrador.documento}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class AdministradorDocumentoUniqueValidator implements ConstraintValidator<AdministradorDocumentoUnique, String> {

        private final AdministradorService administradorService;
        private final HttpServletRequest request;

        public AdministradorDocumentoUniqueValidator(final AdministradorService administradorService,
                final HttpServletRequest request) {
            this.administradorService = administradorService;
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
            if (currentId != null && value.equalsIgnoreCase(administradorService.get(Long.parseLong(currentId)).getDocumento())) {
                // value hasn't changed
                return true;
            }
            return !administradorService.documentoExists(value);
        }

    }

}
