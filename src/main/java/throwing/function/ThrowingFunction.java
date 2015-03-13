package throwing.function;

import java.util.Objects;
import java.util.function.Function;

import throwing.Nothing;

@FunctionalInterface
public interface ThrowingFunction<T, R, X extends Throwable> {
    public R apply(T t) throws X;

    default public Function<T, R> fallbackTo(Function<? super T, ? extends R> fallback) {
        ThrowingFunction<T, R, Nothing> t = fallback::apply;
        return orTry(t)::apply;
    }

    default public <Y extends Throwable> ThrowingFunction<T, R, Y> orTry(
            ThrowingFunction<? super T, ? extends R, ? extends Y> f) {
        return t -> {
            ThrowingSupplier<R, X> s = () -> apply(t);
            return s.orTry(() -> f.apply(t)).get();
        };
    }

    default public <RR> ThrowingFunction<T, RR, X> andThen(Function<? super R, ? extends RR> after) {
        return andThen((ThrowingFunction<? super R, ? extends RR, ? extends X>) after::apply);
    }

    default public <RR> ThrowingFunction<T, RR, X> andThen(ThrowingFunction<? super R, ? extends RR, ? extends X> after) {
        Objects.requireNonNull(after);
        return t -> after.apply(apply(t));
    }
}
