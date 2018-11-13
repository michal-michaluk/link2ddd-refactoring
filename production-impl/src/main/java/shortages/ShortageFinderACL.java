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
        List<ShortageEntity> oldModel = ShortageFinder.findShortages(today, daysAhead, stock, productions, demands);

        if (RefactoringFeature.RUN_NEW_SHORTAGE_FINDER.isActive()) {
            ShortageFinderRepositoryImpl repository = new ShortageFinderRepositoryImpl(today, daysAhead, stock, productions, demands);
            ShortageCalculator shortageFinder = repository.get();
            List<ShortageEntity> newModel = shortageFinder.findShortages();

            // compare and log oldModel, newModel
            if (RefactoringFeature.RETURN_NEW_SHORTAGE_FINDER.isActive()) {
                // save new model entities
                // trigger side effects
                return newModel;
            }
        }
        return oldModel;
    }

    private ShortageFinderACL() {
    }

}
