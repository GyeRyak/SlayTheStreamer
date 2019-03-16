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
			try {
				Method phase_define_func = AbstractMonsterPatch.define_phase_func.get(monster);
				if (phase_define_func != null) {
					SlayTheStreamer.logger.info("found phase define");
					phase_define_func.invoke(null, monster);
				}
			}
			catch (IllegalAccessException | InvocationTargetException exc) {
				SlayTheStreamer.logger.info("catched error in phase define");
			}
			ArrayList<IntentData> moves = IntentData.getAvailableMoves(monster);
			if (moves.size() == 1) {
				commandMonster(monster, moves.get(0));
				return;
			}
			for (IntentData data: moves) {
				sb.append(data.toString());
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
					for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
						if (m.isDying) { return; }
						String username = user;
						if (SlayTheStreamer.displayNames.containsKey(username)) {
							username = SlayTheStreamer.displayNames.get(username);
						}
						if (m.name.split(" ")[0].toLowerCase().equals(username.split(" ")[0].toLowerCase())) {
							if (msg.startsWith("#") && !AbstractMonsterPatch.had_turn.get(m)) {
								msg = msg.substring(1);
								ArrayList<IntentData> moves = IntentData.getAvailableMoves(m);
								for (IntentData data: moves) {
									if (data.move_name.equals(msg)) {
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
	}

}
