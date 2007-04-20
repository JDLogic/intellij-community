package com.intellij.lang.ant.psi.impl;

import com.intellij.lang.ant.psi.*;
import com.intellij.lang.ant.psi.introspection.AntTypeDefinition;
import com.intellij.psi.PsiLock;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.StringBuilderSpinAllocator;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AntCallImpl extends AntTaskImpl implements AntCall {

  private AntTarget[] myDependsTargets = null;
  private AntProperty[] myParams = null;

  public AntCallImpl(final AntElement parent, final XmlTag sourceElement, final AntTypeDefinition definition) {
    super(parent, sourceElement, definition);
  }

  public void acceptAntElementVisitor(@NotNull final AntElementVisitor visitor) {
    visitor.visitAntCall(this);
  }

  public String toString() {
    @NonNls final StringBuilder builder = StringBuilderSpinAllocator.alloc();
    try {
      builder.append("AntCall to ");
      final AntTarget target = getTarget();
      builder.append((target == null) ? "null" : target.toString());
      return builder.toString();
    }
    finally {
      StringBuilderSpinAllocator.dispose(builder);
    }
  }

  @Nullable
  public AntTarget getTarget() {
    synchronized (PsiLock.LOCK) {
      final String target = getSourceElement().getAttributeValue("target");
      final AntTarget result = (target == null) ? null : getAntProject().getTarget(target);
      if (result != null) {
        result.setDependsTargets(getDependsTargets());
      }
      return result;
    }
  }

  @NotNull
  public AntProperty[] getParams() {
    synchronized (PsiLock.LOCK) {
      if (myParams == null) {
        final List<AntProperty> properties = new ArrayList<AntProperty>();
        for (AntElement element : getChildren()) {
          if (element instanceof AntProperty) {
            properties.add((AntProperty)element);
          }
        }
        myParams = properties.toArray(new AntProperty[properties.size()]);
      }
      return myParams;
    }
  }

  public void clearCaches() {
    synchronized (PsiLock.LOCK) {
      super.clearCaches();
      myDependsTargets = null;
    }
  }

  @NotNull
  private AntTarget[] getDependsTargets() {
    synchronized (PsiLock.LOCK) {
      if (myDependsTargets == null) {
        final List<AntTarget> targets = new ArrayList<AntTarget>();
        for (AntElement element : getChildren()) {
          if (element instanceof AntTarget) {
            targets.add((AntTarget)element);
          }
        }
        myDependsTargets = targets.toArray(new AntTarget[targets.size()]);
      }
      return myDependsTargets;
    }
  }
}
