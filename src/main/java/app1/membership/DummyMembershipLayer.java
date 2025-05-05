package app1.membership;

import dcr1.common.events.userset.values.*;

import java.util.*;
import java.util.stream.Collectors;

// TODO [validate arg]
// TODO [to be removed] temporary setting for testing purposes - hardcoded participants
public final class DummyMembershipLayer {

    // TODO [validate args]

    public record DummyNeighbour(UserVal user, String hostName) {

        String role() {return user().role();}

        // String id() {return user().getId().value();}
    }

    private static final DummyMembershipLayer singleton = new DummyMembershipLayer();

    private final Map<UserVal, DummyNeighbour> neighbourMapping;
    private final Map<String, Set<DummyNeighbour>> neighboursByRole;

    public static DummyMembershipLayer instance() {return singleton;}

    public static DummyMembershipLayer dummyInstance() {
        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("Prosumer", "p1"), "Prosumer_p1"));
        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("Prosumer", "p2"), "Prosumer_p2"));
        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("Prosumer", "p3"), "Prosumer_p3"));
        return singleton;
    }

    private static <K, V> void putIfAbsent(Map<K, Set<V>> mapping, K key, V value) {
        Optional.ofNullable(mapping.get(key)).ifPresentOrElse(set -> set.add(value), () -> {
            Set<V> set = new HashSet<>();
            set.add(value);
            mapping.put(key, set);
        });
    }

    public void onNeighborUp(DummyNeighbour neighbourUp) {
        if (neighbourMapping.putIfAbsent(neighbourUp.user(), neighbourUp) == null) {
            putIfAbsent(neighboursByRole, neighbourUp.role(), neighbourUp);
        }
    }

    public void onNeighborDown(DummyNeighbour neighbourDown) {
        Optional.ofNullable(neighbourMapping.remove(neighbourDown.user()))
                .ifPresent(dummyNeighbour -> neighboursByRole.get(dummyNeighbour.role())
                        .remove(dummyNeighbour));
    }

    public Set<DummyNeighbour> resolveParticipants(UserSetVal receivers) {
        Set<DummyNeighbour> evalResult = new HashSet<>();
        switch (receivers) {
            case UserVal user -> {
                // System.err.println("mapping: " + neighbourMapping);
                for(var key : neighbourMapping.keySet())
                //     System.err.printf("key: %s, hashcode: %d%n", key, key.hashCode());
                // System.err.printf("userval: %s, hashcode: %d%n: ", user, user.hashCode());
                    Optional.ofNullable(neighbourMapping.get(user)).ifPresent(evalResult::add);
            }
            // TODO subsequent filter according to params
            case RoleVal role -> evalResult.addAll(
                    neighboursByRole.getOrDefault(role.role(), Collections.emptySet()));
            case SetDiffVal setDiff -> {
                var positiveSet = new HashSet<>(resolveParticipants(setDiff.positiveSet()));
                var negativeSet = new HashSet<>(resolveParticipants(setDiff.negativeSet()));
                positiveSet.removeAll(negativeSet);
                // System.err.println(
                //         "positive set in evalUserSetExpr:DummyMembership Layer: " + positiveSet);
                evalResult.addAll(positiveSet);
            }
            case SetUnionVal unionSet ->
                    unionSet.userSetVals().forEach(expr -> evalResult.addAll(resolveParticipants(expr)));
        }
        System.err.println("eval result on membership: " + evalResult);
        return evalResult;
    }

    private Set<UserVal> dummySend(UserSetVal receivers) {
        var evalResult = resolveParticipants(receivers);
        // TODO [not yet implemented] actual send (a DCR Protocol callback?)
        return evalResult.stream()
                .map(DummyNeighbour::user)
                .collect(Collectors.toUnmodifiableSet());
    }

    private DummyMembershipLayer() {
        neighbourMapping = new HashMap<>();
        neighboursByRole = new HashMap<>();
    }

    public static void main(String[] args) {
        var communicationLayer = dummyInstance();
        var role = RoleVal.of("Prosumer");
        var prosumerUser = UserVal.of("Prosumer", "p1");
        var otherUser = UserVal.of("Foo", "foo");
        var diff = SetDiffVal.of(role, prosumerUser);
        System.err.println(communicationLayer.dummySend(diff));
        diff = SetDiffVal.of(role, otherUser);
        System.err.println(communicationLayer.dummySend(diff));
    }
}
