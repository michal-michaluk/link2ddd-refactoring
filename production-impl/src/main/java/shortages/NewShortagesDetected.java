package shortages;

public class NewShortagesDetected {
    private final Shortages shortages;
    private final AfterEvent source;

    public Shortages getShortages() {
        return shortages;
    }

    public enum AfterEvent {
        PlanChanged, QualityLocks, StockChanges, DemandChanged
    }

    public NewShortagesDetected(Shortages shortages, AfterEvent source) {
        this.shortages = shortages;
        this.source = source;
    }
}
