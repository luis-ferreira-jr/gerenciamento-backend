package com.gerenciamento.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null) {
            return false;
        }

        String digits = cpf.replaceAll("\\D", "");

        if (digits.length() != 11 || digits.chars().distinct().count() == 1) {
            return false;
        }

        int firstCheck = calculateCheckDigit(digits, 9, 10);
        if (firstCheck != (digits.charAt(9) - '0')) {
            return false;
        }

        int secondCheck = calculateCheckDigit(digits, 10, 11);
        return secondCheck == (digits.charAt(10) - '0');
    }

    private int calculateCheckDigit(String digits, int length, int firstWeight) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += (digits.charAt(i) - '0') * (firstWeight - i);
        }

        int remainder = 11 - (sum % 11);
        return remainder >= 10 ? 0 : remainder;
    }
}
