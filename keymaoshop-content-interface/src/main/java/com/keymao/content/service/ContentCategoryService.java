package com.keymao.content.service;

import com.keymao.common.pojo.EasyUITreeNode;
import com.keymao.common.utils.E3Result;

import java.util.List;

/**
 * 内容分类接口
 */
public interface ContentCategoryService {
    /**
     * 根据父节点ID获取内容分类list
     * @param parentId 父节点ID
     * @return
     */
    public List<EasyUITreeNode> getContentCategoryList(long parentId);

    /**
     * 新增内容分类
     * @param parentId 父节点ID
     * @param name 内容分类名称
     * @return
     */
    public E3Result addContentCategory(long parentId,String name);

    /**
     * 更新内容分类
     * @param id 节点ID
     * @param name 内容分类名称
     * @return
     */
    public E3Result updateContentCategory(long id,String name);

    /**
     *删除内容分类
     * @param id
     * @return
     */
    public E3Result deleteContentCategory(long id);
}
