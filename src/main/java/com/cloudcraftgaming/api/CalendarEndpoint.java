package com.cloudcraftgaming.api;

import com.cloudcraftgaming.api.utils.ResponseUtils;
import com.cloudcraftgaming.discal.api.database.DatabaseManager;
import com.cloudcraftgaming.discal.api.object.GuildSettings;
import com.cloudcraftgaming.discal.api.object.calendar.CalendarData;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * Created by Nova Fox on 11/7/17.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCalWebAPI
 */
@SuppressWarnings("ThrowableNotThrown")
public class CalendarEndpoint {
	public static String getCalendar(Request request, Response response) {
		try {
			JSONObject jsonMain = new JSONObject(request.body());
			String guildId = jsonMain.getString("GUILD_ID");
			Integer calNumber = jsonMain.getInt("CALENDAR_NUMBER");

			GuildSettings settings = DatabaseManager.getManager().getSettings(Long.valueOf(guildId));
			CalendarData calendar = DatabaseManager.getManager().getCalendar(Long.valueOf(guildId), calNumber);

			if (!calendar.getCalendarAddress().equalsIgnoreCase("primary")) {

				JSONObject body = new JSONObject();
				body.put("GUILD_ID", guildId);
				body.put("CALENDAR_NUMBER", calNumber);
				body.put("EXTERNAL", settings.useExternalCalendar());
				body.put("CALENDAR_ID", calendar.getCalendarId());
				body.put("CALENDAR_ADDRESS", calendar.getCalendarAddress());

				response.type("application/json");
				response.status(200);
				response.body(body.toString());
			} else {
				response.type("application/json");
				response.status(404);
				response.body(ResponseUtils.getJsonResponseMessage("Calendar not found"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			halt(400, "Bad Request");
		} catch (Exception e) {
			e.printStackTrace();
			halt(500, "Internal Server Error");
		}
		return response.body();
	}
}