package de.n8M4.hypixelapiforge;

import net.hypixel.api.HypixelAPI;
import java.util.UUID;

public class LevelHead {

    public static String API_KEY = "api key";
    public static void requestPlayerLevel(UUID uuid) {
        try {
            HypixelAPI.INSTANCE.getPlayerByUuid(uuid).whenComplete((playerReply, throwable) -> Hypixelapiforge.playerApiList.get(uuid).setLevel((int) playerReply.getPlayer().getNetworkLevel()));
        } catch (Exception e) {
            e.printStackTrace();
            Hypixelapiforge.playerApiList.get(uuid).setLevel(-1);
        }
    }
}