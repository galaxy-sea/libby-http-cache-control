package plus.wcj.libby.http.cache.control.aop;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/21
 */
public class SpelUtil {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    public static String parser(Method method, Object[] arguments, String spelExpression) {
        EvaluationContext context = toEvaluationContext(method, arguments);
        return PARSER.parseExpression(spelExpression).getValue(context).toString();
    }

    public static String[] parser(Method method, Object[] arguments, String... spelExpressions) {
        EvaluationContext context = toEvaluationContext(method, arguments);
        String[] evaluationResults = new String[spelExpressions.length];
        for (int i = spelExpressions.length - 1; i >= 0; i--) {
            String evaluationResult = PARSER.parseExpression(spelExpressions[i]).getValue(context).toString();
            evaluationResults[i] = evaluationResult;
        }
        return evaluationResults;
    }

    private static EvaluationContext toEvaluationContext(Method method, Object[] arguments) {
        EvaluationContext context = new StandardEvaluationContext();
        if (arguments != null && arguments.length > 0) {
            String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
            for (int i = arguments.length - 1; i >= 0; i--) {
                context.setVariable(parameterNames[i], arguments[i]);
            }
        }
        return context;
    }
}
