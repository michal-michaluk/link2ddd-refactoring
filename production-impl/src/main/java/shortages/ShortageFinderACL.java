package shortages;

import entities.DemandEntity;
import entities.ProductionEntity;
import entities.ShortageEntity;
import external.CurrentStock;
import tools.ShortageFinder;

import java.time.LocalDate;
import java.util.List;

public class ShortageFinderACL {

    public static List<ShortageEntity> findShortages(LocalDate today, int daysAhead, CurrentStock stock,
                                                     List<ProductionEntity> productions, List<DemandEntity> demands) {
        // togglez
        if (true) {
            return ShortageFinder.findShortages(today, daysAhead, stock, productions, demands);
        } else {
            ShortageFinderRepositoryImpl repository = new ShortageFinderRepositoryImpl(today, daysAhead, stock, productions, demands);
            ShortageCalculator shortageFinder = repository.get();
            return shortageFinder.findShortages();
        }
    }

    private ShortageFinderACL() {
    }

}
