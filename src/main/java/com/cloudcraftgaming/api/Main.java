package com.cloudcraftgaming.api;

import static spark.Spark.*;

/**
 * Created by Nova Fox on 11/4/17.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCalWebAPI
 */
public class Main {
	@SuppressWarnings("ThrowableNotThrown")
	public static void main(String[] args) {
		before("/api/*", (request, response) -> {
			if (!request.requestMethod().equalsIgnoreCase("POST")) {
				System.out.println("Denied '" + request.requestMethod() + "' access from: " + request.ip());
				halt(405, "Method not allowed!");
			} else {
				//Check authorization
			}
		});

		path("/api/discal", () -> {
			before("/*", (q, a) -> System.out.println("Received API call"));
			path("/test", () -> {

			});
			path("/username", () -> {

			});
		});
	}
}