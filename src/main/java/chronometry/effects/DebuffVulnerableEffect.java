package chronometry.effects;

import chronometry.MoveEffect;

public class DebuffVulnerableEffect extends MoveEffect {
    public DebuffVulnerableEffect(int turns) {
        this.number = turns;
    }
}
