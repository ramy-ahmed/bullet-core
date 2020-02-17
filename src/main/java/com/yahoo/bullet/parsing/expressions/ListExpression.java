/*
 *  Copyright 2019, Yahoo Inc.
 *  Licensed under the terms of the Apache License, Version 2.0.
 *  See the LICENSE file associated with the compute for terms.
 */
package com.yahoo.bullet.parsing.expressions;

import com.google.gson.annotations.Expose;
import com.yahoo.bullet.common.BulletError;
import com.yahoo.bullet.querying.evaluators.Evaluator;
import com.yahoo.bullet.querying.evaluators.ListEvaluator;
import com.yahoo.bullet.typesystem.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yahoo.bullet.common.BulletError.makeError;

/**
 * An expression that holds a list of expressions. A primitive type
 * must be specified as only lists of primitives are supported at the moment.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ListExpression extends Expression {
    private static final BulletError LIST_REQUIRES_NON_NULL_LIST = makeError("The values list must not be null.", "Please provide a values list.");
    private static final BulletError LIST_REQUIRES_LIST_TYPE = makeError("The type must be list (if specified).", "Please provide a list type.");
    private static final BulletError LIST_REQUIRES_PRIMITIVE_TYPE = makeError("The primitive type must be specified if type is specified.", "Please provide a primitive type.");
    private static final String DELIMITER = ", ";

    @Expose
    private List<Expression> values;

    @Override
    public Optional<List<BulletError>> initialize() {
        if (values == null) {
            return Optional.of(Collections.singletonList(LIST_REQUIRES_NON_NULL_LIST));
        }
        if (type != null) {
            //if (type != Type.LIST) {
            if (!Type.isList(type)) {
                return Optional.of(Collections.singletonList(LIST_REQUIRES_LIST_TYPE));
            }
            //if (!Type.PRIMITIVES.contains(primitiveType)) {
            //    return Optional.of(Collections.singletonList(LIST_REQUIRES_PRIMITIVE_TYPE));
            //}
        }
        List<BulletError> errors = new ArrayList<>();
        values.forEach(values -> values.initialize().ifPresent(errors::addAll));
        return errors.isEmpty() ? Optional.empty() : Optional.of(errors);
    }

    @Override
    public String getName() {
        return "[" + values.stream().map(Expression::getName).collect(Collectors.joining(DELIMITER)) + "]";
    }

    @Override
    public Evaluator getEvaluator() {
        return new ListEvaluator(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ListExpression)) {
            return false;
        }
        ListExpression other = (ListExpression) obj;
        return Objects.equals(values, other.values);// && type == other.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);//, type);
    }

    @Override
    public String toString() {
        return "{values: " + values + ", " + super.toString() + "}";
    }
}
