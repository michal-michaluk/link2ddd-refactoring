package shortages;

import entities.ProductionEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionOutputs {

    private String productRefNo;

    private final Map<LocalDate, List<ProductionEntity>> outputs;

    public ProductionOutputs(List<ProductionEntity> productions) {
        outputs = new HashMap<>();
        for (ProductionEntity production : productions) {
            outputs.computeIfAbsent(production.getStart().toLocalDate(), key -> new ArrayList<>())
                    .add(production);

            productRefNo = production.getForm().getRefNo();
        }
    }

    public long getOutput(LocalDate day) {
        return outputs.get(day)
                .stream()
                .mapToLong(ProductionEntity::getOutput)
                .sum();
    }

    public String getProductRefNo() {
        return productRefNo;
    }
}
