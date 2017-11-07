package com.cloudcraftgaming.api;

import com.cloudcraftgaming.api.endpoints.v1.AnnouncementEndpoint;
import com.cloudcraftgaming.api.endpoints.v1.GuildEndpoint;
import com.cloudcraftgaming.discal.api.database.DatabaseManager;
import com.cloudcraftgaming.discal.api.file.ReadFile;
import com.cloudcraftgaming.discal.api.object.BotSettings;

import static spark.Spark.*;

/**
 * Created by Nova Fox on 11/4/17.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCalWebAPI
 */
@SuppressWarnings("ThrowableNotThrown")
public class Main {
	@SuppressWarnings("FieldCanBeLocal")
	private static BotSettings settings;

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
			}
			//Check authorization
			if (request.headers().contains("Authorization") && !request.headers("Authorization").equals("API_KEY")) {
				halt(401, "Unauthorized");
			}
			if (!request.contentType().equalsIgnoreCase("application/json")) {
				halt(400, "Bad Request");
			}
		});

		path("/api/v1/discal", () -> {
			before("/*", (q, a) -> System.out.println("Received API call from: " + q.ip() + "; Host:" + q.host()));
			path("/guild", () -> {
				path("/settings", () -> {
					post("/get", GuildEndpoint::getSettings);
					post("/update", GuildEndpoint::updateSettings);
				});
			});
			path("/announcement", () -> {
				post("/get", AnnouncementEndpoint::getAnnouncement);
				post("/create", AnnouncementEndpoint::createAnnouncement);
				post("/update", AnnouncementEndpoint::updateAnnouncement);
				post("/delete", AnnouncementEndpoint::deleteAnnouncement);
				post("/list", AnnouncementEndpoint::listAnnouncements);
			});
		});
	}
}