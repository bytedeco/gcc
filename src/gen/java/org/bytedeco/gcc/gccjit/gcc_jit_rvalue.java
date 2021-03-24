// Targeted by JavaCPP version 1.5.6-SNAPSHOT: DO NOT EDIT THIS FILE

package org.bytedeco.gcc.gccjit;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static org.bytedeco.gcc.global.gccjit.*;


/* A gcc_jit_rvalue is an expression within your code, with some type.  */
@Opaque @Properties(inherit = org.bytedeco.gcc.presets.gccjit.class)
public class gcc_jit_rvalue extends Pointer {
    /** Empty constructor. Calls {@code super((Pointer)null)}. */
    public gcc_jit_rvalue() { super((Pointer)null); }
    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
    public gcc_jit_rvalue(Pointer p) { super(p); }
}
