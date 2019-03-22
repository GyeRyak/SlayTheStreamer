package chronometry;

public abstract class MoveEffect {
    public String effect_string;
    public int number = 0;
    public int multiplier = 0;

    public int number() {
        return this.number;
    }

    public int multiplier() {
        return this.multiplier;
    }

    @Override
    public String toString() {
        String sb = SlayTheStreamer.localizedChatEffects.get(this.getClass().getSimpleName());
        sb = sb.replaceAll("\\{num\\}", String.valueOf(this.number()))
                .replaceAll("\\{effect\\}", this.effect_string);
        String mul = "";
        if (this.multiplier() > 1) {
            mul = "x".concat(String.valueOf(this.multiplier()));
        }
        sb = sb.replaceAll("\\{mul\\}", mul);
        return sb;
    }
}
