package throwing.stream.adapter;

import throwing.ThrowingBaseSpliterator;
import throwing.function.ThrowingConsumer;
import throwing.function.ThrowingDoubleConsumer;
import throwing.function.ThrowingIntConsumer;
import throwing.function.ThrowingLongConsumer;

abstract class CheckedSpliterator<T, X extends Throwable, D extends java.util.Spliterator<T>, S extends ThrowingBaseSpliterator<T, X, S>> extends
    CheckedAdapter<D, X> implements ThrowingBaseSpliterator<T, X, S>, ChainingAdapter<D, S> {
  static class Basic<T, X extends Throwable> extends
      CheckedSpliterator<T, X, java.util.Spliterator<T>, ThrowingBaseSpliterator.ThrowingSpliterator<T, X>> implements
      ThrowingBaseSpliterator.ThrowingSpliterator<T, X> {
    Basic(java.util.Spliterator<T> delegate, FunctionAdapter<X> functionAdapter) {
      super(delegate, functionAdapter);
    }

    @Override
    public ThrowingBaseSpliterator.ThrowingSpliterator<T, X> trySplit() {
      return chain(java.util.Spliterator<T>::trySplit);
    }

    @Override
    public ThrowingBaseSpliterator.ThrowingSpliterator<T, X> getSelf() {
      return this;
    }

    @Override
    public ThrowingBaseSpliterator.ThrowingSpliterator<T, X> createNewAdapter(
        java.util.Spliterator<T> newDelegate) {
      return new CheckedSpliterator.Basic<>(newDelegate, getFunctionAdapter());
    }
  }

  static class OfInt<X extends Throwable> extends
      CheckedSpliterator<Integer, X, java.util.Spliterator.OfInt, ThrowingBaseSpliterator.OfInt<X>> implements
      ThrowingBaseSpliterator.OfInt<X> {
    OfInt(java.util.Spliterator.OfInt delegate, FunctionAdapter<X> functionAdapter) {
      super(delegate, functionAdapter);
    }

    @Override
    public ThrowingBaseSpliterator.OfInt<X> trySplit() {
      return chain(java.util.Spliterator.OfInt::trySplit);
    }

    @Override
    public boolean tryAdvance(ThrowingIntConsumer<? extends X> action) throws X {
      return unmaskException(() -> getDelegate().tryAdvance(getFunctionAdapter().convert(action)));
    }

    @Override
    public ThrowingBaseSpliterator.OfInt<X> getSelf() {
      return this;
    }

    @Override
    public ThrowingBaseSpliterator.OfInt<X> createNewAdapter(java.util.Spliterator.OfInt newDelegate) {
      return new CheckedSpliterator.OfInt<>(newDelegate, getFunctionAdapter());
    }
  }

  static class OfLong<X extends Throwable> extends
      CheckedSpliterator<Long, X, java.util.Spliterator.OfLong, ThrowingBaseSpliterator.OfLong<X>> implements
      ThrowingBaseSpliterator.OfLong<X> {
    OfLong(java.util.Spliterator.OfLong delegate, FunctionAdapter<X> functionAdapter) {
      super(delegate, functionAdapter);
    }

    @Override
    public ThrowingBaseSpliterator.OfLong<X> trySplit() {
      return chain(java.util.Spliterator.OfLong::trySplit);
    }

    @Override
    public boolean tryAdvance(ThrowingLongConsumer<? extends X> action) throws X {
      return unmaskException(() -> getDelegate().tryAdvance(getFunctionAdapter().convert(action)));
    }

    @Override
    public ThrowingBaseSpliterator.OfLong<X> getSelf() {
      return this;
    }

    @Override
    public ThrowingBaseSpliterator.OfLong<X> createNewAdapter(
        java.util.Spliterator.OfLong newDelegate) {
      return new CheckedSpliterator.OfLong<>(newDelegate, getFunctionAdapter());
    }
  }

  static class OfDouble<X extends Throwable> extends
      CheckedSpliterator<Double, X, java.util.Spliterator.OfDouble, ThrowingBaseSpliterator.OfDouble<X>> implements
      ThrowingBaseSpliterator.OfDouble<X> {
    OfDouble(java.util.Spliterator.OfDouble delegate, FunctionAdapter<X> functionAdapter) {
      super(delegate, functionAdapter);
    }

    @Override
    public ThrowingBaseSpliterator.OfDouble<X> trySplit() {
      return chain(java.util.Spliterator.OfDouble::trySplit);
    }

    @Override
    public boolean tryAdvance(ThrowingDoubleConsumer<? extends X> action) throws X {
      return unmaskException(() -> getDelegate().tryAdvance(getFunctionAdapter().convert(action)));
    }

    @Override
    public ThrowingBaseSpliterator.OfDouble<X> getSelf() {
      return this;
    }

    @Override
    public ThrowingBaseSpliterator.OfDouble<X> createNewAdapter(
        java.util.Spliterator.OfDouble newDelegate) {
      return new CheckedSpliterator.OfDouble<>(newDelegate, getFunctionAdapter());
    }
  }

  CheckedSpliterator(D delegate, FunctionAdapter<X> functionAdapter) {
    super(delegate, functionAdapter);
  }

  @Override
  public boolean tryAdvance(ThrowingConsumer<? super T, ? extends X> action) throws X {
    return unmaskException(() -> getDelegate().tryAdvance(getFunctionAdapter().convert(action)));
  }

  @Override
  public long estimateSize() {
    return getDelegate().estimateSize();
  }

  @Override
  public int characteristics() {
    return getDelegate().characteristics();
  }
}
