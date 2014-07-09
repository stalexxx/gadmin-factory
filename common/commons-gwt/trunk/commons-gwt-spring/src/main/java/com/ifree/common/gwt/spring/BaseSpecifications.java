package com.ifree.common.gwt.spring;

import com.ifree.common.gwt.shared.types.DateInterval;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Nonnull;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
