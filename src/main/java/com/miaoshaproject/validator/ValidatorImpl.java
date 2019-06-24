package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorImpl implements InitializingBean {
    private Validator validator;

    // 实现校验方法并返回校验结果
    public ValidationResult validate(Object bean) {
        final ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet;
        constraintViolationSet = validator.validate(bean);
        if (constraintViolationSet.size() > 0) {
            // 有错误
            result.setHasError(true);
            constraintViolationSet.forEach(constraintViolation -> {
                // 错误信息
                String errMsg = constraintViolation.getMessage();
                // 出错字段
                String propertyName = constraintViolation.getPropertyPath().toString();
                result.getErrMsgMap().put(propertyName, errMsg);
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 将hibernate validator 通过工厂初始化方式使其实例话
//        this.validator = (Validator) Validation.buildDefaultValidatorFactory().getValidator();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
