package protocols.application;

import dcr.common.Record;
import dcr.common.data.values.IntVal;
import dcr.common.data.values.StringVal;
import dcr.common.events.userset.values.*;
import dcr.runtime.communication.MembershipLayer;
import org.apache.commons.lang3.NotImplementedException;
import pt.unl.fct.di.novasys.babel.protocols.membership.VersionedPeer;
import pt.unl.fct.di.novasys.network.data.Host;

import java.util.*;
import java.util.stream.Collectors;

// TODO [validate arg]
// TODO [to be removed] temporary setting for testing purposes - hardcoded participants
public final class DummyMembershipLayer
        implements MembershipLayer {


    // TODO [validate args]

    public record DummyNeighbour(UserVal user, String hostName)
            implements Neighbour {

        @Override
        public Host host() {
            throw new NotImplementedException("host() not yet implemented");
        }

        // String role() {return user().role();}

        // String id() {return user().getId().value();}
    }

    private static final DummyMembershipLayer singleton = new DummyMembershipLayer();

    static {
        // USE CASE EDP V1
        // singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
        //         Record.ofEntries(Record.Field.of("id", StringVal.of("1")))), "P_1"));
        // singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
        //         Record.ofEntries(Record.Field.of("id", StringVal.of("2")))), "P_2"));
        // singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
        //         Record.ofEntries(Record.Field.of("id", StringVal.of("3")))), "P_3"));
        // singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
        //         Record.ofEntries(Record.Field.of("id", StringVal.of("4")))), "P_4"));





        // USE CASE EDP V3
        // EC 1
//        singleton.onNeighborUp(new DummyNeighbour(
//                UserVal.of("CO", Record.ofEntries(Record.Field.of("cid", IntVal.of(1)))), "CO_1"));
        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
                Record.ofEntries(Record.Field.of("id", StringVal.of("1")),
                        Record.Field.of("cid", IntVal.of(1)))), "P_1_1"));
        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
                Record.ofEntries(Record.Field.of("id", StringVal.of("2")),
                        Record.Field.of("cid", IntVal.of(1)))), "P_2_1"));
        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
                Record.ofEntries(Record.Field.of("id", StringVal.of("3")),
                        Record.Field.of("cid", IntVal.of(1)))), "P_3_1"));
//        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
//                Record.ofEntries(Record.Field.of("id", StringVal.of("4")),
//                        Record.Field.of("cid", IntVal.of(1)))), "P_4_1"));
        // EC 2
        singleton.onNeighborUp(new DummyNeighbour(
                UserVal.of("CO", Record.ofEntries(Record.Field.of("cid", IntVal.of(2)))), "CO_2"));
        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
                Record.ofEntries(Record.Field.of("id", StringVal.of("1")),
                        Record.Field.of("cid", IntVal.of(2)))), "P_1_2"));
        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
                Record.ofEntries(Record.Field.of("id", StringVal.of("2")),
                        Record.Field.of("cid", IntVal.of(2)))), "P_2_2"));
        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
                Record.ofEntries(Record.Field.of("id", StringVal.of("3")),
                        Record.Field.of("cid", IntVal.of(2)))), "P_3_2"));
        singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
                Record.ofEntries(Record.Field.of("id", StringVal.of("4")),
                        Record.Field.of("cid", IntVal.of(2)))), "P_4_2"));
    }

    private final Map<UserVal, Neighbour> neighbourMapping;
    private final Map<String, Set<Neighbour>> neighboursByRole;

    public static DummyMembershipLayer instance() {return singleton;}
    //
    // public static DummyMembershipLayer dummyInstance() {
    //     // EC 1
    //     singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
    //             Record.ofEntries(Record.Field.of("id", StringVal.of("1")),
    //                     Record.Field.of("cid", IntVal.of(1)))), "P_1_1"));
    //     singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
    //             Record.ofEntries(Record.Field.of("id", StringVal.of("2")),
    //                     Record.Field.of("cid", IntVal.of(1)))), "P_2_1"));
    //     singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
    //             Record.ofEntries(Record.Field.of("id", StringVal.of("4")),
    //                     Record.Field.of("cid", IntVal.of(1)))), "P_4_1"));
    //     // EC 2
    //     singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
    //             Record.ofEntries(Record.Field.of("id", StringVal.of("1")),
    //                     Record.Field.of("cid", IntVal.of(2)))), "P_1_2"));
    //     singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
    //             Record.ofEntries(Record.Field.of("id", StringVal.of("2")),
    //                     Record.Field.of("cid", IntVal.of(2)))), "P_2_2"));
    //     singleton.onNeighborUp(new DummyNeighbour(UserVal.of("P",
    //             Record.ofEntries(Record.Field.of("id", StringVal.of("3")),
    //                     Record.Field.of("cid", IntVal.of(2)))), "P_3_2"));
    //
    //     //
    //     // singleton.onNeighborUp(new DummyNeighbour(UserVal.of("Prosumer", "p1"),
    //     "Prosumer_p1"));
    //     // singleton.onNeighborUp(new DummyNeighbour(UserVal.of("Prosumer", "p2"),
    //     "Prosumer_p2"));
    //     // singleton.onNeighborUp(new DummyNeighbour(UserVal.of("Prosumer", "p3"),
    //     "Prosumer_p3"));
    //     return singleton;
    // }

    private static <K, V> void putIfAbsent(Map<K, Set<V>> mapping, K key, V value) {
        Optional.ofNullable(mapping.get(key)).ifPresentOrElse(set -> set.add(value), () -> {
            Set<V> set = new HashSet<>();
            set.add(value);
            mapping.put(key, set);
        });
    }


    @Override
    public void onNeighborUp(Neighbour neighbourUp) {
        if (neighbourMapping.putIfAbsent(neighbourUp.user(), neighbourUp) == null) {
            putIfAbsent(neighboursByRole, neighbourUp.role(), neighbourUp);
        }
    }

    @Override
    public void onNeighborDown(Neighbour neighbourDown) {

    }

    public void onNeighborDown(DummyNeighbour neighbourDown) {
        Optional.ofNullable(neighbourMapping.remove(neighbourDown.user()))
                .ifPresent(dummyNeighbour -> neighboursByRole.get(dummyNeighbour.role())
                        .remove(dummyNeighbour));
    }

    public Set<Neighbour> resolveParticipants(UserSetVal receivers) {
        Set<Neighbour> evalResult = new HashSet<>();
        switch (receivers) {
            case UserVal user -> {
                for (var key : neighbourMapping.keySet())
                    Optional.ofNullable(neighbourMapping.get(user)).ifPresent(evalResult::add);
            }
            // TODO subsequent filter according to params
            case RoleVal role -> {
                // neighboursByRole.forEach(
                //         (key, value) -> value.forEach(n -> System.err.println(n.user())));
                var candidates = neighboursByRole.getOrDefault(role.role(), Collections.emptySet());
                // candidates.forEach(candidate -> System.err.println(candidate.user()));
                // System.err.println();
                for (var param : role.params()) {
                    candidates = candidates.stream()
                            .filter(u -> param.value()
                                    .equals(u.user()
                                            .getParamsAsRecordVal()
                                            .fetchProp(param.name())))
                            .collect(Collectors.toSet());
                }
                // candidates.forEach(candidate -> System.err.println(candidate.user()));
                evalResult.addAll(candidates);
            }
            case SetDiffVal setDiff -> {
                var positiveSet = new HashSet<>(resolveParticipants(setDiff.positiveSet()));
                var negativeSet = new HashSet<>(resolveParticipants(setDiff.negativeSet()));
                positiveSet.removeAll(negativeSet);
                evalResult.addAll(positiveSet);
            }
            case SetUnionVal unionSet -> unionSet.userSetVals()
                    .forEach(expr -> evalResult.addAll(resolveParticipants(expr)));
        }
        // System.err.println("eval result on membership: " + evalResult);
        return evalResult;
    }

    private Set<UserVal> dummySend(UserSetVal receivers) {
        var evalResult = resolveParticipants(receivers);
        // TODO [not yet implemented] actual send (a DCR Protocol callback?)
        return evalResult.stream().map(Neighbour::user).collect(Collectors.toUnmodifiableSet());
    }

    private DummyMembershipLayer() {
        neighbourMapping = new HashMap<>();
        neighboursByRole = new HashMap<>();
    }

    // public static void main(String[] args) {
    //     var communicationLayer = dummyInstance();
    //     var role = RoleVal.of("Prosumer");
    //     var prosumerUser = UserVal.of("Prosumer", "p1");
    //     var otherUser = UserVal.of("Foo", "foo");
    //     var diff = SetDiffVal.of(role, prosumerUser);
    //     System.err.println(communicationLayer.dummySend(diff));
    //     diff = SetDiffVal.of(role, otherUser);
    //     System.err.println(communicationLayer.dummySend(diff));
    // }
}
