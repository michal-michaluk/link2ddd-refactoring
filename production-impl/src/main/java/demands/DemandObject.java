package demands;

import api.AdjustDemandDto;
import entities.DemandEntity;
import entities.ManualAdjustmentEntity;
import enums.DeliverySchema;

import java.time.Clock;
import java.time.LocalDate;

public class DemandObject {
    private DemandId id;
    private DemandEntity demand;
    private DemandEvents events;
    private DefaultDeliverySchemaPolicy policy;
    private Clock clock;

    public DemandObject(DemandId id, DemandEntity demand, DefaultDeliverySchemaPolicy policy, DemandEvents events, Clock clock) {
        this.id = id;
        this.demand = demand;
        this.policy = policy;
        this.events = events;
        this.clock = clock;
    }

    public boolean adjustDemand(AdjustDemandDto adjustment) {
        if (adjustment.getAtDay().isBefore(LocalDate.now(clock))) {
            return true;
        }

        ManualAdjustmentEntity manualAdjustment = new ManualAdjustmentEntity();
        manualAdjustment.setLevel(adjustment.getLevel());
        manualAdjustment.setNote(adjustment.getNote());
        manualAdjustment.setDeliverySchema(adjustment.getDeliverySchema());

        demand.getAdjustment().add(manualAdjustment);

        events.emit(new DemandChanged(id,
                getLevel(),
                getDeliverySchema()
        ));
        return false;
    }

    private long getLevel() {
        if (demand.getAdjustment().isEmpty()) {
            return demand.getOriginal().getLevel();
        } else {
            return demand.getAdjustment().get(demand.getAdjustment().size() - 1).getLevel();
        }
    }

    private DeliverySchema getDeliverySchema() {
        DeliverySchema deliverySchema;
        if (demand.getAdjustment().isEmpty()) {
            deliverySchema = demand.getOriginal().getDeliverySchema();
        } else {
            deliverySchema = demand.getAdjustment().get(demand.getAdjustment().size() - 1).getDeliverySchema();
        }
        if (deliverySchema == null) {
            return policy.defaultFor(demand.getProductRefNo());
        }
        return deliverySchema;
    }
}
