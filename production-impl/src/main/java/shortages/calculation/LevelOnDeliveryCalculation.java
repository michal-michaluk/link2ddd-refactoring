package shortages.calculation;

public interface LevelOnDeliveryCalculation {

    LevelOnDeliveryCalculation atDayStart = (level, demand, produced) -> level - demand.getLevel();
    LevelOnDeliveryCalculation tillEndOfDay = (level, demand, produced) -> level - demand.getLevel() + produced;

    long calculateLevelOnDelivery(long level, Demands.DailyDemand demand, long produced);
}
