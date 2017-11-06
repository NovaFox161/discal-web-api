package com.cloudcraftgaming.api;

import com.cloudcraftgaming.api.endpoints.GuildEndpoint;
import com.cloudcraftgaming.discal.api.database.DatabaseManager;
import com.cloudcraftgaming.discal.api.file.ReadFile;
import com.cloudcraftgaming.discal.api.object.BotSettings;

import static spark.Spark.*;

/**
 * Created by Nova Fox on 11/4/17.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCalWebAPI
 */
public class Main {
	static BotSettings settings;

	@SuppressWarnings("ThrowableNotThrown")
	public static void main(String[] args) {
		if (args.length < 1) {
			throw new NullPointerException("No bot settings provided!");
		}

		settings = ReadFile.readBotSettings(args[0]);

		DatabaseManager.getManager().connectToMySQL(settings);

		before("/api/*", (request, response) -> {
			if (!request.requestMethod().equalsIgnoreCase("POST")) {
				System.out.println("Denied '" + request.requestMethod() + "' access from: " + request.ip());
				halt(405, "Method not allowed");
			} else {
				//Check authorization
				if (request.headers().contains("Authorization") && !request.headers("Authorization").equalsIgnoreCase("API_KEY")) {
					halt(401, "Unauthorized");
				}
			}
		});

		path("/api/discal", () -> {
			before("/*", (q, a) -> System.out.println("Received API call"));
			path("/guild", () -> {
				path("/settings", () -> {
					post("/get", GuildEndpoint::getSettings);
				});
			});
		});
	}
}