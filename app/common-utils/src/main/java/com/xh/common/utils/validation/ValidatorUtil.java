package com.xh.common.utils.validation;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidatorUtil {

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.byProvider( HibernateValidator.class )
            .configure()
            .failFast( true )
            .buildValidatorFactory();

    private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

    public static String validate(Object... args) {
        for (Object arg : args) {
            String message = doValidate(arg);
            if (message != null) {
                return message;
            }
        }
        return null;
    }

    private static String doValidate(Object arg) {
        Set<ConstraintViolation<Object>> violations = VALIDATOR.validate(arg);
        if(CollectionUtils.isEmpty(violations)){
            return null;
        } else {
            return violations.stream().findFirst().get().getMessage();
        }
    }
}
