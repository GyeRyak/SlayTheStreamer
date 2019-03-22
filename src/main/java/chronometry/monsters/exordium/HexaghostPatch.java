package chronometry.monsters.exordium;

import basemod.ReflectionHacks;
import chronometry.IntentData;
import chronometry.effects.AttackEffect;
import chronometry.effects.BuffStrengthEffect;
import chronometry.effects.DebuffCardDiscardEffect;
import chronometry.effects.DefendEffect;
import chronometry.patches.AbstractMonsterPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;

import java.util.ArrayList;

public class HexaghostPatch {
    public static byte PREPARING = 0;
    public static byte INFERNO_0 = 1;
    public static byte FIGHT = 2;
    public static byte INFERNO_1 = 3;

    @SpirePatch(clz=Hexaghost.class,
                method=SpirePatch.CONSTRUCTOR,
                paramtypez = {})
    public static class InitMoves {
        public static void Postfix(Hexaghost __instance) {
            ArrayList<IntentData> moves = new ArrayList<IntentData>();

            IntentData move2 = new IntentData(
                    __instance.getClass(),
                    "TACKLE",
                    Intent.ATTACK
            );
            move2.add_phase(FIGHT);
            move2.add_effect(new AttackEffect(__instance.damage.get(0), 2));
            moves.add(move2);

            IntentData move3 = new IntentData(
                    __instance.getClass(),
                    "INFLAME",
                    Intent.BUFF,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "STRENGTHEN_NAME"),
                    2
            );
            move3.add_phase(FIGHT);
            move3.add_effect(new DefendEffect(
                    (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "strengthenBlockAmt")
            ));
            move3.add_effect(new BuffStrengthEffect(
                    (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "strAmount")
            ));
            moves.add(move3);

            IntentData move4 = new IntentData(
                    __instance.getClass(),
                    "SEAR",
                    Intent.STRONG_DEBUFF,
                    (String) ReflectionHacks.getPrivateStatic(__instance.getClass(), "SEAR_NAME")
            );
            move4.add_phase(FIGHT);
            move4.add_effect(new AttackEffect(__instance.damage.get(1)));
            move4.add_effect(new DebuffCardDiscardEffect(
                    Burn.NAME,
                    (int)ReflectionHacks.getPrivate(__instance, __instance.getClass(), "searBurnCount")
            ));
            moves.add(move4);

            IntentData move5 = new IntentData(
                    __instance.getClass(),
                    "ACTIVATE",
                    Intent.UNKNOWN
            );
            move5.add_phase(PREPARING);
            moves.add(move5);

            AbstractMonsterPatch.intent_moves.set(__instance, moves);
            try {
                AbstractMonsterPatch.turn_start_func.set(__instance,
                        HexaghostPatch.class.getMethod("turnStart", Hexaghost.class));
            }
            catch (NoSuchMethodException exc) { }
            try {
                AbstractMonsterPatch.after_action_func.set(__instance,
                        HexaghostPatch.class.getMethod("afterAction", Hexaghost.class, IntentData.class));
            }
            catch (NoSuchMethodException exc) { }
        }
    }

    public static void turnStart(Hexaghost monster) {
        int orbActiveCount = (int)ReflectionHacks.getPrivate(monster, monster.getClass(), "orbActiveCount");
        byte current_phase = AbstractMonsterPatch.current_phase.get(monster);
        if (current_phase == INFERNO_0) {
            if (orbActiveCount == 0) {
                AbstractMonsterPatch.current_phase.set(monster, FIGHT);
            }
        }
        else if (current_phase != PREPARING) {
            if (orbActiveCount < 6) {
                AbstractMonsterPatch.current_phase.set(monster, FIGHT);
            }
            else {
                AbstractMonsterPatch.current_phase.set(monster, INFERNO_1);
            }
        }
    }

    public static void afterAction(Hexaghost monster, IntentData intent) {
        int orbActiveCount = (int)ReflectionHacks.getPrivate(monster, monster.getClass(), "orbActiveCount");
        if (orbActiveCount == 0 && intent.equals(monster.getClass(), "ACTIVATE")) {
            AbstractMonsterPatch.current_phase.set(monster, INFERNO_0);
        }
    }

    // 0 (prep):    turnStart, orb = 0
    //              action => intent = "ACTIVATE"
    //              afterAction, orb = 0 => phase = inf0
    // 1 (inf0):    turnStart, orb = 6
    //              action => ...
    // 2 (inf0):    turnStart, orb = 0 => phase = figh
    //              action =>
    // ...
    // 8 (figh):    turnStart, orb = 6 => phase = inf1
    //              action =>
    // 9 (inf1):    turnStart, orb = 0 => phase = figh
}
