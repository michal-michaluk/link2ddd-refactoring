package infrastructure;

import java.time.LocalDateTime;
import java.util.List;

public interface DemandReadModelDao {
    List<DemandReadModel> findFrom(LocalDateTime atStartOfDay, String productRefNo);
}
