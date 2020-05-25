package de.bentrm.datacat.graphql;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;

import java.util.HashMap;
import java.util.Map;

public class CustomInstrumentation extends SimpleInstrumentation {

    public static class CustomInstrumentationState implements InstrumentationState {
        private final Map<String, Object> currentState = new HashMap<>();

        void record(String key, long timing) {
            currentState.put(key, timing);
        }
    }

    @Override
    public InstrumentationState createState() {
        return new CustomInstrumentationState();
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        long startNanos = System.nanoTime();
        return new SimpleInstrumentationContext<>() {
            @Override
            public void onCompleted(ExecutionResult result, Throwable t) {
                CustomInstrumentationState state = parameters.getInstrumentationState();
                state.record(parameters.getQuery(), System.nanoTime() - startNanos);
            }
        };
    }
}
