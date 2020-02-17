/*
 *  Copyright 2019, Yahoo Inc.
 *  Licensed under the terms of the Apache License, Version 2.0.
 *  See the LICENSE file associated with the compute for terms.
 */
package com.yahoo.bullet.postaggregations;

import com.yahoo.bullet.parsing.Having;
import com.yahoo.bullet.querying.evaluators.Evaluator;
import com.yahoo.bullet.result.Clip;
import com.yahoo.bullet.typesystem.Type;

public class HavingStrategy implements PostStrategy {
    private Evaluator evaluator;

    /**
     *
     * @param having
     */
    public HavingStrategy(Having having) {
        evaluator = having.getExpression().getEvaluator();
    }

    @Override
    public Clip execute(Clip clip) {
        clip.getRecords().removeIf(record -> {
            try {
                return !((Boolean) evaluator.evaluate(record).forceCast(Type.BOOLEAN).getValue());
            } catch (Exception ignored) {
                return true;
            }
        });
        return clip;
    }
}
