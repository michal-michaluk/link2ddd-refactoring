package infrastructure;

import enums.DeliverySchema;

import java.time.LocalDate;

public class DemandReadModel {
    private Long level;
    private DeliverySchema deliverySchema;
    private LocalDate date;

    public DemandReadModel(Long level, DeliverySchema deliverySchema, LocalDate date) {
        this.level = level;
        this.deliverySchema = deliverySchema;
        this.date = date;
    }

    public LocalDate getDay() {
        return date;
    }

    DeliverySchema getDeliverySchema() {
        return deliverySchema;
    }

    Long getLevel() {
        return level;
    }
}
