package shortages;

import shortages.monitoring.ShortageMonitoringProcess;
import shortages.monitoring.ShortageMonitoringProcessRepository;

public class ShortagesFacade {
    private ShortageMonitoringProcessRepository repository;

    public void afterStockChanges(String productRefNo) {
        ShortageMonitoringProcess ob = repository.get(productRefNo);
        ob.afterStockChanges(productRefNo);
        repository.save(ob);
    }

    public void afterProductionPlanChanged(String refNo) {
        ShortageMonitoringProcess ob = repository.get(refNo);
        ob.afterProductionPlanChanged(refNo);
        repository.save(ob);
    }

    public void afterDemandChanged(String productRefNo) {
        ShortageMonitoringProcess ob = repository.get(productRefNo);
        ob.afterDemandChanged(productRefNo);
        repository.save(ob);
    }

    public void afterQualityLocks(String productRefNo) {
        ShortageMonitoringProcess ob = repository.get(productRefNo);
        ob.afterQualityLocks(productRefNo);
        repository.save(ob);
    }
}
