package me.redis;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import redis.clients.jedis.Jedis;

public class RedisConnector {
	private static Jedis jedis = new Jedis("localhost");;

	public static void start() {
		jedis.set("ffhs", "Redis tutorial");
	}

	public static Object getContacts() {
		Map<String, String> contactMap;
		JsonArray arr = new JsonArray();
		String counter = jedis.get("counter");
		for (int key = 1; key <= Integer.parseInt(counter); key++) {
			contactMap = jedis.hgetAll("contact:" + key);
			JsonObject contact = new JsonObject();
			contact.put("id", contactMap.get("id"));
			contact.put("firstname", contactMap.get("firstname"));
			contact.put("name", contactMap.get("name"));
			contact.put("street", contactMap.get("street"));
			contact.put("city", contactMap.get("city"));
			contact.put("zip", contactMap.get("zip"));
			arr.add(contact);
		}
		return arr;
	}

	public static Object addUpdate(JsonObject contact) {
		try {
			Map<String, String> contactMap = new HashMap<String, String>();
			String id = contact.getString("id");
			contactMap.put("firstname", contact.getString("firstname"));
			contactMap.put("name", contact.getString("name"));
			contactMap.put("street", contact.getString("street"));
			contactMap.put("zip", contact.getString("zip"));
			contactMap.put("city", contact.getString("city"));
			if (!id.equals("-1")) {
				 jedis.hset("contact:" + id, "firstname", contactMap.get("firstname"));
				 jedis.hset("contact:" + id, "name", contactMap.get("name"));
				 jedis.hset("contact:" + id, "street", contactMap.get("street"));
				 jedis.hset("contact:" + id, "zip", contactMap.get("zip"));
				 jedis.hset("contact:" + id, "city", contactMap.get("city"));
			} else {
				long idnr = jedis.incr("counter");
				contact.put("id", String.valueOf(idnr));
				contactMap.put("id", String.valueOf(idnr));
				jedis.hmset("contact:" + String.valueOf(idnr), contactMap);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contact;
	}
}
