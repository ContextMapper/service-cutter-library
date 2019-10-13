package ch.hsr.servicecutter.model.criteria;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class CouplingCriterionCharacteristic {

    private String name;
    private Integer weight;
    private boolean isDefault = false;
    private CouplingCriterion couplingCriterion;

    public CouplingCriterionCharacteristic(CouplingCriterion criterion, String name, Integer weight, boolean isDefault) {
        this.name = name;
        this.couplingCriterion = criterion;
        this.weight = weight;
        this.isDefault = isDefault;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public CouplingCriterion getCouplingCriterion() {
        return couplingCriterion;
    }

    public String getName() {
        return name;
    }

    public Integer getWeight() {
        return weight;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, couplingCriterion);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CouplingCriterionCharacteristic) {
            CouplingCriterionCharacteristic other = (CouplingCriterionCharacteristic) obj;
            return this == other || (Objects.equal(name, other.name) && Objects.equal(couplingCriterion, other.couplingCriterion));
        } else {
            return false;
        }
    }
}
