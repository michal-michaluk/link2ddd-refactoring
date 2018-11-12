package shortages;

import com.sun.istack.internal.Nullable;
import entities.DemandEntity;
import enums.DeliverySchema;
import tools.Util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class Demands {

    private final HashMap<LocalDate, DemandEntity> demandsPerDay;

    public Demands(List<DemandEntity> demands) {
        demandsPerDay = new HashMap<>();
        for (DemandEntity demand1 : demands) {
            demandsPerDay.put(demand1.getDay(), demand1);
        }
    }

    @Nullable
    public DailyDemand get(LocalDate day) {
        DemandEntity demand = demandsPerDay.get(day);
        if (demand != null) {
            return new DailyDemand(
                    Util.getDeliverySchema(demand),
                    Util.getLevel(demand)
            );
        } else { // TODO introduce null object pattern
            return null;
        }
    }

    public static class DailyDemand {
        private final DeliverySchema deliverySchema;
        private final long level;

        public DailyDemand(DeliverySchema deliverySchema, long level) {
            this.deliverySchema = deliverySchema;
            this.level = level;
        }

        public long getLevel() {
            return level;
        }

        public boolean hasDeliverySchema(DeliverySchema schema) {
            return deliverySchema == schema;
        }
    }
}
