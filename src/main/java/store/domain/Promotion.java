package store.domain;

public class Promotion {
    private final String name;
    private final int buy;
    private final int free;

    public Promotion(String name, int buy, int free) {
        this.name = name;
        this.buy = buy;
        this.free = free;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getFree() {
        return free;
    }
}
