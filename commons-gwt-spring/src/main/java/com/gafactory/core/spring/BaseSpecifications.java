package com.gafactory.core.spring;

import com.gafactory.core.shared.types.DateInterval;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Nonnull;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by alex on 22.04.14.
 */
public class BaseSpecifications {
    public static <T> Specification<T> baseStringIsLike(final String name, final String fieldName) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                String likePattern = getLikePattern(name);
                return cb.like(cb.lower(root.get(fieldName)), likePattern);
            }

            private String getLikePattern(final String searchTerm) {
                StringBuilder pattern = new StringBuilder();
                pattern.append("%");
                pattern.append(searchTerm.toLowerCase());
                pattern.append("%");
                return pattern.toString();
            }
        };
    }



    public static <T> Specification<T> isNullSpecification(final String field) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.isNull(root.get(field));
            }
        };

    }

    public static <T> Specification<T> stringSpecification(final String value, final String field) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(field), value);
            }
        };
    }
    public static <T> Specification<T> booleanSpecification(final Boolean value, final String field) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(field), value);
            }
        };
    }
    public static <T> Specification<T> integerSpecification(final Integer value, final String field) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(field), value);
            }


        };
    }
    public static <T, N extends Number > Specification<T> numberSpecification(final N value, final String field) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(field), value);
            }

        };
    }

    public static <T> Specification<T> excludeIdListSpecification(final List<String> excludes, String field) {
        return new Specification<T>() {

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Object> path = root.get(field);
                return cb.not(path.in(excludes));
            }
        };

    }

    public static <T, E extends Enum<E>> Specification<T> enumSpecification(final E value, final String field) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(field), value);
            }


        };
    }

    public static <T> Specification<T> dateIntervalSpecification(@Nonnull DateInterval interval, String field) {
        if (interval.getFrom() == null && interval.getTo() == null) {
            return null;

        }
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (interval.getFrom() != null && interval.getTo() != null) {
                    return cb.between(root.get(field), interval.getFrom(), interval.getTo());
                } else {
                    if (interval.getFrom() != null) {
                        return cb.greaterThanOrEqualTo(root.get(field), interval.getFrom());
                    } else /*(interval.getTo() != null)*/ {
                        return cb.lessThanOrEqualTo(root.get(field), interval.getTo());
                    }
                }
            }
        };
    }
}
