/*******************************************************************************
 * Copyright (c) 2025 Kichwa Coders Canada, Inc.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class EchoHttpServer implements Closeable {

	private HttpServer server;

	public EchoHttpServer() throws IOException {
		InetSocketAddress addr = new InetSocketAddress(InetAddress.getLoopbackAddress(), 0);
		server = HttpServer.create(addr, 0);
		createHandlers();
		server.start();
	}

	protected void createHandlers() {
		server.createContext("/get/echo", this::handleGetEcho);
		server.createContext("/post/echo", this::handlePostEcho);
	}

	@Override
	public void close() {
		server.stop(0);
	}

	public int port() {
		return server.getAddress().getPort();
	}

	public String getEchoUrl(String msg) {
		return "http://localhost:" + port() + "/get/echo?msg=" + URLEncoder.encode(msg, StandardCharsets.UTF_8);
	}

	public String postEchoUrl() {
		return "http://localhost:" + port() + "/post/echo";
	}

	protected void handleGetEcho(HttpExchange exchange) throws IOException {
		if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
			exchange.sendResponseHeaders(405, -1);
			return;
		}

		String msg = extractMessageFromQuery(exchange.getRequestURI().getQuery());
		respond(exchange, msg);
	}

	protected void handlePostEcho(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			exchange.sendResponseHeaders(405, -1);
			return;
		}

		byte[] bodyBytes = exchange.getRequestBody().readAllBytes();
		String body = new String(bodyBytes, StandardCharsets.UTF_8);

		respond(exchange, body);
	}

	private String extractMessageFromQuery(String query) {
		if (query == null)
			return "";
		// XXX: This is not real/complete query decoding, add more
		// as needed if tests require it
		for (String part : query.split("&")) {
			if (part.startsWith("msg=")) {
				return URLDecoder.decode(part.substring(4), StandardCharsets.UTF_8);
			}
		}
		return "";
	}

	private void respond(HttpExchange exchange, String text) throws IOException {
		var html = expectedResponse(text);
		byte[] bytes = html.getBytes(StandardCharsets.UTF_8);

		exchange.sendResponseHeaders(200, bytes.length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(bytes);
		}
	}

	public String expectedResponse(String text) {
		var html = String.format("""
				<!DOCTYPE html>
				<html>
				<head>
				    <title>%s</title>
				</head>
				<body>
				    <h1>This is the SWT Test Echo Http Server</h1>
				    <p>This is the echo message (also in title): %s</p>
				</body>
				</html>
				""", text, text);
		return html;
	}

	public static void main(String[] args) throws IOException {
		try (var server = new EchoHttpServer()) {
			System.out.println("started on port " + server.port());
			System.out.println("Try visiting " + server.getEchoUrl("Hello SWT"));
			System.out.println("Will shutdown by pressing newline");
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		}
		System.out.println("shutdown");
	}

}
