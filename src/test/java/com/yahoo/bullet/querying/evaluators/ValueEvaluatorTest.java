/*
 *  Copyright 2020, Yahoo Inc.
 *  Licensed under the terms of the Apache License, Version 2.0.
 *  See the LICENSE file associated with the compute for terms.
 */
package com.yahoo.bullet.querying.evaluators;

import com.yahoo.bullet.query.expressions.ValueExpression;
import com.yahoo.bullet.result.RecordBox;
import com.yahoo.bullet.typesystem.Type;
import com.yahoo.bullet.typesystem.TypedObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ValueEvaluatorTest {
    @Test
    public void testConstructor() {
        ValueEvaluator evaluator = new ValueEvaluator(new ValueExpression(5));
        Assert.assertEquals(evaluator.getValue(), new TypedObject(Type.INTEGER, 5));
        Assert.assertEquals(evaluator.getType(), Type.INTEGER);
        Assert.assertEquals(evaluator.evaluate(RecordBox.get().getRecord()), evaluator.getValue());

        evaluator = new ValueEvaluator(new ValueExpression("5"));
        Assert.assertEquals(evaluator.getValue(), new TypedObject(Type.STRING, "5"));
        Assert.assertEquals(evaluator.getType(), Type.STRING);
        Assert.assertEquals(evaluator.evaluate(RecordBox.get().getRecord()), evaluator.getValue());
    }
}
