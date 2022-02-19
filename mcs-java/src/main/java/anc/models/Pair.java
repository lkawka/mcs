package anc.models;

import com.google.common.base.Objects;

public class Pair {
    public final Integer v1;
    public final Integer v2;

    public Pair(Integer v1, Integer v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public boolean contains1(int v) {
        if(v1 == v)
            return true;
        return false;
    }

    public boolean contains2(int v) {
        if(v2 == v)
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(v1, v2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair other = (Pair) obj;
        return Objects.equal(v1, other.v1) && Objects.equal(v2, other.v2);
    }

    @Override
    public String toString() {
        return "Pair [v1=" + v1 + ", v2=" + v2 + "]";
    }
}
