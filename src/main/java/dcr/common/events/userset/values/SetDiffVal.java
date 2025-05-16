package dcr.common.events.userset.values;


import java.util.Objects;

// TODO [sanitize args]

public record SetDiffVal(UserSetVal positiveSet, UserSetVal negativeSet)
        implements UserSetVal {

    // TODO [sanitize args]
    public SetDiffVal {
        Objects.requireNonNull(positiveSet);
        Objects.requireNonNull(negativeSet);
    }

    public static SetDiffVal of(UserSetVal positiveSet, UserSetVal negativeSet) {
        return new SetDiffVal(positiveSet, negativeSet);
    }

    @Override
    public String toString() {
        return String.format("%s \\ %s", positiveSet, negativeSet);
    }
}