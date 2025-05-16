package dcr.common.data.types;


import java.util.Objects;

// TODO [sanitize args]
// TODO [revisit] should this be <T> parametrised? Not just label?
// TODO [javadoc]

/**
 * EventIdType
 * @param label
 */
public record EventIdType(String label)
        implements Type {

    public static EventIdType of(String label) {return new EventIdType(label);}

    @Override
    public int hashCode() {
        return Objects.hashCode(label);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;}
        return label.equals(((EventIdType) obj).label);
    }

    @Override
    public String toString() {return label;}
}
