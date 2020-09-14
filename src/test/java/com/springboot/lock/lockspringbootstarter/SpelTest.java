package com.springboot.lock.lockspringbootstarter;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @ClassName SpelTest
 * @Description
 * @Author 温少
 * @Date 2020/9/11 9:16 上午
 * @Version V1.0
 **/
public class SpelTest {

    public static void main(String[] args) {
        //创建SpEL表达式的解析器
        ExpressionParser parser=new SpelExpressionParser();
        //解析表达式需要的上下文，解析时有一个默认的上下文
        EvaluationContext ctx = new StandardEvaluationContext();
        //在上下文中设置变量，变量名为user，内容为user对象
        ctx.setVariable("id", 123);
        Object value = parser.parseExpression("#id + '_aaa'").getValue(ctx);
        System.out.println(value);


    }

}
