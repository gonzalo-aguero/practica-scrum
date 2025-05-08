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
 * Validate that the dni value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = AdministradorDniUnique.AdministradorDniUniqueValidator.class
)
public @interface AdministradorDniUnique {

    String message() default "{Exists.administrador.dni}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class AdministradorDniUniqueValidator implements ConstraintValidator<AdministradorDniUnique, String> {

        private final AdministradorService administradorService;
        private final HttpServletRequest request;

        public AdministradorDniUniqueValidator(final AdministradorService administradorService,
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
            if (currentId != null && value.equalsIgnoreCase(administradorService.get(Long.parseLong(currentId)).getDni())) {
                // value hasn't changed
                return true;
            }
            return !administradorService.dniExists(value);
        }

    }

}
