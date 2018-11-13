package infrastructure;

import entities.ProductionEntity;
import shortages.ShortagesFacade;
import shortages.monitoring.ShortageService;

import java.util.List;
import java.util.stream.Collectors;

public class ShortageServiceACL {

    private ShortageService oldService;
    private ShortagesFacade newService;

    public void processShortagesFromPlannerService(List<ProductionEntity> products) {
        oldService.processShortagesFromPlannerService(products);

        if (false) {
            products.stream()
                    .map(entity -> entity.getForm().getRefNo())
                    .collect(Collectors.toList())
                    .forEach(refNo -> newService.afterProductionPlanChanged(refNo));
        }
    }

    public void processShortagesFromLogisticService(String productRefNo) {
        oldService.processShortagesFromLogisticService(productRefNo);

        if (false) {
            newService.afterDemandChanged(productRefNo);
        }
    }

    public void processShortagesFromQualityService(String productRefNo) {
        oldService.processShortagesFromQualityService(productRefNo);

        if (false) {
            newService.afterQualityLocks(productRefNo);
        }
    }

    public void processShortagesFromWarehouseService(String productRefNo) {
        oldService.processShortagesFromWarehouseService(productRefNo);

        if (false) {
            newService.afterStockChanges(productRefNo);
        }
    }

}
