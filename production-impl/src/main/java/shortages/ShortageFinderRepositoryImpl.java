package shortages;

import entities.DemandEntity;
import entities.ProductionEntity;
import external.CurrentStock;

import java.time.LocalDate;
import java.util.List;
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

        ProductionPlan outputs = new ProductionPlan(productions);
        Demands demandsPerDay = new Demands(demands);

        // TODO ASK including locked or only proper parts
        // TODO ASK current stock or on day start? what if we are in the middle of production a day?
        long level = stock.getLevel();


        return new ShortageCalculator(dates, outputs, demandsPerDay, level);
    }
}
