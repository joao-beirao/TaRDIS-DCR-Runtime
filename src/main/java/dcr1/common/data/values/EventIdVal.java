package dcr1.common.data.values;

import dcr1.common.data.types.EventIdType;
import dcr1.common.data.types.Type;
import org.jetbrains.annotations.NotNull;

// TODO validate args - (reminder -> proper event id)

// TODO [reminder] reuse eventType refs - many EventIdVal instances but, comparatively, very few
//  event types - and a ref type

// TODO [revisit] should EventIdType be <T> parametrised? Not just label?
public record EventIdVal(String id)
        implements Value {

    public static EventIdVal of(String id) {return new EventIdVal(id);}

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;}
        ;
        return id.equals(((EventIdVal) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @NotNull
    @Override
    public String toString() {return id;}

    @Override
    public EventIdType type() {
        throw new RuntimeException("Not yet implemented");
    }

}