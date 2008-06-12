package org.uispec4j.extension;

import org.objectweb.asm.*;
import org.uispec4j.ComponentAmbiguityException;
import org.uispec4j.ItemNotFoundException;
import org.uispec4j.Panel;
import org.uispec4j.UIComponent;
import org.uispec4j.finder.ComponentFinder;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.utils.UIComponentFactory;

class PanelClassEnhancer extends ClassAdapter implements Opcodes {
  private Extension[] extensions;

  public static byte[] transformClass(ClassReader reader, Extension[] extensions) {
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    PanelClassEnhancer transformer = new PanelClassEnhancer(writer, extensions);
    reader.accept(transformer, 0);
    return writer.toByteArray();
  }

  private PanelClassEnhancer(ClassVisitor cv, Extension[] extensions) {
    super(cv);
    this.extensions = extensions;
  }

  public MethodVisitor visitMethod(int access,
                                   String name, String desc,
                                   String signature,
                                   String[] exceptions) {

    MethodVisitor cd = cv.visitMethod(access, name, desc, signature, exceptions);
    if (cd == null) {
      return null;
    }
    if ("<clinit>".equals(name) && ((access & ACC_STATIC) != 0)) {
      return new StaticInitEnhancer(cd);
    }
    return cd;
  }

  private class StaticInitEnhancer extends MethodAdapter {
    public StaticInitEnhancer(MethodVisitor mv) {
      super(mv);
    }

    public void visitInsn(int opcode) {
      if (opcode == RETURN) {
        for (Extension extension : extensions) {
          String componentClassName = extension.getComponentClassName();
          String transformedComponentClassName = transformClassName(componentClassName);
          addStaticInitializerForExtension(transformedComponentClassName, componentClassName);
        }
      }
      mv.visitInsn(opcode);
    }

    private void addStaticInitializerForExtension(String transformedComponentClassName, String componentClassName) {
      mv.visitFieldInsn(GETSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      Label l12 = new Label();
      mv.visitJumpInsn(IFNONNULL, l12);
      mv.visitLdcInsn(componentClassName);
      mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Panel.class), "getClass",
                         Type.getMethodDescriptor(Type.getType(Class.class), new Type[]{Type.getType(String.class)}));
      mv.visitInsn(DUP);
      mv.visitFieldInsn(PUTSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      Label l13 = new Label();
      mv.visitJumpInsn(GOTO, l13);
      mv.visitLabel(l12);
      mv.visitFieldInsn(GETSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      mv.visitLabel(l13);
      mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(UIComponentFactory.class), "addUIComponentClass",
                         Type.getMethodDescriptor(Type.VOID_TYPE, new Type[]{Type.getType(Class.class)}));
    }
  }

  public void visitEnd() {
    addStaticGetClassMethod();
    for (Extension extension : extensions) {
      addExtension(extension.getComponentName(), extension.getComponentClassName());
    }
    cv.visitEnd();
  }

  private void addStaticGetClassMethod() {
    MethodVisitor v = cv.visitMethod(ACC_STATIC + ACC_PRIVATE, "getClass",
                                     Type.getMethodDescriptor(Type.getType(Class.class),
                                                              new Type[]{Type.getType(String.class)}), null, null);
    Label l0 = new Label();
    v.visitLabel(l0);
    v.visitVarInsn(ALOAD, 0);
    v.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Class.class),
                      "forName", Type.getMethodDescriptor(Type.getType(Class.class), new Type[]{Type.getType(String.class)}));
    Label l1 = new Label();
    v.visitLabel(l1);
    v.visitInsn(ARETURN);
    Label l2 = new Label();
    v.visitLabel(l2);
    v.visitVarInsn(ASTORE, 1);
    v.visitTypeInsn(NEW, Type.getInternalName(NoClassDefFoundError.class));
    v.visitInsn(DUP);
    v.visitVarInsn(ALOAD, 1);
    v.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Throwable.class), "getMessage",
                      Type.getMethodDescriptor(Type.getType(String.class), new Type[0]));
    v.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(NoClassDefFoundError.class), "<init>",
                      Type.getMethodDescriptor(Type.VOID_TYPE, new Type[]{Type.getType(String.class)}));
    v.visitInsn(ATHROW);
    v.visitTryCatchBlock(l0, l1, l2, Type.getInternalName(ClassNotFoundException.class));
    v.visitMaxs(3, 2);
  }

  private void addExtension(String componentName, String componentClassName) {
    String methodName = "get" + componentName;
    String transformedComponentClassName = transformClassName(componentClassName);
    String slashedComponentClassName = componentClassName.replace('.', '/');

    cv.visitField(ACC_STATIC + ACC_SYNTHETIC, transformedComponentClassName, "Ljava/lang/Class;", null, null);

    {
      MethodVisitor v = cv.visitMethod(ACC_PUBLIC, methodName,
                                       "(" + Type.getDescriptor(String.class) + ")L" + slashedComponentClassName + ";",
                                       null,
                                       new String[]{Type.getInternalName(ItemNotFoundException.class), Type.getInternalName(ComponentAmbiguityException.class)});
      Label l0 = new Label();
      v.visitLabel(l0);
      v.visitVarInsn(ALOAD, 0);
      v.visitFieldInsn(GETFIELD, Type.getInternalName(Panel.class), "finder", Type.getDescriptor(ComponentFinder.class));
      v.visitFieldInsn(GETSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      Label l1 = new Label();
      v.visitJumpInsn(IFNONNULL, l1);
      v.visitLdcInsn(componentClassName);
      v.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Panel.class), "getClass",
                        Type.getMethodDescriptor(Type.getType(Class.class), new Type[]{Type.getType(String.class)}));
      v.visitInsn(DUP);
      v.visitFieldInsn(PUTSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      Label l2 = new Label();
      v.visitJumpInsn(GOTO, l2);
      v.visitLabel(l1);
      v.visitFieldInsn(GETSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      v.visitLabel(l2);
      v.visitVarInsn(ALOAD, 1);
      v.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Panel.class), "getComponent",
                        Type.getMethodDescriptor(Type.getType(UIComponent.class),
                                                 new Type[]{Type.getType(ComponentFinder.class), Type.getType(Class.class), Type.getType(String.class)}));
      v.visitTypeInsn(CHECKCAST, slashedComponentClassName);
      v.visitInsn(ARETURN);
      v.visitMaxs(3, 2);
    }
    {
      MethodVisitor v = cv.visitMethod(ACC_PUBLIC, methodName,
                                       "()L" + slashedComponentClassName + ";", null,
                                       new String[]{Type.getInternalName(ItemNotFoundException.class),
                                                    Type.getInternalName(ComponentAmbiguityException.class)});
      Label l0 = new Label();
      v.visitLabel(l0);
      v.visitVarInsn(ALOAD, 0);
      v.visitFieldInsn(GETFIELD, Type.getInternalName(Panel.class), "finder", Type.getDescriptor(ComponentFinder.class));
      v.visitFieldInsn(GETSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      Label l1 = new Label();
      v.visitJumpInsn(IFNONNULL, l1);
      v.visitLdcInsn(componentClassName);
      v.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Panel.class), "getClass",
                        Type.getMethodDescriptor(Type.getType(Class.class), new Type[]{Type.getType(String.class)}));
      v.visitInsn(DUP);
      v.visitFieldInsn(PUTSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      Label l2 = new Label();
      v.visitJumpInsn(GOTO, l2);
      v.visitLabel(l1);
      v.visitFieldInsn(GETSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      v.visitLabel(l2);
      v.visitInsn(ACONST_NULL);
      v.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Panel.class), "getComponent",
                        Type.getMethodDescriptor(Type.getType(UIComponent.class),
                                                 new Type[]{Type.getType(ComponentFinder.class), Type.getType(Class.class), Type.getType(String.class)}));
      v.visitTypeInsn(CHECKCAST, slashedComponentClassName);
      v.visitInsn(ARETURN);
      v.visitMaxs(3, 1);
    }
    {
      MethodVisitor v = cv.visitMethod(ACC_PUBLIC, methodName,
                                       "(" + Type.getDescriptor(ComponentMatcher.class) + ")L" + slashedComponentClassName + ";",
                                       null,
                                       new String[]{Type.getInternalName(ItemNotFoundException.class),
                                                    Type.getInternalName(ComponentAmbiguityException.class)});
      Label l0 = new Label();
      v.visitLabel(l0);
      v.visitVarInsn(ALOAD, 0);
      v.visitFieldInsn(GETFIELD, Type.getInternalName(Panel.class), "finder", Type.getDescriptor(ComponentFinder.class));
      v.visitVarInsn(ALOAD, 0);
      v.visitFieldInsn(GETSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      Label l1 = new Label();
      v.visitJumpInsn(IFNONNULL, l1);
      v.visitLdcInsn(componentClassName);
      v.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Panel.class), "getClass",
                        Type.getMethodDescriptor(Type.getType(Class.class), new Type[]{Type.getType(String.class)}));
      v.visitInsn(DUP);
      v.visitFieldInsn(PUTSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      Label l2 = new Label();
      v.visitJumpInsn(GOTO, l2);
      v.visitLabel(l1);
      v.visitFieldInsn(GETSTATIC, Type.getInternalName(Panel.class), transformedComponentClassName, Type.getDescriptor(Class.class));
      v.visitLabel(l2);
      v.visitVarInsn(ALOAD, 1);
      v.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Panel.class), "getMatcherByClass",
                        Type.getMethodDescriptor(Type.getType(ComponentMatcher.class), new Type[]{Type.getType(Class.class), Type.getType(ComponentMatcher.class)}));
      v.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Panel.class), "getComponent",
                        Type.getMethodDescriptor(Type.getType(UIComponent.class), new Type[]{Type.getType(ComponentFinder.class), Type.getType(ComponentMatcher.class)}));
      v.visitTypeInsn(CHECKCAST, slashedComponentClassName);
      v.visitInsn(ARETURN);
      Label l3 = new Label();
      v.visitLabel(l3);
      v.visitLineNumber(205, l0);
      v.visitLocalVariable("this", Type.getDescriptor(Panel.class), null, l0, l3, 0);
      v.visitLocalVariable("matcher", Type.getDescriptor(ComponentMatcher.class), null, l0, l3, 1);
      v.visitMaxs(4, 2);
    }
  }

  private String transformClassName(String componentClassName) {
    return "class$" + componentClassName.replace('.', '$');
  }
}