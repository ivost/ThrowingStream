package throwing.function;

import java.util.function.LongToIntFunction;

import throwing.Nothing;

@FunctionalInterface
public interface ThrowingLongToIntFunction<X extends Throwable> {
    public int applyAsInt(long value) throws X;

    default public LongToIntFunction fallbackTo(LongToIntFunction fallback) {
        ThrowingLongToIntFunction<Nothing> t = fallback::applyAsInt;
        return orTry(t)::applyAsInt;
    }

    default public <Y extends Throwable> ThrowingLongToIntFunction<Y> orTry(ThrowingLongToIntFunction<? extends Y> f) {
        return t -> {
            ThrowingSupplier<Integer, X> s = () -> applyAsInt(t);
            return s.orTry(() -> f.applyAsInt(t)).get();
        };
    }
}
