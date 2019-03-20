package com.keymao.content.service;

import com.keymao.common.pojo.EasyUIDataGridResult;
import com.keymao.common.utils.E3Result;
import com.keymao.pojo.TbContent;

import java.util.List;

public interface ContentService {
    /**
     * 新增内容
     * @param content
     * @return
     */
    public E3Result addContent(TbContent content);

    /**
     * 根据分类Id查询分页列表
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    public EasyUIDataGridResult getContentList(long categoryId, int page, int rows);

    /**
     * 根据分类Id查询列表
     * @param categoryId
     * @return
     */
    public List<TbContent> getContentListByCid(long categoryId);

    /**
     * 更新内容
     * @param content
     * @return
     */
    public E3Result updateContent(TbContent content);

    /**
     * 批量删除内容
     * @param ids 内容id，以英文逗号分割
     * @return
     */
    public E3Result deleteContents(String ids);
}
