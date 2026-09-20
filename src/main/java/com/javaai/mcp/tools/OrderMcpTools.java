package com.javaai.mcp.tools;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

/**
 * @McpTool 示例：暴露"可执行的动作"
 *
 * ⚠️ 安全铁律：这里**只暴露读操作**。
 *    删除、退款、改地址等写操作永远不要直接暴露成 MCP Tool ——
 *    MCP 是"人人可调"的协议，暴露危险工具 = 给所有 AI 客户端开后门。
 */
@Component
public class OrderMcpTools {

    /**
     * @McpTool 的 description 会自动变成 LLM 判断"要不要调"的依据，必须写清楚。
     * 参数带 userId —— 权限校验在 Service 层统一完成，MCP 层不重复实现。
     */
    @McpTool(description = "根据订单号查询订单状态，返回 待发货/已发货/已签收")
    public String queryOrderStatus(
            @McpToolParam(description = "订单号，例如 A12345") String orderNo,
            @McpToolParam(description = "用户ID，用于权限校验") String userId) {

        // 真实场景：return orderService.getStatus(orderNo, userId);
        //            权限校验（该 userId 是否有权访问该订单）在 Service 层完成
        return switch (orderNo) {
            case "A12345" -> "已发货（预计 2 天送达）";
            case "B67890" -> "已签收";
            default -> "待发货";
        };
    }

    @McpTool(description = "查询指定用户的可用优惠券数量")
    public int countAvailableCoupons(
            @McpToolParam(description = "用户ID") String userId) {
        // 真实场景：return couponService.countAvailable(userId);
        return 3;
    }

    @McpTool(description = "预估指定订单的退款金额（只读，不实际退款）")
    public String estimateRefund(
            @McpToolParam(description = "订单号") String orderNo,
            @McpToolParam(description = "用户ID") String userId) {
        // 注意：这里只"预估"，真正的退款另走人工确认流程
        return "预计可退 299.00 元";
    }
}
