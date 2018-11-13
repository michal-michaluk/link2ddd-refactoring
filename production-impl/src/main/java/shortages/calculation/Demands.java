package shortages.calculation;

import com.sun.istack.internal.Nullable;
import enums.DeliverySchema;

import java.time.LocalDate;
import java.util.Map;

public class Demands {

    private final Map<LocalDate, DailyDemand> demands;

    public Demands(Map<LocalDate, DailyDemand> demands) {
        this.demands = demands;
    }

    @Nullable
    public DailyDemand get(LocalDate day) {
        // TODO introduce null object pattern
        return demands.getOrDefault(day, null);
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
