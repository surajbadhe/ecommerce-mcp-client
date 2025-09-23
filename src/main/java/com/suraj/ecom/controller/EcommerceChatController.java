package com.suraj.ecom.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class EcommerceChatController {

	private final ChatClient chatClient;
	
	
	@Qualifier("ecommerce-mcp-server-callback-tool-provider")
	private final SyncMcpToolCallbackProvider syncMcpToolCallbackProvider;

	/**
	 * The ChatClient is built with the SyncMcpToolCallbackProvider, which gives the
	 * LLM access to all the tools on the remote MCP server.
	 */
	@PostMapping("/mcp/chat")
	public String chat(@RequestBody String message) {
		return chatClient.prompt().user(message).toolCallbacks(syncMcpToolCallbackProvider).call().content();
	}
}
