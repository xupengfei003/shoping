package so.sao.shop.supplier.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.List;
import java.util.Set;

/**
 * @author guangpu.yan
 * @create 2017-08-29
 **/
public class ModelValidateDetailBuilder {
    public static ModelValidateDetail from(BindException exception) {
        List<ObjectError> globalErrors = exception.getGlobalErrors();
        List<FieldError> fieldErrors = exception.getFieldErrors();
        return ModelValidateDetailBuilder.buildFromErrors(globalErrors, fieldErrors);
    }

    public static ModelValidateDetail from(MethodArgumentNotValidException exception) {
        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        return ModelValidateDetailBuilder.buildFromErrors(globalErrors, fieldErrors);
    }

    public static ModelValidateDetail from(ConstraintViolationException exception) {
        return ModelValidateDetailBuilder.buildFromViolations(exception.getConstraintViolations());
    }

    private static ModelValidateDetail buildFromViolations(Set<ConstraintViolation<?>> violations) {
        ModelValidateDetail result = new ModelValidateDetail();
        for (ConstraintViolation violation: violations) {
            ConstraintDescriptor<?> descriptor = violation.getConstraintDescriptor();
            String errorCode = descriptor.getAnnotation().annotationType().getSimpleName();
            String errorMessage = violation.getMessage();
            ValidateError validateError = new ValidateError(errorCode, errorMessage);
            Path propertyPath = violation.getPropertyPath();
            if (propertyPath == null) {
                result.addGlobalError(validateError);
            } else {
                result.addFieldError(propertyPath.toString(), validateError);
            }
        }
        return result;
    }

    private static ModelValidateDetail buildFromErrors(List<ObjectError> globalErrors, List<FieldError> fieldErrors) {
        ModelValidateDetail result = new ModelValidateDetail();
        for (int i = 0; i < globalErrors.size(); i++) {
            ObjectError error = globalErrors.get(i);
            ValidateError validateError = new ValidateError(error.getCode(), error.getDefaultMessage());
            result.addGlobalError(validateError);
        }
        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError error = fieldErrors.get(i);
            String field = error.getField();
            ValidateError validateError = new ValidateError(error.getCode(), error.getDefaultMessage());
            result.addFieldError(field, validateError);
        }
        return result;
    }
}
