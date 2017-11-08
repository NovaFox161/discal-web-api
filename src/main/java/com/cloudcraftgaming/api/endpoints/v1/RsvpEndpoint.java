package com.cloudcraftgaming.api.endpoints.v1;

import com.cloudcraftgaming.discal.api.database.DatabaseManager;
import com.cloudcraftgaming.discal.api.object.event.RsvpData;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

/**
 * Created by Nova Fox on 11/8/17.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCalWebAPI
 */
public class RsvpEndpoint {
	public static String getRsvp(Request request, Response response) {
		try {
			JSONObject jsonMain = new JSONObject(request.body());
			String guildId = jsonMain.getString("GUILD_ID");
			String eventId = jsonMain.getString("EVENT_ID");

			RsvpData rsvp = DatabaseManager.getManager().getRsvpData(Long.valueOf(guildId), eventId);

			JSONObject body = new JSONObject();
			body.put("GUILD_ID", guildId);
			body.put("EVENT_ID", eventId);
			body.put("EVENT_END", rsvp.getEventEnd());
			body.put("ON_TIME", rsvp.getGoingOnTime());
			body.put("LATE", rsvp.getGoingLate());
			body.put("UNDECIDED", rsvp.getUndecided());
			body.put("NOT_GOING", rsvp.getNotGoing());

			response.type("application/json");
			response.status(200);
			response.body(body.toString());
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