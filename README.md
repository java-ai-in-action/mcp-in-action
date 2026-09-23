# mcp-in-action · 把 Spring AI 的 @Tool 一键暴露成 MCP Server

> 配套文章：篇5《把 Spring AI 的 @Tool 一键暴露成 MCP Server：我用它让 30 个老接口变成 AI 工具》

## ✨ 这个仓库演示什么

1. **`@McpTool` / `@McpResource` / `@McpPrompt` 三种注解**：把 Spring Bean 直接暴露成 MCP 能力
2. **Streamable HTTP 传输**：生产环境首选（SSE 已 deprecated）
3. **安全三件套**：权限校验 + 限流 + 审计，**且业务代码一行不改**
4. **Claude Desktop / Cursor 直连**：不用写一行客户端代码

## 📁 结构

```
src/main/java/com/javaai/mcp/
├── McpServerApplication.java        # 主类
├── tools/OrderMcpTools.java         # @McpTool：可执行动作
├── resources/OrderMcpResources.java # @McpResource：可读数据
├── prompts/OrderMcpPrompts.java     # @McpPrompt：提示词模板
└── config/McpSecurityConfig.java    # 权限 + 限流 + 审计
docs/claude-desktop-config.json      # Claude Desktop 接入配置
```

## 🚀 快速开始

```bash
export OPENAI_API_KEY=sk-xxx   # 或 DASHSCOPE_API_KEY
mvn spring-boot:run
# MCP 端点在 http://localhost:8080/mcp（Streamable HTTP）
```

## 🔌 接入 Claude Desktop

把 `docs/claude-desktop-config.json` 的内容合并进 Claude Desktop 配置文件：

- macOS: `~/Library/Application Support/Claude/claude_desktop_config.json`
- Windows: `%APPDATA%\Claude\claude_desktop_config.json`

```json
{
  "mcpServers": {
    "order-server": { "url": "http://localhost:8080/mcp" }
  }
}
```

重启 Claude Desktop，然后说"查一下订单 A12345 的状态"，它就会调用你的工具。

## 🧩 三种注解速览

| 注解 | 用途 | 类比 |
|---|---|---|
| `@McpTool` | 暴露"可执行的动作" | 函数调用 |
| `@McpResource` | 暴露"可读取的数据" | GET 请求 |
| `@McpPrompt` | 暴露"提示词模板" | 预置 Prompt |

## ⚠️ 安全铁律

> **永远不要把危险操作（删除、转账、改权限）直接暴露成 MCP Tool。**

MCP 是"人人可调"的协议——一个暴露出去的 `deleteOrder`，等于给所有 AI 客户端开了后门。

本仓库的做法：
1. **只暴露读操作**，写操作要求人工确认
2. **参数带 userId**，Service 层统一鉴权
3. **限流 + 审计**，全程可追溯
4. **工具白名单**，危险方法永不注册

## 📚 参考

- [MCP 官方规范](https://modelcontextprotocol.io/)
- [Spring AI MCP 文档](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html)

## License

MIT

> 版本说明：本仓库基于 Spring AI 2.0 GA 与 MCP 1.0 规范编写，具体 API 与版本号以官方仓库为准。

---

## 📮 关注公众号「Java程序员面试宝典」

<img src="docs/wechat-qrcode.png" width="720" alt="扫码关注公众号：Java程序员面试宝典" />

**微信搜一搜「Java程序员面试宝典」**，或直接扫码关注。

- 📖 **「Java AI 实战派」系列 10 篇长文** —— 公众号首发，不定时更新
- 🧰 每篇都配**可运行的开源仓库**（这套系列一共 8 个仓库）
- 🕳️ 只讲**踩过的坑**，不讲空概念

> 这个仓库帮到你了吗？点个 ⭐ **Star** 支持一下，再去公众号坐坐 👆
