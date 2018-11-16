package demands;

import enums.DeliverySchema;

import java.time.LocalDate;

public class DemandChanged {
    private final DemandId id;
    private final long level;
    private final DeliverySchema deliverySchema;

    public DemandChanged(DemandId id, long level, DeliverySchema deliverySchema) {
        this.id = id;
        this.level = level;
        this.deliverySchema = deliverySchema;
    }

    public String getRefNo() {
        return id.getProductRefNo();
    }

    public long getLevel() {
        return level;
    }

    public DeliverySchema getDeliverySchema() {
        return deliverySchema;
    }

    public LocalDate getDate() {
        return id.getDate();
    }
}
