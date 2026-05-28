package com.github.allen_wang_2001;

// the import block
// please ignore this, even i don't like looking at this brick
import net.fabricmc.api.ModInitializer;
import static net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT;
import static net.minecraft.ChatFormatting.GOLD;
import static net.fabricmc.fabric.api.message.v1.ServerMessageEvents.ALLOW_CHAT_MESSAGE;
import static net.minecraft.network.chat.Component.literal;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import java.util.Locale;


public class Mypos implements ModInitializer {
	//system variables
	public static final String MOD_ID = "mypos";
	public String version_art = "-------0.7.0 by allenwang2001-------";
	//init
	@Override
	public void onInitialize() {
		ALLOW_CHAT_MESSAGE.register((message, sender, params)
				-> this.onPlayerChatMessage(message, sender, params));
		EVENT.register((
				dispatcher,
				registryAccess,
				environment)
				->
				{dispatcher.register(Commands.literal("mypos_version")
				.executes(context
				->
				{CommandSourceStack source = context.getSource();
				source.sendSystemMessage(Component.literal(version_art).withStyle(GOLD));
				return 1;
				}));
		});
	}

	//private functions
	private double[] get_coords(ServerPlayer player){
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		return new double[]{x,y,z};
	}
	private String format_string(double[] coords, boolean precise) {
		Locale decimal_format = Locale.US;

		// I for the love of god can't figure out how this works.
		String dec_precision = precise ? "%.3f" : "%.0f";

		String x = String.format(decimal_format, dec_precision, coords[0]);
		String y = String.format(decimal_format, dec_precision, coords[1]);
		String z = String.format(decimal_format, dec_precision, coords[2]);

		return String.format(decimal_format, "[%s,%s,%s]", x, y, z);
	}
	// a bag of flags
	private boolean[] calc_flag(String content) {

		boolean private_std = (content.equals("mypos"));

		boolean contain_std = (content.contains("mypos"));

		boolean private_3dp = (content.equals("mypos-exact"));

		boolean contain_3dp = (content.contains("mypos-exact"));

		return new boolean[]{private_std, private_3dp, contain_std, contain_3dp};
	}
	public boolean onPlayerChatMessage(PlayerChatMessage message,
									   ServerPlayer sender,
									   ChatType.Bound params) {

		String content = message.signedContent();

		// a null check
		if (content == null && content.isEmpty()) {
			return true;
		}

		// defining flags
		boolean[] flags = calc_flag(content);
		boolean private_std = flags[0];
		boolean private_3dp = flags[1];
		boolean contain_std = flags[2];
		boolean contain_3dp = flags[3];

		double[] coords = get_coords(sender);

		String coordMessage = null;

		if (private_std) {
			coordMessage = format_string(coords, false);
			sender.sendSystemMessage(Component.literal(coordMessage));
			return false;
		} else if (private_3dp) {
			coordMessage = format_string(coords, true);
			sender.sendSystemMessage(Component.literal(coordMessage));
			return false;
		} else if (contain_3dp) {
			coordMessage = format_string(coords, true);
			String modifiedContent = content.replaceFirst("mypos-exact", coordMessage);
			Component fullMessage = literal(" < " + sender.getName().getString() + " > ").append(modifiedContent);
			sender.level().getServer().getPlayerList().broadcastSystemMessage(fullMessage, false);
			return false;
		} else if (contain_std) {
			coordMessage = format_string(coords, false);
			String modifiedContent = content.replaceFirst("mypos", coordMessage);
			Component fullMessage = literal(" < " + sender.getName().getString() + " > ").append(modifiedContent);
			sender.level().getServer().getPlayerList().broadcastSystemMessage(fullMessage, false);
			return false;
		}

		return true;
	}
}