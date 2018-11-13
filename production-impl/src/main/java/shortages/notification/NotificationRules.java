package shortages.notification;

import shortages.NewShortagesDetected;
import shortages.Shortages;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NotificationRules {

    private NotificationsPort service;
    private Map<NewShortagesDetected.AfterEvent, Consumer<Shortages>> map = init();

    private Map<NewShortagesDetected.AfterEvent, Consumer<Shortages>> init() {
        Map<NewShortagesDetected.AfterEvent, Consumer<Shortages>> map = new HashMap<>();
        map.put(NewShortagesDetected.AfterEvent.DemandChanged, service::alertPlanner);
        map.put(NewShortagesDetected.AfterEvent.PlanChanged, service::markOnPlan);
        map.put(NewShortagesDetected.AfterEvent.QualityLocks, service::softNotifyPlanner);
        map.put(NewShortagesDetected.AfterEvent.StockChanges, service::alertPlanner);
        return map;
    }

    public void notifyPlanner(NewShortagesDetected.AfterEvent source, Shortages shortages) {
        map.get(source).accept(shortages);
    }
}
