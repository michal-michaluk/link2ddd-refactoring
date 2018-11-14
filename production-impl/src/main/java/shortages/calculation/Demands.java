package shortages.calculation;

import com.sun.istack.internal.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public DailyDemand get(LocalDateTime deliveryTime) {
        return null;
    }

    public static class DailyDemand {
        private final LevelOnDeliveryCalculation policy;
        private final long level;

        public DailyDemand(long level, LevelOnDeliveryCalculation policy) {
            this.policy = policy;
            this.level = level;
        }

        public long getLevel() {
            return level;
        }

        public long calculateLevelOnDelivery(long level, long produced) {
            return policy.calculateLevelOnDelivery(level, this, produced);
        }
    }
}
