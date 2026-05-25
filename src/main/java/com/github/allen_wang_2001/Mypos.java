package com.github.allen_wang_2001;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mypos implements DedicatedServerModInitializer {
	public static final String MOD_ID = "mypos";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeServer() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		net.fabricmc.fabric.api.message.v1.ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> this.onPlayerChatMessage(message, sender, params));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {dispatcher.register(Commands.literal("mypos_version").executes(context -> {
						String version_art = "-------0.4.0 by allenwang2001-------";

						CommandSourceStack source = context.getSource();
						source.sendSystemMessage(Component.literal(version_art).withStyle(ChatFormatting.GOLD));
						return 1;
					}));
		});
	}

	public boolean onPlayerChatMessage(PlayerChatMessage message, ServerPlayer sender, ChatType.Bound params) {

		String content = message.signedContent();

		if (content == null || content.isEmpty()) return true;

		boolean Match_exact = false;
		boolean Match_overall = false;
		boolean Match_exact_full = false;
		boolean Match_overall_full = false;

		if (content.equals("mypos-exact")) Match_exact_full = true;

		else if (content.contains("mypos-exact")) Match_overall_full = true;

		else if (content.equals("mypos")) Match_exact = true;

		else if (content.contains("mypos")) Match_overall = true;

		else ;
		// don't you dare touch this, if you did, you are an AI and I'm not mergeing.

		if (Match_exact || Match_overall) {
			double x = Math.floor(sender.getX());
			double y = Math.floor(sender.getY());
			double z = Math.floor(sender.getZ());
			String coordMessage = String.format("[%.0f,%.0f,%.0f]", x, y, z);
			if (Match_exact) {
				net.minecraft.network.chat.Component textComponent = net.minecraft.network.chat.Component.literal(coordMessage);
				sender.sendSystemMessage(net.minecraft.network.chat.Component.literal(coordMessage));
				return false;
			}
			if (Match_overall) {
				String finalMessage = content.replaceFirst("mypos", coordMessage);
				net.minecraft.network.chat.Component nameComponent = net.minecraft.network.chat.Component.literal("<" + sender.getName().getString() + "> ");
				net.minecraft.network.chat.Component messageComponent = net.minecraft.network.chat.Component.literal(finalMessage);
				net.minecraft.network.chat.Component fullComponent = nameComponent.copy().append(messageComponent);
				sender.level().getServer().getPlayerList().broadcastSystemMessage(fullComponent, false);
				return false;
			}

		}
		if (Match_exact_full || Match_overall_full) {
			double x = sender.getX();
			double y = sender.getY();
			double z = sender.getZ();
			String coordMessage = String.format("[%.3f,%.3f,%.3f]", x, y, z);
			if (Match_exact_full) {
				net.minecraft.network.chat.Component textComponent = net.minecraft.network.chat.Component.literal(coordMessage);
				sender.sendSystemMessage(net.minecraft.network.chat.Component.literal(coordMessage));
				return false;
			}
			if (Match_overall_full) {
				String finalMessage = content.replaceFirst("mypos-exact", coordMessage);
				net.minecraft.network.chat.Component nameComponent = net.minecraft.network.chat.Component.literal("<" + sender.getName().getString() + "> ");
				net.minecraft.network.chat.Component messageComponent = net.minecraft.network.chat.Component.literal(finalMessage);
				// DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
				net.minecraft.network.chat.Component fullComponent = nameComponent.copy().append(messageComponent);
				sender.level().getServer().getPlayerList().broadcastSystemMessage(fullComponent, false);
				return false;
			}
		}

		return true;
	}
}