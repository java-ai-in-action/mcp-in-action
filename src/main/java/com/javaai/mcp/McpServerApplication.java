package com.javaai.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MCP Server 启动类
 *
 * 启动后 MCP 端点在 http://localhost:8080/mcp（Streamable HTTP）
 * 任意 MCP 客户端（Claude Desktop / Cursor / 自研 Agent）均可连接。
 *
 * 配套文章：篇5《把 Spring AI 的 @Tool 一键暴露成 MCP Server》
 */
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
}
