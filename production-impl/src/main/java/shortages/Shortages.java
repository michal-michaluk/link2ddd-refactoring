package shortages;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Shortages {

    private final String productRefNo;
    private final LocalDate now;
    private final Map<LocalDate, Long> shortages;

    public Shortages(String productRefNo, LocalDate now, Map<LocalDate, Long> shortages) {
        this.productRefNo = productRefNo;
        this.now = now;
        this.shortages = shortages;
    }

    public static Builder builder(String productRefNo, LocalDate now) {
        return new Builder(productRefNo, now);
    }

    public static Shortages empty(String productRefNo) {
        return new Shortages(productRefNo, LocalDate.MAX, Collections.emptyMap());
    }

    public boolean isEmpty() {
        return shortages.isEmpty();
    }

    public boolean isDifferentThan(Shortages other) {
        return other != null && !(productRefNo.equals(other.productRefNo)
                && shortages.equals(other.shortages));
    }

    public Stream<Map.Entry<LocalDate, Long>> stream() {
        return shortages.entrySet().stream();
    }

    public LocalDate getFound() {
        return now;
    }

    public String getRefNo() {
        return productRefNo;
    }

    public static class Builder {
        private String productRefNo;
        private LocalDate now;
        private Map<LocalDate, Long> shortages = new HashMap<>();

        public Builder(String productRefNo, LocalDate now) {
            this.productRefNo = productRefNo;
            this.now = now;
        }

        public Shortages build() {
            return new Shortages(productRefNo, now, shortages);
        }

        public Builder add(LocalDate date, long missingLevel) {
            shortages.put(date, missingLevel);
            return this;
        }

        public Builder merge(Builder other) {
            shortages.putAll(other.shortages);
            return this;
        }
    }
}
