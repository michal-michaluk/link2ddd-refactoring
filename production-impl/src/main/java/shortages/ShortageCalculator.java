package shortages;

import entities.ShortageEntity;
import enums.DeliverySchema;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class ShortageCalculator {

    private final String productRefNo;
    private final List<LocalDate> dates;
    private final ProductionOutputs outputs;
    private final Demands demandsPerDay;
    private final long stockLevel;

    public ShortageCalculator(String productRefNo, List<LocalDate> dates, ProductionOutputs outputs, Demands demandsPerDay, long level) {
        this.productRefNo = productRefNo;
        this.dates = dates;
        this.outputs = outputs;
        this.demandsPerDay = demandsPerDay;
        this.stockLevel = level;
    }

    public List<ShortageEntity> findShortages() {
        List<ShortageEntity> gap = new LinkedList<>();
        long level = stockLevel;
        for (LocalDate day : dates) {
            Demands.DailyDemand demand = demandsPerDay.get(day);
            if (demand == null) {
                level += outputs.getOutput(day);
                continue;
            }
            long produced = outputs.getOutput(day);

            long levelOnDelivery;
            if (demand.hasDeliverySchema(DeliverySchema.atDayStart)) {
                levelOnDelivery = level - demand.getLevel();
            } else if (demand.hasDeliverySchema(DeliverySchema.tillEndOfDay)) {
                levelOnDelivery = level - demand.getLevel() + produced;
            } else if (demand.hasDeliverySchema(DeliverySchema.every3hours)) {
                // TODO WTF ?? we need to rewrite that app :/
                throw new NotImplementedException();
            } else {
                // TODO implement other variants
                throw new NotImplementedException();
            }

            if (!(levelOnDelivery >= 0)) {
                ShortageEntity entity = new ShortageEntity();
                entity.setRefNo(productRefNo);
                entity.setFound(LocalDate.now());
                entity.setAtDay(day);
                gap.add(entity);
            }
            long endOfDayLevel = level + produced - demand.getLevel();
            // TODO: ASK accumulated shortages or reset when under zero?
            level = endOfDayLevel >= 0 ? endOfDayLevel : 0;
        }
        return gap;
    }
}
