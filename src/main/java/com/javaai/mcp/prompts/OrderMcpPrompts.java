package com.javaai.mcp.prompts;

import org.springframework.ai.mcp.annotation.McpPrompt;
import org.springframework.ai.mcp.annotation.McpPromptParam;
import org.springframework.stereotype.Component;

/**
 * @McpPrompt 示例：暴露"提示词模板"
 *
 * 作用：把团队沉淀的高质量 Prompt 变成可复用的资源，
 *       任何 MCP 客户端都能拿到并使用（不用每个人自己编)。
 */
@Component
public class OrderMcpPrompts {

    @McpPrompt(
            name = "order-complaint",
            description = "生成订单投诉处理话术（客服场景）"
    )
    public String complaintPrompt(
            @McpPromptParam(description = "订单号") String orderNo) {
        return """
                请以客服身份，针对订单 %s 生成一段专业的投诉处理话术。
                要求：
                  1. 先共情（认可用户的情绪）
                  2. 再说明（客观陈述情况）
                  3. 最后给方案（明确的下一步 + 时间承诺）
                不超过 150 字，语气真诚，不要推卸责任。
                """.formatted(orderNo);
    }

    @McpPrompt(
            name = "order-summary",
            description = "把订单信息总结成一句话（用于语音播报）"
    )
    public String summaryPrompt(
            @McpPromptParam(description = "订单详情 JSON") String orderJson) {
        return """
                把下面的订单信息总结成一句适合语音播报的话（不超过 40 字）：
                %s
                """.formatted(orderJson);
    }
}
