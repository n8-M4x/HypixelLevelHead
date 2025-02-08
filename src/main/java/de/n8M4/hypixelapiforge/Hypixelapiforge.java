package de.n8M4.hypixelapiforge;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.apache.ApacheHttpClient;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        MinecraftForge.EVENT_BUS.register(ClientCommandRegistration.class);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE) // Ensure it runs in the Forge event bus
    public class ClientCommandRegistration {

        @SubscribeEvent
        public static void registerClientCommands(RegisterClientCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

            dispatcher.register(
                    LiteralArgumentBuilder.<CommandSourceStack>literal("setapikey") // The command name
                            .then(com.mojang.brigadier.builder.RequiredArgumentBuilder.<CommandSourceStack, String>argument("key", StringArgumentType.string())
                                    .executes(context -> {
                                        String msg = StringArgumentType.getString(context, "key");
                                            try {
                                                LevelHead.API_KEY = UUID.fromString(msg).toString();
                                                HypixelAPI.INSTANCE = new HypixelAPI(new ApacheHttpClient(UUID.fromString(LevelHead.API_KEY)));
                                                Minecraft.getInstance().player.displayClientMessage(Component.literal("Api Key set"), false);
                                                Hypixelapiforge.playerApiList.forEach((uuid, playerstats) -> playerstats.setRequested(false));

                                            } catch (Exception e) {
                                                Minecraft.getInstance().player.displayClientMessage(Component.literal("No valid Api Key"), false);
                                            }
                                        return Command.SINGLE_SUCCESS;
                                    })
                            )
            );
        }
    }
}