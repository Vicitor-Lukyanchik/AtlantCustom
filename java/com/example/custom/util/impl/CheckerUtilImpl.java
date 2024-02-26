package com.example.custom.util.impl;

import com.example.custom.util.CheckerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CheckerUtilImpl implements CheckerUtil {

    private static final String UNDERSCORE = "_";
    private static final String LITTLE_DASH = "-";
    private static final String VERTICAL_DASH = "|";
    private static final String LATTICE = "#";
    private static final String QUESTION_MARK = "?";
    private static final String EXCLAMATION_MARK = "!";
    private static final String AMPERSAND = "&";
    private static final String DOG = "@";
    private static final String DOLLAR = "$";
    private static final String POINT = ".";
    private static final String COMMA = ",";
    private static final String POINT_WITH_COMMA = ";";
    private static final String COLON = ":";
    private static final String BIRD = "^";
    private static final String STAR = "*";
    private static final String NUMBER = "№";
    private static final String PERCENT = "%";
    private static final String DASH = "'";
    private static final String TWO_DASHES = "\"";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String PLUS = "+";
    private static final String EQUALS = "=";
    private static final String LEFT_SQUARE_PARENTHESIS = "[";
    private static final String RIGHT_SQUARE_PARENTHESIS = "]";
    private static final String LEFT_FIGURE_PARENTHESIS = "}";
    private static final String RIGHT_FIGURE_PARENTHESIS = "{";
    private static final String ACUTE = "`";
    private static final String TILDE = "~";
    private static final String LESS_THAN = "<";
    private static final String MORE_THAN = ">";
    private static final String DECLARATION_ERROR_MESSAGE = "Номер деларации не может содержать символ";
    private static final String PLURAL_LETTER = "(ы)";
    private static final String EMPTY_STRING = "";
    private static final String SPACE = " ";

    @Override
    public String checkDeclarationNumber(String number) {
        List<String> illegalSymbols = findAllUsedIllegalSymbols(number);

        if (!illegalSymbols.isEmpty()) {
            return buildErrorMessage(illegalSymbols);
        }

        return EMPTY_STRING;
    }

    private List<String> findAllUsedIllegalSymbols(String string) {
        List<String> symbols = new ArrayList<>();
        if (string.contains(UNDERSCORE)) {
            symbols.add(UNDERSCORE);
        }
        if (string.contains(TILDE)) {
            symbols.add(TILDE);
        }
        if (string.contains(LESS_THAN)) {
            symbols.add(LESS_THAN);
        }
        if (string.contains(MORE_THAN)) {
            symbols.add(MORE_THAN);
        }
        if (string.contains(ACUTE)) {
            symbols.add(ACUTE);
        }
        if (string.contains(LATTICE)) {
            symbols.add(LATTICE);
        }
        if (string.contains(LITTLE_DASH)) {
            symbols.add(LITTLE_DASH);
        }
        if (string.contains(VERTICAL_DASH)) {
            symbols.add(VERTICAL_DASH);
        }
        if (string.contains(QUESTION_MARK)) {
            symbols.add(QUESTION_MARK);
        }
        if (string.contains(EXCLAMATION_MARK)) {
            symbols.add(EXCLAMATION_MARK);
        }
        if (string.contains(AMPERSAND)) {
            symbols.add(AMPERSAND);
        }
        if (string.contains(DOG)) {
            symbols.add(DOG);
        }
        if (string.contains(DOLLAR)) {
            symbols.add(DOLLAR);
        }
        if (string.contains(POINT)) {
            symbols.add(POINT);
        }
        if (string.contains(COMMA)) {
            symbols.add(COMMA);
        }
        if (string.contains(POINT_WITH_COMMA)) {
            symbols.add(POINT_WITH_COMMA);
        }
        if (string.contains(COLON)) {
            symbols.add(COLON);
        }
        if (string.contains(BIRD)) {
            symbols.add(BIRD);
        }
        if (string.contains(STAR)) {
            symbols.add(STAR);
        }
        if (string.contains(NUMBER)) {
            symbols.add(NUMBER);
        }
        if (string.contains(PERCENT)) {
            symbols.add(PERCENT);
        }
        if (string.contains(DASH)) {
            symbols.add(DASH);
        }
        if (string.contains(TWO_DASHES)) {
            symbols.add(TWO_DASHES);
        }
        if (string.contains(LEFT_PARENTHESIS)) {
            symbols.add(LEFT_PARENTHESIS);
        }
        if (string.contains(RIGHT_PARENTHESIS)) {
            symbols.add(RIGHT_PARENTHESIS);
        }
        if (string.contains(PLUS)) {
            symbols.add(PLUS);
        }
        if (string.contains(EQUALS)) {
            symbols.add(EQUALS);
        }
        if (string.contains(LEFT_SQUARE_PARENTHESIS)) {
            symbols.add(LEFT_SQUARE_PARENTHESIS);
        }
        if (string.contains(RIGHT_SQUARE_PARENTHESIS)) {
            symbols.add(RIGHT_SQUARE_PARENTHESIS);
        }
        if (string.contains(LEFT_FIGURE_PARENTHESIS)) {
            symbols.add(LEFT_FIGURE_PARENTHESIS);
        }
        if (string.contains(RIGHT_FIGURE_PARENTHESIS)) {
            symbols.add(RIGHT_FIGURE_PARENTHESIS);
        }
        return symbols;
    }

    private static String buildErrorMessage(List<String> illegalSymbols) {
        StringBuilder illegalSymbolsString = new StringBuilder(illegalSymbols.get(0));
        for (int i = 1; i < illegalSymbols.size(); i++) {
            illegalSymbolsString.append(COMMA).append(SPACE).append(illegalSymbols.get(i));
        }

        return DECLARATION_ERROR_MESSAGE + buildPluralLetter(illegalSymbols.size()) + COLON + illegalSymbolsString;
    }

    private static String buildPluralLetter(int symbolsCount) {
        if (symbolsCount > 1) {
            return PLURAL_LETTER;
        }
        return EMPTY_STRING;
    }
}
