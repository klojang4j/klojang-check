package nl.naturalis.common.collection;

import nl.naturalis.check.Check;
import nl.naturalis.common.x.Param;

import java.util.*;

import static nl.naturalis.check.CommonChecks.in;
import static nl.naturalis.common.ClassMethods.isSubtype;
import static nl.naturalis.common.ClassMethods.isSupertype;
import static nl.naturalis.common.ObjectMethods.ifEmpty;
import static nl.naturalis.common.collection.DuplicateValueException.ValueType.KEY;
import static nl.naturalis.common.collection.TypeNode.NO_SUBTYPES;

final class TypeGraphBuilder<V> implements TypeMapBuilder<V> {

  // ================================================================== //
  // ======================= [ WritableTypeNode ] ===================== //
  // ================================================================== //

  private static class WritableTypeNode {

    private final WiredList<WritableTypeNode> subclasses = new WiredList<>();
    private final WiredList<WritableTypeNode> extensions = new WiredList<>();

    private final Class<?> type;
    private Object value;

    WritableTypeNode(Class<?> type, Object val) {
      this.type = type;
      this.value = val;
    }

    TypeNode toTypeNode() {
      var subclasses = this.subclasses.stream()
          .map(WritableTypeNode::toTypeNode)
          .toArray(TypeNode[]::new);
      if (subclasses.length == 0) {
        subclasses = NO_SUBTYPES;
      }
      var extensions = this.extensions.stream()
          .map(WritableTypeNode::toTypeNode)
          .toArray(TypeNode[]::new);
      if (extensions.length == 0) {
        extensions = NO_SUBTYPES;
      }
      return new TypeNode(type, value, subclasses, extensions);
    }

    void addChild(WritableTypeNode node) {
      if (node.type.isInterface()) {
        addInterface(node);
      } else {
        addClass(node);
      }
    }

    void addClass(WritableTypeNode node) {
      for (var itr = subclasses.wiredIterator(); itr.hasNext(); ) {
        var child = itr.next();
        if (isSupertype(node.type, child.type)) {
          itr.remove();
          node.addClass(child);
        } else if (isSubtype(node.type, child.type)) {
          child.addClass(node);
          return;
        }
      }
      for (var itr = extensions.wiredIterator(); itr.hasNext(); ) {
        var child = itr.next();
        if (isSubtype(node.type, child.type)) {
          child.addClass(node);
          return;
        }
      }
      subclasses.add(node);
    }

    void addInterface(WritableTypeNode node) {
      for (var itr = subclasses.wiredIterator(); itr.hasNext(); ) {
        var child = itr.next();
        if (isSupertype(node.type, child.type)) {
          itr.remove();
          node.addClass(child);
        }
      }
      for (var itr = extensions.wiredIterator(); itr.hasNext(); ) {
        var child = itr.next();
        if (isSupertype(node.type, child.type)) {
          itr.remove();
          node.addInterface(child);
        } else if (isSubtype(node.type, child.type)) {
          child.addInterface(node);
          return;
        }
      }
      extensions.add(node);
    }

  }

  // ================================================================== //
  // ===================== [ TypeGraphMapBuilder ] ==================== //
  // ================================================================== //

  private final Set<Class<?>> all = new HashSet<>();
  private final List<WritableTypeNode> classes = new ArrayList<>();
  private final List<WritableTypeNode> interfaces = new ArrayList<>();
  private final WritableTypeNode root;

  private int size;
  private boolean autobox = true;

  TypeGraphBuilder() {
    this.root = new WritableTypeNode(Object.class, null);
  }

  @Override
  public TypeGraphBuilder<V> autobox(boolean autobox) {
    this.autobox = autobox;
    return this;
  }

  @Override
  public TypeGraphBuilder<V> add(Class<?> type, V value) {
    Check.notNull(type, Param.TYPE)
        .isNot(in(), all, () -> new DuplicateValueException(KEY, type));
    Check.notNull(value, Param.VALUE);
    if (type == Object.class) {
      root.value = value;
    } else if (type.isInterface()) {
      interfaces.add(new WritableTypeNode(type, value));
    } else {
      classes.add(new WritableTypeNode(type, value));
    }
    all.add(type);
    return this;
  }

  @Override
  public TypeGraphBuilder<V> addMultiple(V value, Class<?>... types) {
    Check.notNull(types, "types");
    Arrays.stream(types).forEach(t -> add(t, value));
    return this;
  }

  @Override
  public TypeGraph<V> freeze() {
    for (var node : classes) {
      root.addClass(node);
    }
    for (var node : interfaces) {
      root.addInterface(node);
    }
    return new TypeGraph<>(root.toTypeNode(), all.size(), autobox);
  }

}
