package shortages;

public interface ShortageEvents {
    void emit(NewShortagesDetected event);

    void emit(ShortagesResolved event);
}
