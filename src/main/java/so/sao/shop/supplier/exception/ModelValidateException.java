package so.sao.shop.supplier.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guangpu.yan
 * @create 2017-08-29
 **/
public class ModelValidateException extends DataConstraintException {
    private List<ValidateError> globalErrors = new ArrayList<>();
    private Map<String, List<ValidateError>> fieldErrors = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ModelValidateException(String message) {
        super(message);
    }

    public void addGlobalError(ValidateError validateError) {
        this.globalErrors.add(validateError);
    }

    public void addFieldErrors(String field, ValidateError validateError) {
        logger.error(validateError.getMessage());
        List<ValidateError> errors = fieldErrors.get(field);
        if (errors == null) {
            errors = new ArrayList<>();
            fieldErrors.put(field, errors);
        }
        errors.add(validateError);
    }

    public List<ValidateError> getGlobalErrors() {
        return globalErrors;
    }

    public Map<String, List<ValidateError>> getFieldErrors() {
        return fieldErrors;
    }
}
