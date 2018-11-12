package shortages;

import entities.ShortageEntity;
import enums.DeliverySchema;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

class ShortageCalculator {
    private List<LocalDate> dates;
    private ProductionPlan outputs;
    private Demands demandsPerDay;
    private long level;

    public ShortageCalculator(List<LocalDate> dates, ProductionPlan outputs, Demands demandsPerDay, long level) {
        this.dates = dates;
        this.outputs = outputs;
        this.demandsPerDay = demandsPerDay;
        this.level = level;
    }

    public List<ShortageEntity> findShortages() {
        List<ShortageEntity> gap = new LinkedList<>();
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
                entity.setRefNo(outputs.getProductRefNo());
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
