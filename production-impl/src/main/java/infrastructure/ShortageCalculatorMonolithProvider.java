package infrastructure;

import dao.DemandDao;
import dao.ProductionDao;
import entities.DemandEntity;
import entities.ProductionEntity;
import external.StockService;
import shortages.Demands;
import shortages.ProductionOutputs;
import shortages.ShortageCalculator;
import shortages.ShortageCalculatorProvider;
import tools.Util;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ShortageCalculatorMonolithProvider implements ShortageCalculatorProvider {

    private ProductionDao productionDao;
    private StockService stockService;
    private DemandDao demandDao;
    private Clock clock;

    @Override
    public ShortageCalculator get(String productRefNo, int daysAhead) {
        LocalDate today = LocalDate.now(clock);
        List<LocalDate> dates = Stream.iterate(today, date -> date.plusDays(1))
                .limit(daysAhead)
                .collect(toList());

        ProductionOutputs outputs = new ProductionOutputs(
                productionDao.findFromTime(productRefNo, today.atStartOfDay()).stream()
                        .collect(Collectors.toMap(
                                entity -> entity.getStart().toLocalDate(),
                                ProductionEntity::getOutput,
                                (first, second) -> first + second
                        ))
        );

        Demands demandsPerDay = new Demands(demandDao.findFrom(today.atStartOfDay(), productRefNo).stream()
                .collect(Collectors.toMap(
                        DemandEntity::getDay,
                        entity -> new Demands.DailyDemand(
                                Util.getDeliverySchema(entity),
                                Util.getLevel(entity)
                        )
                )));
        long level = stockService.getCurrentStock(productRefNo).getLevel();

        return new ShortageCalculator(productRefNo, dates, outputs, demandsPerDay, level);
    }
}
