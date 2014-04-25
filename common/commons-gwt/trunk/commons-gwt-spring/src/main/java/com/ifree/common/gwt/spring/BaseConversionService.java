package com.ifree.common.gwt.spring;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.core.convert.support.GenericConversionService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by alex on 25.04.14.
 */
public class BaseConversionService extends GenericConversionService {
    public <S, T> Function<S, T> getConvertFuction(final Class<T> dest) {
        return new Function<S, T>() {
            @Nullable
            @Override
            public T apply(@Nullable S input) {
                return convert(input, dest);
            }
        };
    }

    @Nullable
    public <F, T> Collection<T> transform(@Nullable Collection<F> fromCollection, Class<T> dest) {
        if (fromCollection != null) {
            return Collections2.transform(fromCollection, getConvertFuction(dest));
        } else {
            return null;
        }
    }

    @Nullable
    public <F, T> List<T> transformToList(@Nullable Collection<F> fromCollection, Class<T> dest) {
        if (fromCollection != null) {
            return Lists.newArrayList(Collections2.transform(fromCollection, getConvertFuction(dest)));
        } else {
            return null;
        }
    }

    @Nullable
    public <F, T> Set<T> transformToSet(@Nullable Collection<F> fromCollection, Class<T> dest) {
        if (fromCollection != null) {
            return Sets.newHashSet(Collections2.transform(fromCollection, getConvertFuction(dest)));
        } else {
            return null;
        }
    }

    @Nonnull
    public <F, T> List<T> transformToListNotNull(@Nonnull Collection<F> fromCollection, Class<T> dest) {
        return Lists.newArrayList(Collections2.transform(fromCollection, getConvertFuction(dest)));
    }

    @Nonnull
    public <F, T> List<T> transformToListNotNull(@Nonnull Iterable<F> fromCollection, Class<T> dest) {
        return Lists.newArrayList(Collections2.transform(Lists.newArrayList(fromCollection), getConvertFuction(dest)));
    }
}
