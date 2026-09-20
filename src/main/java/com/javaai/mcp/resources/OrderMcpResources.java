package com.javaai.mcp.resources;

import org.springframework.ai.mcp.annotation.McpResource;
import org.springframework.stereotype.Component;

/**
 * @McpResource 示例：暴露"可读取的数据"
 *
 * 与 @McpTool 的区别：
 *   - Tool   = 可执行的动作（可能改变状态）
 *   - Resource = 可读取的数据（只读，类似 HTTP GET）
 *
 * uri 支持模板变量，客户端可以像访问 REST 资源一样访问。
 */
@Component
public class OrderMcpResources {

    @McpResource(
            uri = "order://{orderNo}/detail",
            name = "订单详情",
            description = "读取指定订单的完整信息（JSON 格式）"
    )
    public String getOrderDetail(String orderNo) {
        // 真实场景：return json(orderService.findDetail(orderNo));
        return """
                {
                  "orderNo": "%s",
                  "status": "已发货",
                  "amount": 299.00,
                  "items": ["无线耳机", "保护壳"],
                  "createTime": "2026-09-15 10:00:00"
                }
                """.formatted(orderNo);
    }

    @McpResource(
            uri = "config://shipping/regions",
            name = "可配送地区",
            description = "返回当前支持配送的地区列表"
    )
    public String shippingRegions() {
        return "全国（新疆、西藏部分地区除外）";
    }
}
