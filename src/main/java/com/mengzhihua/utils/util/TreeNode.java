package com.mengzhihua.utils.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic tree node used by {@link TreeUtil}.
 */
public class TreeNode<I> {

    private I id;
    private I parentId;
    private String label;
    private Object extra;
    private List<TreeNode<I>> children = new ArrayList<>();

    public TreeNode() {
    }

    public TreeNode(I id, I parentId, String label) {
        this.id = id;
        this.parentId = parentId;
        this.label = label;
    }

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }

    public I getParentId() {
        return parentId;
    }

    public void setParentId(I parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public List<TreeNode<I>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<I>> children) {
        this.children = children == null ? new ArrayList<>() : children;
    }
}
