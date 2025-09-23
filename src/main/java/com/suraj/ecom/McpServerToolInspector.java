package com.suraj.ecom;

import java.util.List;

import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.modelcontextprotocol.client.McpSyncClient;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class McpServerToolInspector implements CommandLineRunner {

    private final List<McpSyncClient> mcpClients;

    @Override
    public void run(String... args) throws Exception {
    	System.out.println("\n=============================================");
        System.out.println(" DISCOVERING MCP SERVER TOOLS");
        System.out.println("=============================================");

        for (McpSyncClient client : mcpClients) {
            System.out.println("  Connected to MCP Server: " + client.getClientInfo().name());

            SyncMcpToolCallbackProvider provider = new SyncMcpToolCallbackProvider(List.of(client));
            List<ToolCallback> toolCallbacks = List.of(provider.getToolCallbacks());

            if (toolCallbacks.isEmpty()) {
                System.out.println(" No tools found on this MCP client.");
            } else {
                for (ToolCallback toolCallback : toolCallbacks) {
                    ToolDefinition toolDefinition = toolCallback.getToolDefinition();
                    System.out.println("  ✅ ToolName    : " + toolDefinition.name());
                    System.out.println("     Description : " + toolDefinition.description());
                    System.out.println("     Input Schema: " + toolDefinition.inputSchema());
                    System.out.println("=============================================");
                }
            }
        }
    }
}