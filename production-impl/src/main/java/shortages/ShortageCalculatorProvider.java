package shortages;

public interface ShortageCalculatorProvider {
    ShortageCalculator get(String productRefNo, int daysAhead);
}
