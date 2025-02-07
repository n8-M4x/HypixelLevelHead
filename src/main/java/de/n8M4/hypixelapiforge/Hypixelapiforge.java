package de.n8M4.hypixelapiforge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.UUID;

@Mod(Hypixelapiforge.MODID)
public class Hypixelapiforge {
    public static HashMap<UUID, PlayerStats> playerApiList = new HashMap<>();

    public static final String MODID = "hypixelapiforge";

    public Hypixelapiforge(FMLJavaModLoadingContext context) {
        MinecraftForge.EVENT_BUS.register(this);
    }
}