# Complete Map Hierarchy in Java

> From top-level interfaces down to every concrete implementation present in this project.

---

## Interface / Class Hierarchy Tree

```
Iterable<T>                         (java.lang)
└── Collection<E>                   (java.util)  ← Map does NOT extend Collection
                                                    (shown only for context)

Map<K,V>                            (java.util)  ← ROOT of the Map hierarchy
│
├── AbstractMap<K,V>                (java.util)  ← skeletal implementation
│   │
│   ├── HashMap<K,V>               (java.util)  ──► MyHashMap.java
│   │   └── LinkedHashMap<K,V>     (java.util)  ──► MyLinkedHashMap.java
│   │
│   ├── EnumMap<K,V>               (java.util)  ──► MyEnumMap.java
│   ├── WeakHashMap<K,V>           (java.util)  ──► MyWeakHashMap.java
│   ├── IdentityHashMap<K,V>       (java.util)  ──► MyIdentityHashMap.java
│   │
│   └── TreeMap<K,V>               (java.util)  ──► MySortedMap.java
│       implements SortedMap<K,V>
│       implements NavigableMap<K,V>
│
├── SortedMap<K,V>                  (java.util)  ← extends Map
│   └── NavigableMap<K,V>          (java.util)  ← extends SortedMap
│       └── TreeMap<K,V>           (java.util)  ──► MySortedMap.java
│           (also extends AbstractMap — see above)
│
├── ConcurrentMap<K,V>             (java.util.concurrent)  ← extends Map
│   │
│   ├── ConcurrentHashMap<K,V>    (java.util.concurrent)  ──► ConcurrentMaps/MyConcurrentHashMap.java
│   │   extends AbstractMap
│   │   implements ConcurrentMap
│   │
│   └── ConcurrentNavigableMap<K,V>  (java.util.concurrent)  ← extends ConcurrentMap + NavigableMap
│       └── ConcurrentSkipListMap<K,V>  (java.util.concurrent)  ──► ConcurrentMaps/MyConcurrentSkipListMap.java
│
└── [Immutable Maps — no public class; created via factory methods]
    Map.of(...)  /  Map.copyOf(...)  /  Map.entry(...)   ──► MyImmutableMap.java
    (internal class: ImmutableCollections.MapN / Map1)
```

---

## Flat Reference Table

| Level | Type | Name | File in Project |
|-------|------|------|-----------------|
| 1 | Interface | `Map<K,V>` | — |
| 2 | Interface | `SortedMap<K,V>` | — |
| 2 | Interface | `NavigableMap<K,V>` | — |
| 2 | Interface | `ConcurrentMap<K,V>` | — |
| 2 | Interface | `ConcurrentNavigableMap<K,V>` | — |
| 2 | Abstract Class | `AbstractMap<K,V>` | — |
| 3 | Concrete Class | `HashMap<K,V>` | `MyHashMap.java` |
| 4 | Concrete Class | `LinkedHashMap<K,V>` | `MyLinkedHashMap.java` |
| 3 | Concrete Class | `TreeMap<K,V>` | `MySortedMap.java` |
| 3 | Concrete Class | `EnumMap<K,V>` | `MyEnumMap.java` |
| 3 | Concrete Class | `WeakHashMap<K,V>` | `MyWeakHashMap.java` |
| 3 | Concrete Class | `IdentityHashMap<K,V>` | `MyIdentityHashMap.java` |
| 3 | Concrete Class | `ConcurrentHashMap<K,V>` | `ConcurrentMaps/MyConcurrentHashMap.java` |
| 4 | Concrete Class | `ConcurrentSkipListMap<K,V>` | `ConcurrentMaps/MyConcurrentSkipListMap.java` |
| — | Factory (Immutable) | `Map.of / Map.copyOf` | `MyImmutableMap.java` |

---

## Key Notes

- **`Map` does NOT extend `Iterable` or `Collection`.**  
  It is a completely separate root interface. `Iterable` is shown at the top only for
  orientation with the rest of the Collections Framework.

- **`AbstractMap`** provides a skeletal implementation to minimise the effort required to
  implement `Map`. Most concrete maps extend it.

- **`LinkedHashMap`** extends `HashMap` and maintains **insertion order** via a doubly-linked
  list running through its entries.

- **`TreeMap`** implements both `NavigableMap` (and therefore `SortedMap`) **and** extends
  `AbstractMap`. It keeps keys in **natural/comparator order** using a Red-Black Tree.

- **`ConcurrentHashMap`** is thread-safe without locking the whole map — it uses **segment /
  bucket-level locking** (Java 8+: CAS + `synchronized` on individual bins).

- **`ConcurrentSkipListMap`** is thread-safe and **sorted**, backed by a **Skip List** data
  structure. It implements `ConcurrentNavigableMap`.

- **`EnumMap`** is backed by a plain **array** indexed by enum ordinal — extremely fast for
  enum keys.

- **`WeakHashMap`** holds **weak references** to keys; entries are automatically removed by
  the GC when their key is no longer reachable.

- **`IdentityHashMap`** uses **reference equality (`==`)** instead of `.equals()` for key
  comparison, and `System.identityHashCode()` instead of `hashCode()`.

- **Immutable Maps** (`Map.of`, `Map.copyOf`) return unmodifiable map instances backed by
  JDK-internal classes (`ImmutableCollections.MapN`). They throw
  `UnsupportedOperationException` on any mutation attempt.
```
