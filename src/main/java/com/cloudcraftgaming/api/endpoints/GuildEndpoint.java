package com.cloudcraftgaming.api.endpoints;

import com.cloudcraftgaming.discal.api.database.DatabaseManager;
import com.cloudcraftgaming.discal.api.object.GuildSettings;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * Created by Nova Fox on 11/5/17.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCalWebAPI
 */
public class GuildEndpoint {
	public static String getSettings(Request request, Response response) {
		if (!request.contentType().equalsIgnoreCase("application/json")) {
			halt(400, "Bad Request");
		}
		try {
			JSONObject jsonMain = new JSONObject(request.body());
			String guildId = jsonMain.getString("GUILD_ID");

			GuildSettings settings = DatabaseManager.getManager().getSettings(Long.valueOf(guildId));

			response.type("application/json");
			response.status(200);

			JSONObject body = new JSONObject();
			body.put("GUILD_ID", settings.getGuildID());
			body.put("EXTERNAL_CALENDAR", settings.useExternalCalendar());
			body.put("CONTROL_ROLE", settings.getControlRole());
			body.put("DISCAL_CHANNEL", settings.getDiscalChannel());
			body.put("SIMPLE_ANNOUNCEMENT", settings.usingSimpleAnnouncements());
			body.put("LANG", settings.getLang());
			body.put("PREFIX", settings.getPrefix());
			body.put("PATRON_GUILD", settings.isPatronGuild());
			body.put("MAX_CALENDARS", settings.getMaxCalendars());

			response.body(body.toString());
		} catch (Exception e) {
			e.printStackTrace();
			halt(500, "Internal Server Error");
		}
		return response.body();
	}
}