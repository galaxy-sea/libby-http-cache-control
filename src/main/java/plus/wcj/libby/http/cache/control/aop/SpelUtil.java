/*
 * Copyright 2021-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package plus.wcj.libby.http.cache.control.aop;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/5/21
 */
public class SpelUtil {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    public static boolean condition(String condition, EvaluationContext context) {
        return "".equals(condition) || StringUtils.hasText(condition)
                ? PARSER.parseExpression(condition).getValue(context, boolean.class)
                : false;
    }


    public static String parser(String spelExpression, EvaluationContext context) {
        return PARSER.parseExpression(spelExpression).getValue(context, String.class);
    }

    public static String[] parser(EvaluationContext context, String... spelExpressions) {
        String[] evaluationResults = new String[spelExpressions.length];
        for (int i = spelExpressions.length - 1; i >= 0; i--) {
            String evaluationResult = PARSER.parseExpression(spelExpressions[i]).getValue(context, String.class);
            evaluationResults[i] = evaluationResult;
        }
        return evaluationResults;
    }

    public static EvaluationContext toEvaluationContext(Method method, Object[] arguments) {
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
