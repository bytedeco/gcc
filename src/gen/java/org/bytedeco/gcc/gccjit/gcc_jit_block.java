// Targeted by JavaCPP version 1.5.7-SNAPSHOT: DO NOT EDIT THIS FILE

package org.bytedeco.gcc.gccjit;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static org.bytedeco.gcc.global.gccjit.*;


/* A gcc_jit_block encapsulates a "basic block" of statements within a
   function (i.e. with one entry point and one exit point).

   Every block within a function must be terminated with a conditional,
   a branch, or a return.

   The blocks within a function form a directed graph.

   The entrypoint to the function is the first block created within
   it.

   All of the blocks in a function must be reachable via some path from
   the first block.

   It's OK to have more than one "return" from a function (i.e. multiple
   blocks that terminate by returning).  */
@Opaque @Properties(inherit = org.bytedeco.gcc.presets.gccjit.class)
public class gcc_jit_block extends Pointer {
    /** Empty constructor. Calls {@code super((Pointer)null)}. */
    public gcc_jit_block() { super((Pointer)null); }
    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
    public gcc_jit_block(Pointer p) { super(p); }
}
