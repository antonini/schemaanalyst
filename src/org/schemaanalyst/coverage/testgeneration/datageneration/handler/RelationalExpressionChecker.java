package org.schemaanalyst.coverage.testgeneration.datageneration.handler;

import org.schemaanalyst.data.*;
import org.schemaanalyst.logic.RelationalOperator;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * Created by phil on 26/02/2014.
 */
public class RelationalExpressionChecker {

    public static boolean check(Value lhs, RelationalOperator op, Value rhs) {
        return check(lhs, op, rhs, false);
    }

    public static boolean check(Value lhs, RelationalOperator op, Value rhs, boolean allowNull) {
        if (lhs == null || rhs == null) {
            return allowNull;
        }

        if (!lhs.getClass().equals(rhs.getClass())) {
            throw new RelationalExpressionCheckException(
                    lhs + "(type + " + lhs.getClass().getSimpleName() + ") is of a different type to " +
                    rhs + "(type + " + rhs.getClass().getSimpleName() + ")");
        }

        return new ValueVisitor() {
            Value lhs, rhs;
            RelationalOperator op;
            boolean result;

            boolean dispatchCheck(Value lhs, RelationalOperator op, Value rhs) {
                this.lhs = lhs;
                this.op = op;
                this.rhs = rhs;
                lhs.accept(this);
                return result;
            }

            @Override
            public void visit(BooleanValue value) {
                result = checkBooleanValue((BooleanValue) lhs,  op, (BooleanValue) rhs);
            }

            @Override
            public void visit(DateValue value) {
                result = checkCompoundValue((DateValue) lhs,  op, (DateValue) rhs);
            }

            @Override
            public void visit(DateTimeValue value) {
                result = checkCompoundValue((DateTimeValue) lhs,  op, (DateTimeValue) rhs);
            }

            @Override
            public void visit(NumericValue value) {
                result = checkNumericValue((NumericValue) lhs, op, (NumericValue) rhs);
            }

            @Override
            public void visit(StringValue value) {
                result = checkCompoundValue((StringValue) lhs,  op, (StringValue) rhs);
            }

            @Override
            public void visit(TimeValue value) {
                result = checkCompoundValue((TimeValue) lhs,  op, (TimeValue) rhs);
            }

            @Override
            public void visit(TimestampValue value) {
                result = checkNumericValue((TimestampValue) lhs, op, (TimestampValue) rhs);
            }
        }.dispatchCheck(lhs, op, rhs);
    }

    private static boolean checkCompoundValue(CompoundValue lhs, RelationalOperator op, CompoundValue rhs) {
        List<Value> lhsValues = lhs.getElements();
        List<Value> rhsValues = rhs.getElements();

        Iterator<Value> lhsIterator = lhsValues.iterator();
        Iterator<Value> rhsIterator = rhsValues.iterator();

        while (lhsIterator.hasNext() && rhsIterator.hasNext()) {
            Value nextLHSValue = lhsIterator.next();
            Value nextRHSValue = rhsIterator.next();

            if (!check(nextLHSValue, RelationalOperator.EQUALS, nextRHSValue)) {
                return check(nextLHSValue, op, nextRHSValue);
            }
        }

        // all the elements are equal to one another so far ...

        // one value has more elements than the other
        if (lhsIterator.hasNext() || rhsIterator.hasNext()) {

            switch (op) {
                case EQUALS:
                    return false;
                case NOT_EQUALS:
                    return true;
                case GREATER:
                case GREATER_OR_EQUALS:
                    return lhsIterator.hasNext();
                case LESS:
                case LESS_OR_EQUALS:
                    return rhsIterator.hasNext();
                default:
                    throw new RelationalExpressionCheckException("Unknown relational operator " + op);
            }
        }

        // the values have the same number of elements (and are equal)
        switch (op) {
            case EQUALS:
            case GREATER_OR_EQUALS:
            case LESS_OR_EQUALS:
                return true;
            case NOT_EQUALS:
            case GREATER:
            case LESS:
                return false;
            default:
                throw new RelationalExpressionCheckException("Unknown relational operator " + op);
        }
    }

    private static boolean checkNumericValue(NumericValue lhs, RelationalOperator op, NumericValue rhs) {
        BigDecimal lhsValue = lhs.get();
        BigDecimal rhsValue = rhs.get();

        switch (op) {
            case EQUALS:
                return lhsValue.equals(rhsValue);

            case NOT_EQUALS:
                return !lhsValue.equals(rhsValue);

            case GREATER:
                return lhsValue.compareTo(rhsValue) > 0;

            case GREATER_OR_EQUALS:
                return lhsValue.compareTo(rhsValue) >= 0;

            case LESS:
                return lhsValue.compareTo(rhsValue) < 0;

            case LESS_OR_EQUALS:
                return lhsValue.compareTo(rhsValue) <= 0;

            default:
                throw new RelationalExpressionCheckException("Unknown relational operator " + op);
        }
    }

    private static boolean checkBooleanValue(BooleanValue lhs, RelationalOperator op, BooleanValue rhs) {
        boolean lhsValue = lhs.get();
        boolean rhsValue = rhs.get();

        switch (op) {
            case EQUALS:
                return lhsValue == rhsValue;

            case NOT_EQUALS:
                return lhsValue != rhsValue;

            case GREATER:
            case GREATER_OR_EQUALS:
            case LESS:
            case LESS_OR_EQUALS:
                throw new RelationalExpressionCheckException("Cannot use the " + op + " operator with boolean values");

            default:
                throw new RelationalExpressionCheckException("Unknown relational operator " + op);
        }
    }
}
