package shortages.monitoring;

import dao.ShortageDao;
import entities.ShortageEntity;
import infrastructure.ShortageAdapter;
import shortages.ShortageEvents;
import shortages.Shortages;
import shortages.calculation.ShortageCalculatorProvider;

import java.util.List;

public class ShortageMonitoringProcessRepository {

    private ShortageDao shortageDao;
    private ShortageAdapter shortageAdapter;
    private ShortageCalculatorProvider calculatorProvider;
    private int confShortagePredictionDaysAhead;
    private ShortageEvents events;


    public ShortageMonitoringProcess get(String productRefNo) {
        List<ShortageEntity> entities = shortageDao.getForProduct(productRefNo);
        Shortages shortages = shortageAdapter.fromEntities(productRefNo, entities);
        return new ShortageMonitoringProcess(
                shortages,
                calculatorProvider,
                confShortagePredictionDaysAhead,
                events
        );
    }

    public void save(ShortageMonitoringProcess shortages) {

    }
}
