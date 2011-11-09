package org.jruby.compiler.ir.instructions.calladapter;

import org.jruby.compiler.ir.operands.Label;
import org.jruby.compiler.ir.operands.Operand;
import org.jruby.interpreter.InterpreterContext;
import org.jruby.runtime.Block;
import org.jruby.runtime.CallSite;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

/**
 *
 */
public class TwoArgBlockOperandCallAdapter extends ClosureCallAdapter {
    private Operand arg1;
    private Operand arg2;
    
    public TwoArgBlockOperandCallAdapter(CallSite callSite, Operand[] args, Operand closure) {
        super(callSite, closure);
        
        assert args.length == 2;
        
        arg1 = args[0];
        arg2 = args[1];
    }

    @Override
    public Label call(InterpreterContext interp, ThreadContext context, Operand result, IRubyObject self, IRubyObject receiver) {
        IRubyObject value1 = (IRubyObject) arg1.retrieve(interp, context, self);
        IRubyObject value2 = (IRubyObject) arg2.retrieve(interp, context, self);
        Block block = prepareBlock(interp, context, self);
        
        try {
            result.store(interp, context, self, callSite.call(context, self, receiver, value1, value2, block));
        } finally {
            block.escape();
        }
        
        return null;
    }    
}