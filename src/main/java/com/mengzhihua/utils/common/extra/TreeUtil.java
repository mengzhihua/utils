package com.mengzhihua.utils.common.extra;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.mengzhihua.utils.common.lang.AssertUtil;
import com.mengzhihua.utils.common.lang.CollectionUtil;

/**
 * Builds parent/child trees from a flat list.
 */
public final class TreeUtil {

    private TreeUtil() {
    }

    public static <T, I> List<T> build(
            List<T> nodes,
            Function<T, I> idGetter,
            Function<T, I> parentIdGetter,
            BiConsumer<T, List<T>> childrenSetter,
            I rootParentId) {
        if (CollectionUtil.isEmpty(nodes)) {
            return new ArrayList<>();
        }
        AssertUtil.notNull(idGetter, "idGetter must not be null");
        AssertUtil.notNull(parentIdGetter, "parentIdGetter must not be null");
        AssertUtil.notNull(childrenSetter, "childrenSetter must not be null");

        Map<I, T> nodeMap = new LinkedHashMap<>();
        Map<I, List<T>> childrenMap = new LinkedHashMap<>();
        for (T node : nodes) {
            if (node == null) {
                continue;
            }
            I id = idGetter.apply(node);
            nodeMap.put(id, node);
            childrenMap.computeIfAbsent(id, key -> new ArrayList<>());
        }

        List<T> roots = new ArrayList<>();
        for (T node : nodeMap.values()) {
            I parentId = parentIdGetter.apply(node);
            List<T> children = childrenMap.getOrDefault(idGetter.apply(node), new ArrayList<>());
            childrenSetter.accept(node, children);
            if (isRoot(parentId, rootParentId) || !nodeMap.containsKey(parentId)) {
                roots.add(node);
            } else {
                childrenMap.computeIfAbsent(parentId, key -> new ArrayList<>()).add(node);
            }
        }
        return roots;
    }

    public static List<TreeNode<Long>> build(List<TreeNode<Long>> nodes, Long rootParentId) {
        return build(nodes, TreeNode::getId, TreeNode::getParentId, TreeNode::setChildren, rootParentId);
    }

    private static <I> boolean isRoot(I parentId, I rootParentId) {
        if (parentId == null) {
            return true;
        }
        return Objects.equals(parentId, rootParentId);
    }
}
