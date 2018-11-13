package infrastructure;

import dao.ShortageDao;
import entities.ShortageEntity;
import shortages.NewShortagesDetected;
import shortages.ShortageEvents;
import shortages.ShortagesResolved;

import java.util.List;

public class ShortageEventsPropagation implements ShortageEvents {

    private ShortageDao shortageDao;
    private ShortageAdapter shortageAdapter;

    @Override
    public void emit(NewShortagesDetected event) {
        List<ShortageEntity> shortages = shortageAdapter.toEntities(event.getShortages());
        // notify planer
        // qa task priority
        // save shortage tables
        shortageDao.save(shortages);
    }

    @Override
    public void emit(ShortagesResolved event) {
        // delete from shortage tables
        shortageDao.delete(event.getProductRefNo());
    }
}
