package shortages.monitoring;

import shortages.NewShortagesDetected;
import shortages.NewShortagesDetected.AfterEvent;
import shortages.ShortageEvents;
import shortages.Shortages;
import shortages.ShortagesResolved;
import shortages.calculation.ShortageCalculator;
import shortages.calculation.ShortageCalculatorProvider;

public class ShortageMonitoringProcess {

    private Shortages currentlyKnown;

    private ShortageCalculatorProvider calculatorProvider;
    private ShortageEvents events;

    private int predictionDaysAhead;

    public ShortageMonitoringProcess(Shortages currentlyKnown, ShortageCalculatorProvider calculatorProvider, int predictionDaysAhead, ShortageEvents events) {
        this.currentlyKnown = currentlyKnown;
        this.calculatorProvider = calculatorProvider;
        this.predictionDaysAhead = predictionDaysAhead;
        this.events = events;
    }

    // target:
    // get rid of redundant code
    // limit dependencies

    // way of refactor:
    // refactor copy of service
    // reconciliation of both implementations

    // refactoring steps:
    // ShortageService: check state is different after shortage calculation
    // Notifications: notify according to rules
    // NotificationsRules: Notification rules 3 variants: alertPlanner markOnPlan softNotifyPlanner
    // Object3: Should Increase Task Priority Policy

    // Introduce Hexagon from ShortageEntity
    // - ShortageMonitoringProcessRepository -> Shortage value object
    // Shortage Diff Policy

    public void afterDemandChanged(String productRefNo) {
        processShortages(productRefNo, AfterEvent.DemandChanged);
    }

    public void afterProductionPlanChanged(String productRefNo) {
        processShortages(productRefNo, AfterEvent.PlanChanged);
    }

    public void afterQualityLocks(String productRefNo) {
        processShortages(productRefNo, AfterEvent.QualityLocks);
    }

    public void afterStockChanges(String productRefNo) {
        processShortages(productRefNo, AfterEvent.StockChanges);
    }

    private void processShortages(String productRefNo, AfterEvent source) {
        ShortageCalculator calculator = calculatorProvider.get(productRefNo, predictionDaysAhead);
        Shortages justCalculated = calculator.findShortages();

        if (!justCalculated.isEmpty() && !justCalculated.isDifferentThan(currentlyKnown)) {
            currentlyKnown = justCalculated;
            events.emit(new NewShortagesDetected(justCalculated, source));
        } else if (justCalculated.isEmpty() && !currentlyKnown.isEmpty()) {
            currentlyKnown = Shortages.empty(productRefNo);
            events.emit(new ShortagesResolved(productRefNo));
        }
    }
}
