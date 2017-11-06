package com.cloudcraftgaming.api.endpoints.v1;

import com.cloudcraftgaming.api.utils.ResponseUtils;
import com.cloudcraftgaming.discal.api.database.DatabaseManager;
import com.cloudcraftgaming.discal.api.enums.announcement.AnnouncementType;
import com.cloudcraftgaming.discal.api.enums.event.EventColor;
import com.cloudcraftgaming.discal.api.object.announcement.Announcement;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.UUID;

import static spark.Spark.halt;

/**
 * Created by Nova Fox on 11/6/17.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCalWebAPI
 */
public class AnnouncementEndpoint {
	public static String getAnnouncement(Request request, Response response) {
		try {
			JSONObject jsonMain = new JSONObject(request.body());
			String guildId = jsonMain.getString("GUILD_ID");
			String announcementId = jsonMain.getString("ANNOUNCEMENT_ID");

			Announcement a = DatabaseManager.getManager().getAnnouncement(UUID.fromString(announcementId), Long.valueOf(guildId));

			if (a != null) {

				response.type("application/json");
				response.status(200);

				JSONObject body = new JSONObject();
				body.put("GUILD_ID", a.getGuildId());
				body.put("ANNOUNCEMENT_ID", a.getAnnouncementId().toString());
				body.put("ANNOUNCEMENT_CHANNEL", a.getAnnouncementChannelId());
				body.put("EVENT_ID", a.getEventId());
				body.put("EVENT_COLOR", a.getEventColor().name());
				body.put("TYPE", a.getAnnouncementType().name());
				body.put("HOURS", a.getHoursBefore());
				body.put("MINUTES", a.getMinutesBefore());
				body.put("INFO", a.getInfo());
				body.put("SUBSCRIBERS_ROLE", a.getSubscriberRoleIds());
				body.put("SUBSCRIBERS_USER", a.getSubscriberUserIds());

				response.body(body.toString());
			} else {
				response.type("application/json");
				response.status(404);
				response.body(ResponseUtils.getJsonResponseMessage("Announcement not found."));
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

	public static String createAnnouncement(Request request, Response response) {
		try {
			JSONObject jsonMain = new JSONObject(request.body());
			String guildId = jsonMain.getString("GUILD_ID");

			Announcement a = new Announcement(Long.valueOf(guildId));

			JSONObject body = new JSONObject(request.body());
			a.setAnnouncementChannelId(body.getString("ANNOUNCEMENT_CHANNEL"));
			a.setEventId(body.getString("EVENT_ID"));
			a.setEventColor(EventColor.fromNameOrHexOrID(body.getString("EVENT_COLOR")));
			a.setAnnouncementType(AnnouncementType.fromValue(body.getString("TYPE")));
			a.setHoursBefore(body.getInt("HOURS"));
			a.setMinutesBefore(body.getInt("MINUTES"));
			a.setInfo(body.getString("INFO"));

			if (DatabaseManager.getManager().updateAnnouncement(a)) {
				response.type("application/json");
				response.status(200);
				response.body(ResponseUtils.getJsonResponseMessage("Announcement successfully created!"));
			} else {
				response.type("application/json");
				response.status(500);
				response.body(ResponseUtils.getJsonResponseMessage("Internal Server Error"));
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