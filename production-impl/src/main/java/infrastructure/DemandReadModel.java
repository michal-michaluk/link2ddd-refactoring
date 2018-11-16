package infrastructure;

import enums.DeliverySchema;

import java.time.LocalDate;

public class DemandReadModel {
    private String refNo;
    private LocalDate date;
    private Long level;
    private DeliverySchema deliverySchema;

    public DemandReadModel(String refNo, LocalDate date, Long level, DeliverySchema deliverySchema) {
        this.refNo = refNo;
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
