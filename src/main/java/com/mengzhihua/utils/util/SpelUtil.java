package com.mengzhihua.utils.util;

import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Spring Expression Language evaluator.
 */
public final class SpelUtil {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private SpelUtil() {
    }

    public static <T> T eval(String expression, Object root, Class<T> type) {
        AssertUtil.notBlank(expression, "expression must not be blank");
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        return PARSER.parseExpression(expression).getValue(context, type);
    }

    public static Object eval(String expression, Object root) {
        return eval(expression, root, Object.class);
    }
}
