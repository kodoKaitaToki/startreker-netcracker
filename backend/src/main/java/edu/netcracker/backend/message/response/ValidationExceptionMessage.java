package edu.netcracker.backend.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationExceptionMessage extends RequestExceptionMessage {

    private Map<String, String> errors;

    public static ValidationExceptionMessage createValidationExceptionMessage(List<FieldError> fieldErrors,
                                                                              Class errorClass) {
        ValidationExceptionMessage validationExceptionMessage = new ValidationExceptionMessage();
        validationExceptionMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        validationExceptionMessage.setError(HttpStatus.BAD_REQUEST.name());
        validationExceptionMessage.setMessage("Validation exception");
        validationExceptionMessage.setTimestamp(System.currentTimeMillis());

        validationExceptionMessage.setErrors(
                fieldErrors.stream().collect(Collectors.toMap(
                        fieldError -> createJsonProperty(fieldError.getField(), errorClass),
                        DefaultMessageSourceResolvable::getDefaultMessage))
        );

        return validationExceptionMessage;
    }

    private static String createJsonProperty(String fieldName, Class errorClass) {
        String jsonFieldName = fieldName;

        Field field = null;
        while (errorClass != null) {
            try {
                field = errorClass.getDeclaredField(fieldName);

                break;
            } catch (NoSuchFieldException ignored){}

            errorClass = errorClass.getSuperclass();
        }

        if (field != null && field.isAnnotationPresent(JsonProperty.class)) {
            jsonFieldName = field.getAnnotation(JsonProperty.class).value();
        }

        return jsonFieldName;
    }
}
