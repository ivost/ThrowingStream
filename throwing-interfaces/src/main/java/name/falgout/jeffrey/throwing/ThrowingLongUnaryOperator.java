package name.falgout.jeffrey.throwing;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ThrowingLongUnaryOperator<X extends Throwable> {
  public long applyAsLong(long operand) throws X;

  default public LongUnaryOperator fallbackTo(LongUnaryOperator fallback) {
    return fallbackTo(fallback, null);
  }

  default public LongUnaryOperator fallbackTo(LongUnaryOperator fallback,
      @Nullable Consumer<? super Throwable> thrown) {
    ThrowingLongUnaryOperator<Nothing> t = fallback::applyAsLong;
    return orTry(t, thrown)::applyAsLong;
  }

  default public <Y extends Throwable> ThrowingLongUnaryOperator<Y>
      orTry(ThrowingLongUnaryOperator<? extends Y> f) {
    return orTry(f, null);
  }

  default public <Y extends Throwable> ThrowingLongUnaryOperator<Y> orTry(
      ThrowingLongUnaryOperator<? extends Y> f, @Nullable Consumer<? super Throwable> thrown) {
    return t -> {
      ThrowingSupplier<Long, X> s = () -> applyAsLong(t);
      return s.orTry(() -> f.applyAsLong(t), thrown).get();
    };
  }

  default public <Y extends Throwable> ThrowingLongUnaryOperator<Y> rethrow(Class<X> x,
      Function<? super X, ? extends Y> mapper) {
    return t -> {
      ThrowingSupplier<Long, X> s = () -> applyAsLong(t);
      return s.rethrow(x, mapper).get();
    };
  }
}
