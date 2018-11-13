package shortages;

import java.time.LocalDate;
import java.util.Map;

public class ProductionOutputs {

    private final Map<LocalDate, Long> outputs;
    private final String productRefNo;

    public ProductionOutputs(Map<LocalDate, Long> outputs, String productRefNo) {
        this.outputs = outputs;
        this.productRefNo = productRefNo;
    }

    public long getOutput(LocalDate day) {
        return outputs.getOrDefault(day, 0L);
    }

    public String getProductRefNo() {
        return productRefNo;
    }
}
