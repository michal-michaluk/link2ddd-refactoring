package shortages.calculation;

import java.time.LocalDate;
import java.util.Map;

public class ProductionOutputs {

    private final Map<LocalDate, Long> outputs;

    public ProductionOutputs(Map<LocalDate, Long> outputs) {
        this.outputs = outputs;
    }

    public long getOutput(LocalDate day) {
        return outputs.getOrDefault(day, 0L);
    }
}
