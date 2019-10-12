package ch.hsr.servicecutter.model.solver;

import ch.hsr.servicecutter.api.model.EntityRelationDiagram;
import ch.hsr.servicecutter.model.usersystem.Nanoentity;

public class EntityPair {
    public final Nanoentity nanoentityA;
    public final Nanoentity nanoentityB;

    public EntityPair(final Nanoentity nanoentityA, final Nanoentity nanoentityB) {
        this.nanoentityA = nanoentityA;
        this.nanoentityB = nanoentityB;
    }

    @Override
    public int hashCode() {
        return getCompareString().hashCode();
    }

    private String getCompareString() {
        String idA = nanoentityA.getContextName();
        String idB = nanoentityB.getContextName();
        String stringToHash;
        if (idA.compareTo(idB) > 0) {
            stringToHash = idA + "-" + idB;
        } else {
            stringToHash = idB + "-" + idA;
        }
        return stringToHash;
    }

    @Override
    public String toString() {
        return nanoentityA.getContextName() + " - " + nanoentityB.getContextName();
    }

    @Override
    public boolean equals(final Object obj) {
        return getCompareString().equals(((EntityPair) obj).getCompareString());
    }

}
