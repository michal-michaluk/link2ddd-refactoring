package infrastructure;

import demands.DemandChanged;
import demands.DemandEvents;

public class DemandEventsPropagation implements DemandEvents {

    private ShortageServiceACL shortageService;
    private DemandReadModelDao readModel;

    @Override
    public void emit(DemandChanged event) {
        applyDemandChanged(event);
        shortageService.processShortagesFromLogisticService(event.getRefNo());
    }

    private void applyDemandChanged(DemandChanged event) {
        readModel.save(new DemandReadModel(
                event.getRefNo(),
                event.getDate(),
                event.getLevel(),
                event.getDeliverySchema()
        ));
    }
}
