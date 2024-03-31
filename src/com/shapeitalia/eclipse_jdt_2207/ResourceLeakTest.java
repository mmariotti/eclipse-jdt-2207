package com.shapeitalia.eclipse_jdt_2207;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;


public class ResourceLeakTest
{
    private static class TestAutoCloseable implements AutoCloseable
    {
        @Override
        public void close()
        {
            // do nothing
        }


        private static TestAutoCloseable newInstance()
        {
            return new TestAutoCloseable();
        }


        private static TestAutoCloseable of(String str)
        {
            return new TestAutoCloseable();
        }
    }


    protected TestAutoCloseable tac;


    public void t_direct_notAssigned()
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable.newInstance();
    }


    public void t_direct_localVariable()
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable tac2 = TestAutoCloseable.newInstance();
        tac2.toString();
    }


    public void t_direct_managed()
    {
        /* CORRECT (closed)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable tac2 = TestAutoCloseable.newInstance();
        try(tac2)
        {
            tac2.toString();
        }
    }


    public void t_direct_field()
    {
        /* CORRECT (assigned to field)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        tac = TestAutoCloseable.newInstance();
    }


    public TestAutoCloseable t_direct_returned()
    {
        /* CORRECT (returned)
         * expected: NO WARNING
         * actual:   NO WARNING
         * note:     the Closeable resource is returned */
        return TestAutoCloseable.newInstance();
    }


    public void t_supplier_lambdaExpr_notAssigned(TestAutoCloseable t)
    {
        /* CORRECT (maybe warning is in the wrong place?)
         * expected: WARNING
         * actual:   WARNING
         */
        Optional.ofNullable(t).orElseGet(() -> TestAutoCloseable.newInstance());
    }


    public void t_supplier_lambdaExpr_localVariable(TestAutoCloseable t)
    {
        /* CORRECT (maybe warning is in the wrong place?)
         * expected: WARNING
         * actual:   WARNING
         */
        TestAutoCloseable t2 = Optional.ofNullable(t).orElseGet(() -> TestAutoCloseable.newInstance());
        t2.toString();
    }


    public void t_supplier_lambdaExpr_managed(TestAutoCloseable t)
    {
        /* BUG: FALSE POSITIVE (closed)
         * expected: NO WARNING
         * actual:   WARNING
         */
        TestAutoCloseable t2 = Optional.ofNullable(t).orElseGet(() -> TestAutoCloseable.newInstance());
        try(t2)
        {
            t2.toString();
        }
    }


    public void t_supplier_lambdaExpr_field(TestAutoCloseable t)
    {
        /* BUG: FALSE POSITIVE (assigned to field)
         * expected: NO WARNING
         * actual:   WARNING
         */
        tac = Optional.ofNullable(t).orElseGet(() -> TestAutoCloseable.newInstance());
    }


    public TestAutoCloseable t_supplier_lambdaExpr_returned(TestAutoCloseable t)
    {
        /* BUG FALSE POSITIVE (returned)
         * expected: NO WARNING
         * actual:   WARNING
         */
        return Optional.ofNullable(t).orElseGet(() -> TestAutoCloseable.newInstance());
    }


    public void t_supplier_lambdaExpr2_notAssigned(TestAutoCloseable t)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        Supplier<TestAutoCloseable> supplier = () -> TestAutoCloseable.newInstance();
        Optional.ofNullable(t).orElseGet(supplier);
    }


    public void t_supplier_lambdaExpr2_localVariable(TestAutoCloseable t)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        Supplier<TestAutoCloseable> supplier = () -> TestAutoCloseable.newInstance();
        TestAutoCloseable t2 = Optional.ofNullable(t).orElseGet(supplier);
        t2.toString();
    }


    public void t_supplier_lambdaExpr2_managed(TestAutoCloseable t)
    {
        /* CORRECT (closed)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        Supplier<TestAutoCloseable> supplier = () -> TestAutoCloseable.newInstance();
        TestAutoCloseable t2 = Optional.ofNullable(t).orElseGet(supplier);
        try(t2)
        {
            t2.toString();
        }
    }


    public void t_supplier_lambdaExpr2_field(TestAutoCloseable t)
    {
        /* CORRECT (assigned to field)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        Supplier<TestAutoCloseable> supplier = () -> TestAutoCloseable.newInstance();
        tac = Optional.ofNullable(t).orElseGet(supplier);
    }


    public TestAutoCloseable t_supplier_lambdaExpr2_returned(TestAutoCloseable t)
    {
        /* CORRECT (returned)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        Supplier<TestAutoCloseable> supplier = () -> TestAutoCloseable.newInstance();
        return Optional.ofNullable(t).orElseGet(supplier);
    }


    public void t_supplier_lambdaBlock_notAssigned(TestAutoCloseable t)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        Optional.ofNullable(t).orElseGet(() ->
        {
            return TestAutoCloseable.newInstance();
        });
    }


    public void t_supplier_lambdaBlock_localVariable(TestAutoCloseable t)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable t2 = Optional.ofNullable(t).orElseGet(() ->
        {
            return TestAutoCloseable.newInstance();
        });
        t2.toString();
    }


    public void t_supplier_lambdaBlock_managed(TestAutoCloseable t)
    {
        /* CORRECT (closed)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable t2 = Optional.ofNullable(t).orElseGet(() ->
        {
            return TestAutoCloseable.newInstance();
        });
        try(t2)
        {
            t2.toString();
        }
    }


    public void t_supplier_lambdaBlock_field(TestAutoCloseable t)
    {
        /* CORRECT (assigned to field)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        tac = Optional.ofNullable(t).orElseGet(() ->
        {
            return TestAutoCloseable.newInstance();
        });
    }


    public TestAutoCloseable t_supplier_lambdaBlock_returned(TestAutoCloseable t)
    {
        /* CORRECT (returned)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        return Optional.ofNullable(t).orElseGet(() ->
        {
            return TestAutoCloseable.newInstance();
        });
    }


    public void t_supplier_methdRef_notAssigned(TestAutoCloseable t)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        Optional.ofNullable(t).orElseGet(TestAutoCloseable::newInstance);
    }


    public void t_supplier_methdRef_localVariable(TestAutoCloseable t)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable t2 = Optional.ofNullable(t).orElseGet(TestAutoCloseable::newInstance);
        t2.toString();
    }


    public void t_supplier_methdRef_managed(TestAutoCloseable t)
    {
        /* CORRECT (closed)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable t2 = Optional.ofNullable(t).orElseGet(TestAutoCloseable::newInstance);
        try(t2)
        {
            t2.toString();
        }
    }


    public void t_supplier_methdRef_field(TestAutoCloseable t)
    {
        /* CORRECT (assigned to field)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        tac = Optional.ofNullable(t).orElseGet(TestAutoCloseable::newInstance);
    }


    public TestAutoCloseable t_supplier_methdRef_returned(TestAutoCloseable t)
    {
        /* CORRECT (returned)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        return Optional.ofNullable(t).orElseGet(TestAutoCloseable::newInstance);
    }


    public void t_function_lambdaExpr_notAssigned(String str)
    {
        /* CORRECT (maybe warning is in the wrong place?)
         * expected: WARNING
         * actual:   WARNING
         */
        Optional.of(str).map(x -> TestAutoCloseable.of(x)).get();
    }


    public void t_function_lambdaExpr_localVariable(String str)
    {
        /* CORRECT (maybe warning is in the wrong place?)
         * expected: WARNING
         * actual:   WARNING
         */
        TestAutoCloseable t2 = Optional.of(str).map(x -> TestAutoCloseable.of(x)).get();
        t2.toString();
    }


    public void t_function_lambdaExpr_managed(String str)
    {
        /* BUG: FALSE POSITIVE (closed)
         * expected: NO WARNING
         * actual:   WARNING
         */
        TestAutoCloseable t2 = Optional.of(str).map(x -> TestAutoCloseable.of(x)).get();
        try(t2)
        {
            t2.toString();
        }
    }


    public void t_function_lambdaExpr_field(String str)
    {
        /* CORRECT (assigned to field)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        tac = Optional.of(str).map(x -> TestAutoCloseable.of(x)).get();
    }


    public TestAutoCloseable t_function_lambdaExpr_returned(String str)
    {
        /* BUG: FALSE POSITIVE (returned)
         * expected: NO WARNING
         * actual:   WARNING
         */
        return Optional.of(str).map(x -> TestAutoCloseable.of(x)).get();
    }


    public void t_function_lambdaExpr2_notAssigned(String str)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        Function<? super String, ? extends TestAutoCloseable> mapper = x -> TestAutoCloseable.of(x);
        Optional.of(str).map(mapper).get();
    }


    public void t_function_lambdaExpr2_localVariable(String str)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        Function<? super String, ? extends TestAutoCloseable> mapper = x -> TestAutoCloseable.of(x);
        TestAutoCloseable t2 = Optional.of(str).map(mapper).get();
        t2.toString();
    }


    public void t_function_lambdaExpr2_managed(String str)
    {
        /* CORRECT (closed)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        Function<? super String, ? extends TestAutoCloseable> mapper = x -> TestAutoCloseable.of(x);
        TestAutoCloseable t2 = Optional.of(str).map(mapper).get();
        try(t2)
        {
            t2.toString();
        }
    }


    public void t_function_lambdaExpr2_field(String str)
    {
        /* CORRECT (assigned)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        Function<? super String, ? extends TestAutoCloseable> mapper = x -> TestAutoCloseable.of(x);
        tac = Optional.of(str).map(mapper).get();
    }


    public TestAutoCloseable t_function_lambdaExpr2_returned(String str)
    {
        /* CORRECT (returned)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        Function<? super String, ? extends TestAutoCloseable> mapper = x -> TestAutoCloseable.of(x);
        return Optional.of(str).map(mapper).get();
    }


    public void t_function_lambdaBlock_notAssigned(String str)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        Optional.of(str).map(x ->
        {
            return TestAutoCloseable.of(x);
        }).get();
    }


    public void t_function_lambdaBlock_localVariable(String str)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable t2 = Optional.of(str).map(x ->
        {
            return TestAutoCloseable.of(x);
        }).get();
        t2.toString();
    }


    public void t_function_lambdaBlock_managed(String str)
    {
        /* CORRECT (closed)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable t2 = Optional.of(str).map(x ->
        {
            return TestAutoCloseable.of(x);
        }).get();

        try(t2)
        {
            t2.toString();
        }
    }


    public void t_function_lambdaBlock_field(String str)
    {
        /* CORRECT (assigned to field)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        tac = Optional.of(str).map(x ->
        {
            return TestAutoCloseable.of(x);
        }).get();
    }


    public TestAutoCloseable t_function_lambdaBlock_returned(String str)
    {
        /* CORRECT (returned)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        return Optional.of(str).map(x ->
        {
            return TestAutoCloseable.of(x);
        }).get();
    }


    public void t_function_methodRef_notAssigned(String str)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        Optional.of(str).map(TestAutoCloseable::of).get();
    }


    public void t_function_methodRef_localVariable(String str)
    {
        /* BUG: FALSE NEGATIVE (not closed, assigned to field, returned nor consumed/passed-as-param)
         * expected: WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable t2 = Optional.of(str).map(TestAutoCloseable::of).get();
        t2.toString();
    }


    public void t_function_methodRef_managed(String str)
    {
        /* CORRECT (closed)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        TestAutoCloseable t2 = Optional.of(str).map(TestAutoCloseable::of).get();
        try(t2)
        {
            t2.toString();
        }
    }


    public void t_function_methodRef_field(String str)
    {
        /* CORRECT (assigned to field)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        tac = Optional.of(str).map(TestAutoCloseable::of).get();
    }


    public TestAutoCloseable t_function_methodRef_returned(String str)
    {
        /* CORRECT (returned)
         * expected: NO WARNING
         * actual:   NO WARNING
         */
        return Optional.of(str).map(TestAutoCloseable::of).get();
    }
}
