package beans;

import java.math.BigDecimal;

public class StatisticalOutput {
    private BigDecimal roFlakyRate;
    private BigDecimal fafFlakyRate;
    private BigDecimal trwFlakyRate;
    private BigDecimal itFlakyRate;
    private BigDecimal ctlFlakyRate;

    public BigDecimal getRoFlakyRate() {
        return roFlakyRate;
    }

    public void setRoFlakyRate(BigDecimal roFlakyRate) {
        this.roFlakyRate = roFlakyRate;
    }

    public BigDecimal getFafFlakyRate() {
        return fafFlakyRate;
    }

    public void setFafFlakyRate(BigDecimal fafFlakyRate) {
        this.fafFlakyRate = fafFlakyRate;
    }

    public BigDecimal getTrwFlakyRate() {
        return trwFlakyRate;
    }

    public void setTrwFlakyRate(BigDecimal trwFlakyRate) {
        this.trwFlakyRate = trwFlakyRate;
    }

    public BigDecimal getItFlakyRate() {
        return itFlakyRate;
    }

    public void setItFlakyRate(BigDecimal itFlakyRate) {
        this.itFlakyRate = itFlakyRate;
    }

    public BigDecimal getCtlFlakyRate() {
        return ctlFlakyRate;
    }

    public void setCtlFlakyRate(BigDecimal ctlFlakyRate) {
        this.ctlFlakyRate = ctlFlakyRate;
    }

    @Override
    public String toString() {
        return "StatisticalOutput{" +
                "roFlakyRate=" + roFlakyRate +
                ", fafFlakyRate=" + fafFlakyRate +
                ", trwFlakyRate=" + trwFlakyRate +
                ", itFlakyRate=" + itFlakyRate +
                ", ctlFlakyRate=" + ctlFlakyRate +
                '}';
    }
}
