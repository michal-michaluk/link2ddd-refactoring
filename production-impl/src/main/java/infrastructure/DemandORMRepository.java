package infrastructure;

import dao.DemandDao;
import demands.*;
import entities.DemandEntity;

import java.time.Clock;

class DemandORMRepository implements DemandRepository {

    private DemandDao dao;
    private DemandEvents events;
    private Clock clock;
    private DefaultDeliverySchemaPolicy policy = new DefaultDeliverySchemaPolicy();

    public DemandORMRepository(DemandDao dao) {
        this.dao = dao;
    }

    @Override
    public DemandObject get(DemandId id) {
        DemandEntity demand = dao.getCurrent(id.getProductRefNo(), id.getDate());
        return new DemandObject(id, demand, policy, new PersistingDecorator(), clock);
    }

    private class PersistingDecorator implements DemandEvents {

        @Override
        public void emit(DemandChanged event) {
            events.emit(event);
        }
    }
}
