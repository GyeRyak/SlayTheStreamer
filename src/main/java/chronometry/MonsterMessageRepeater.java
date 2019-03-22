package chronometry;

import chronometry.patches.AbstractMonsterPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.actions.animations.*;
import de.robojumper.ststwitch.TwitchConnection;
import de.robojumper.ststwitch.TwitchPanel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class MonsterMessageRepeater {

	static int MsgLength = 64;

	public static void remindActions(AbstractMonster monster) {
		if (monster.name.length() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("@");
			sb.append(monster.name.split(" ")[0]);
			sb.append(" ");
			sb.append(SlayTheStreamer.localizedChatEffects.get("TwitchNotification"));
			sb.append(": ");
			ArrayList<IntentData> moves = IntentData.getAvailableMoves(monster);
			if (moves.size() == 1) {
				commandMonster(monster, moves.get(0));
				return;
			}
			for (int num = 0; num < moves.size(); num++) {
				sb.append(moves.get(num).toString(num));
			}

			try {
				Method m = TwitchPanel.class.getDeclaredMethod("getTwitch");
				m.setAccessible(true); //if security settings allow this
				TwitchConnection conn = (TwitchConnection)m.invoke(null); //use null if the method is static
				conn.sendMessage(sb.toString());
			}
			catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exc) { }
		}
	}

	static void parseMessage(String msg, String user) {
		if (CardCrawlGame.isInARun()) {
			if (AbstractDungeon.getCurrRoom() != null) {
				if (AbstractDungeon.getCurrRoom().monsters != null) {
                    Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
                    AbstractMonster m;
					while (var1.hasNext()) {
					    m = (AbstractMonster)var1.next();
						SlayTheStreamer.logger.info("parseMessage: monster ".concat(m.name).concat(" isDying ")
								.concat(String.valueOf(m.isDying)).concat(" isPlayer ")
								.concat(String.valueOf(AbstractMonsterPatch.is_player.get(m))));
						if (m.isDying) { return; }
						String username = user;
						if (SlayTheStreamer.displayNames.containsKey(username)) {
							username = SlayTheStreamer.displayNames.get(username);
						}
						if (m.name.split(" ")[0].toLowerCase().equals(username.split(" ")[0].toLowerCase())) {
							if (msg.startsWith("#") && !AbstractMonsterPatch.had_turn.get(m)) {
								msg = msg.substring(1).replaceAll("\\s", "_").toLowerCase();
								ArrayList<IntentData> moves = IntentData.getAvailableMoves(m);
								for (int num = 0; num < moves.size(); num++) {
									IntentData data = moves.get(num);
									String move_name = data.move_name.replaceAll("\\s", "_").toLowerCase();
									if (msg.equals(String.valueOf(num)) || msg.equals(move_name)
											|| msg.equals(String.valueOf(num).concat("_").concat(move_name))) {
										commandMonster(m, data);
										break;
									}
								}
							}
							else {
								msg = msg.substring(0, Math.min(msg.length(), MonsterMessageRepeater.MsgLength));
								if (msg.length() == MonsterMessageRepeater.MsgLength) {
									msg = msg.substring(0, msg.lastIndexOf(" ")) + "...";
								}
								AbstractDungeon.actionManager.addToBottom(new TalkAction(m, msg, 1.5F, 2.5F));
							}
						}
					}
				}
			}
		}
	}

	static void commandMonster(AbstractMonster m, IntentData data) {
		m.setMove(data.move_name, data.intent_code, data.intent_type,
				data.getBaseDamage(), data.getMultiplier(), data.isMulti());
		m.createIntent();
		data.setCooldown();
		AbstractMonsterPatch.had_turn.set(m, true);

		try {
			Method after_action_func = AbstractMonsterPatch.after_action_func.get(m);
			if (after_action_func != null) {
				SlayTheStreamer.logger.info("found after action func");
				after_action_func.invoke(null, m, data);
			}
		}
		catch (IllegalAccessException | InvocationTargetException exc) {
			SlayTheStreamer.logger.info("catched error in after action func");
		}
	}

}
