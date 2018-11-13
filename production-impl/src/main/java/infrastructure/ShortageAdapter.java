package infrastructure;

import entities.ShortageEntity;
import shortages.Shortages;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ShortageAdapter {

    public List<ShortageEntity> toEntities(Shortages shortages) {
        return shortages.stream().map(
                o -> createShortageEntity(
                        shortages.getFound(),
                        shortages.getRefNo(),
                        o.getKey(),
                        o.getValue()
                )
        ).collect(Collectors.toList());
    }

    public Shortages fromEntities(String productRefNo, List<ShortageEntity> shortages) {
        if (shortages.isEmpty()) {
            return Shortages.empty(productRefNo);
        }

        return shortages.stream().reduce(
                Shortages.builder(productRefNo, shortages.get(0).getFound()),
                (builder, entity) -> builder.add(entity.getAtDay(), entity.getMissing()),
                Shortages.Builder::merge
        ).build();
    }

    private ShortageEntity createShortageEntity(LocalDate found, String refNo, LocalDate date, long missing) {
        ShortageEntity entity = new ShortageEntity();
        entity.setFound(found);
        entity.setRefNo(refNo);
        entity.setAtDay(date);
        entity.setMissing(missing);
        return entity;
    }
}
