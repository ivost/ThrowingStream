package name.falgout.jeffrey.throwing;

import java.util.function.Consumer;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ThrowingDoubleToIntFunction<X extends Throwable> {
  public int applyAsInt(double value) throws X;

  default public DoubleToIntFunction fallbackTo(DoubleToIntFunction fallback) {
    return fallbackTo(fallback, null);
  }

  default public DoubleToIntFunction fallbackTo(DoubleToIntFunction fallback,
      @Nullable Consumer<? super Throwable> thrown) {
    ThrowingDoubleToIntFunction<Nothing> t = fallback::applyAsInt;
    return orTry(t, thrown)::applyAsInt;
  }

  default public <Y extends Throwable> ThrowingDoubleToIntFunction<Y>
      orTry(ThrowingDoubleToIntFunction<? extends Y> f) {
    return orTry(f, null);
  }

  default public <Y extends Throwable> ThrowingDoubleToIntFunction<Y> orTry(
      ThrowingDoubleToIntFunction<? extends Y> f, @Nullable Consumer<? super Throwable> thrown) {
    return t -> {
      ThrowingSupplier<Integer, X> s = () -> applyAsInt(t);
      return s.orTry(() -> f.applyAsInt(t), thrown).get();
    };
  }

  default public <Y extends Throwable> ThrowingDoubleToIntFunction<Y> rethrow(Class<X> x,
      Function<? super X, ? extends Y> mapper) {
    return t -> {
      ThrowingSupplier<Integer, X> s = () -> applyAsInt(t);
      return s.rethrow(x, mapper).get();
    };
  }
}
