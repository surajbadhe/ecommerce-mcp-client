package com.suraj.ecom;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import io.modelcontextprotocol.client.McpSyncClient;

@SpringBootApplication
public class EcommerceMcpClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceMcpClientApplication.class, args);
	}
	
	@Bean
	ChatClient ollamaChatClient(OllamaChatModel chatModel) {
		return ChatClient.builder(chatModel).build();
	}

	@Bean
	@Primary
	@Qualifier("ecommerce-mcp-server-callback-tool-provider")
	SyncMcpToolCallbackProvider toolCallbackProvider(List<McpSyncClient> mcpSyncClients) {
		return new SyncMcpToolCallbackProvider(mcpSyncClients);
	}

}
