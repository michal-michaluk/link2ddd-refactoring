package shortages.calculation;

public interface ShortageCalculatorProvider {
    ShortageCalculator get(String productRefNo, int daysAhead);
}
