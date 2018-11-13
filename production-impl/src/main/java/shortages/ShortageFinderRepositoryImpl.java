package shortages;

import entities.DemandEntity;
import entities.ProductionEntity;
import external.CurrentStock;
import tools.Util;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class ShortageFinderRepositoryImpl {
    private LocalDate today;
    private int daysAhead;
    private CurrentStock stock;
    private List<ProductionEntity> productions;
    private List<DemandEntity> demands;

    public ShortageFinderRepositoryImpl(LocalDate today, int daysAhead, CurrentStock stock, List<ProductionEntity> productions, List<DemandEntity> demands) {
        this.today = today;
        this.daysAhead = daysAhead;
        this.stock = stock;
        this.productions = productions;
        this.demands = demands;
    }

    public ShortageCalculator get() {
        List<LocalDate> dates = Stream.iterate(today, date -> date.plusDays(1))
                .limit(daysAhead)
                .collect(toList());

        ProductionOutputs outputs = new ProductionOutputs(
                productions.stream()
                        .collect(Collectors.toMap(
                                entity -> entity.getStart().toLocalDate(),
                                ProductionEntity::getOutput,
                                (first, second) -> first + second
                        )),
                productions.stream()
                        .findAny()
                        .map(entity -> entity.getForm().getRefNo())
                        .orElse(null)
        );

        Demands demandsPerDay = new Demands(demands.stream()
                .collect(Collectors.toMap(
                        DemandEntity::getDay,
                        entity -> new Demands.DailyDemand(
                                Util.getDeliverySchema(entity),
                                Util.getLevel(entity)
                        )
                )));
        long level = stock.getLevel();

        return new ShortageCalculator(dates, outputs, demandsPerDay, level);
    }
}
