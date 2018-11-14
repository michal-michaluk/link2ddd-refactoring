package infrastructure;

import dao.DemandDao;
import dao.ProductionDao;
import entities.ProductionEntity;
import enums.DeliverySchema;
import external.StockService;
import shortages.calculation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Clock;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ShortageCalculatorMonolithProvider implements ShortageCalculatorProvider {

    private ProductionDao productionDao;
    private StockService stockService;
    private DemandDao demandDao;
    private Clock clock;
    private DemandReadModelDao demandReadModel;

    private final Map<DeliverySchema, LevelOnDeliveryCalculation> mapping = initMapping();

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

        Demands demandsPerDay = new Demands(demandReadModel.findFrom(today.atStartOfDay(), productRefNo).stream()
                .collect(Collectors.toMap(
                        DemandReadModel::getDay,
                        entity -> new Demands.DailyDemand(
                                entity.getLevel(), pick(entity.getDeliverySchema())
                        )
                )));
        long level = stockService.getCurrentStock(productRefNo).getLevel();

        return new ShortageCalculator(productRefNo, dates, outputs, demandsPerDay, level);
    }


    private LevelOnDeliveryCalculation pick(DeliverySchema deliverySchema) {
        return Optional.ofNullable(mapping.get(deliverySchema))
                .orElseThrow(NotImplementedException::new);
    }

    private Map<DeliverySchema, LevelOnDeliveryCalculation> initMapping() {
        Map<DeliverySchema, LevelOnDeliveryCalculation> mapping = new HashMap<>();
        mapping.put(DeliverySchema.atDayStart,
                LevelOnDeliveryCalculation.atDayStart);
        mapping.put(DeliverySchema.tillEndOfDay,
                LevelOnDeliveryCalculation.tillEndOfDay);
        mapping.put(DeliverySchema.every3hours, (level1, demand, produced) -> {
            throw new NotImplementedException();
        });
        return Collections.unmodifiableMap(mapping);
    }
}
