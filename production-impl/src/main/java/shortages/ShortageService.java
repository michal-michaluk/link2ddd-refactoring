package shortages;

import dao.ShortageDao;
import entities.ProductionEntity;
import entities.ShortageEntity;
import external.CurrentStock;
import external.JiraService;
import external.NotificationsService;
import external.StockService;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

public class ShortageService {

    private ShortageDao shortageDao;
    private StockService stockService;
    private ShortageCalculatorProvider repository;

    private NotificationsService notificationService;
    private JiraService jiraService;
    private Clock clock;

    private int confShortagePredictionDaysAhead;
    private long confIncreaseQATaskPriorityInDays;

    public void processShortagesFromPlannerService(List<ProductionEntity> products) {
        LocalDate today = LocalDate.now(clock);

        for (ProductionEntity production : products) {
            CurrentStock currentStock = stockService.getCurrentStock(production.getForm().getRefNo());
            ShortageCalculator shortageFinder = repository.get(production.getForm().getRefNo(), confShortagePredictionDaysAhead);
            List<ShortageEntity> shortages = shortageFinder.findShortages();
            List<ShortageEntity> previous = shortageDao.getForProduct(production.getForm().getRefNo());
            if (!shortages.isEmpty() && !shortages.equals(previous)) {
                notificationService.markOnPlan(shortages);
                if (currentStock.getLocked() > 0 &&
                        shortages.get(0).getAtDay()
                                .isBefore(today.plusDays(confIncreaseQATaskPriorityInDays))) {
                    jiraService.increasePriorityFor(production.getForm().getRefNo());
                }
                shortageDao.save(shortages);
            }
            if (shortages.isEmpty() && !previous.isEmpty()) {
                shortageDao.delete(production.getForm().getRefNo());
            }
        }
    }

    public void processShortagesFromLogisticService(String productRefNo) {
        LocalDate today = LocalDate.now(clock);
        CurrentStock stock = stockService.getCurrentStock(productRefNo);
        ShortageCalculator shortageFinder = repository.get(productRefNo, confShortagePredictionDaysAhead);
        List<ShortageEntity> shortages = shortageFinder.findShortages();

        List<ShortageEntity> previous = shortageDao.getForProduct(productRefNo);
        if (!shortages.isEmpty() && !shortages.equals(previous)) {
            notificationService.alertPlanner(shortages);
            if (stock.getLocked() > 0 &&
                    shortages.get(0).getAtDay()
                            .isBefore(today.plusDays(confIncreaseQATaskPriorityInDays))) {
                jiraService.increasePriorityFor(productRefNo);
            }
            shortageDao.save(shortages);
        }
        if (shortages.isEmpty() && !previous.isEmpty()) {
            shortageDao.delete(productRefNo);
        }
    }

    public void processShortagesFromQualityService(String productRefNo) {
        LocalDate today = LocalDate.now(clock);
        CurrentStock currentStock = stockService.getCurrentStock(productRefNo);
        ShortageCalculator shortageFinder = repository.get(productRefNo, confShortagePredictionDaysAhead);
        List<ShortageEntity> shortages = shortageFinder.findShortages();

        List<ShortageEntity> previous = shortageDao.getForProduct(productRefNo);
        if (!shortages.isEmpty() && !shortages.equals(previous)) {
            notificationService.softNotifyPlanner(shortages);
            if (currentStock.getLocked() > 0 &&
                    shortages.get(0).getAtDay()
                            .isBefore(today.plusDays(confIncreaseQATaskPriorityInDays))) {
                jiraService.increasePriorityFor(productRefNo);
            }
        }
        if (shortages.isEmpty() && !previous.isEmpty()) {
            shortageDao.delete(productRefNo);
        }
    }

    public void processShortagesFromWarehouseService(String productRefNo) {
        LocalDate today = LocalDate.now(clock);
        CurrentStock currentStock = stockService.getCurrentStock(productRefNo);
        ShortageCalculator shortageFinder = repository.get(productRefNo, confShortagePredictionDaysAhead);
        List<ShortageEntity> shortages = shortageFinder.findShortages();

        List<ShortageEntity> previous = shortageDao.getForProduct(productRefNo);
        if (shortages != null && !shortages.equals(previous)) {
            notificationService.alertPlanner(shortages);
            if (currentStock.getLocked() > 0 &&
                    shortages.get(0).getAtDay()
                            .isBefore(today.plusDays(confIncreaseQATaskPriorityInDays))) {
                jiraService.increasePriorityFor(productRefNo);
            }
        }
        if (shortages.isEmpty() && !previous.isEmpty()) {
            shortageDao.delete(productRefNo);
        }
    }
}
