package com.javaai.mcp.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 审计切面：记录每一次 MCP 工具调用
 *
 * 记录内容：工具名、参数、耗时、结果 / 异常。
 * 生产环境应把日志落到独立审计表（谁、何时、调了什么、参数、结果、耗时）。
 *
 * 注意：这里拦截所有标了 @McpTool 的方法，业务代码零侵入。
 */
@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    @Around("@annotation(org.springframework.ai.mcp.annotation.McpTool)")
    public Object audit(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        String tool = pjp.getSignature().getName();
        String args = Arrays.toString(pjp.getArgs());

        try {
            Object result = pjp.proceed();
            log.info("[MCP-AUDIT] tool={} args={} cost={}ms result={}",
                    tool, args, System.currentTimeMillis() - start, result);
            return result;
        } catch (Throwable t) {
            log.warn("[MCP-AUDIT] tool={} args={} cost={}ms FAILED: {}",
                    tool, args, System.currentTimeMillis() - start, t.getMessage());
            throw t;
        }
    }
}
