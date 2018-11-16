package demands;

import java.time.LocalDate;

public class DemandId {
    private final String productRefNo;
    private final LocalDate date;

    public DemandId(String productRefNo, LocalDate date) {
        this.productRefNo = productRefNo;
        this.date = date;
    }

    public String getProductRefNo() {
        return productRefNo;
    }

    public LocalDate getDate() {
        return date;
    }
}
