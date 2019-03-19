package com.keymao.content.service.impl;

import com.keymao.common.pojo.EasyUITreeNode;
import com.keymao.common.utils.E3Result;
import com.keymao.content.service.ContentCategoryService;
import com.keymao.mapper.TbContentCategoryMapper;
import com.keymao.pojo.TbContentCategory;
import com.keymao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //// 1、取查询参数id，parentId
        // 2、根据parentId查询tb_content_category，查询子节点列表。
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        // 3、得到List<TbContentCategory>
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
        // 4、把列表转换成List<EasyUITreeNode>ub
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory tbContentCategory : list){
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(tbContentCategory.getId());
            easyUITreeNode.setText(tbContentCategory.getName());
            easyUITreeNode.setState(tbContentCategory.getIsParent() ? "closed" : "open");
            //添加到列表
            resultList.add(easyUITreeNode);
        }
        return resultList;
    }

    @Override
    public E3Result addContentCategory(long parentId, String name) {
        //1、创建表对应pojo对象
        TbContentCategory tbContentCategory = new TbContentCategory();
        //2、设置pojo属性
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        //1正常，2删除
        tbContentCategory.setStatus(1);
        //默认排序1
        tbContentCategory.setSortOrder(1);
        //新添加节点一定是叶子节点
        tbContentCategory.setIsParent(false);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        //3、插入数据库
        tbContentCategoryMapper.insert(tbContentCategory);
        //4、判断父节点的isParent属性，如果不是true改成true
        //根据parentId查询父节点
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()){
            parent.setIsParent(true);
            //更新到数据库中
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }
        //5、返回结果，返回E3result，包含pojo
        return E3Result.ok(tbContentCategory);
    }

    @Override
    public E3Result updateContentCategory(long id, String name) {
        //1、创建表对应pojo对象
        TbContentCategory tbContentCategory = new TbContentCategory();
        //2、设置pojo属性
        tbContentCategory.setId(id);
        tbContentCategory.setName(name);
        tbContentCategory.setUpdated(new Date());
        int flag = tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
 //System.out.println("更新--" + flag);
        Map<String,String> map = new  HashMap<>();
        map.put("flag",String.valueOf(flag));
        return E3Result.ok(map);
    }

    @Override
    public E3Result deleteContentCategory(long id) {
        Map<String,String> map = new  HashMap<>();
        //判断是否是父节点
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        if(tbContentCategory.getIsParent()){
            //定义201为删除失败
            return E3Result.build(201,"父节点不能删除，请先删除其下子节点！");
        }
        int flag = tbContentCategoryMapper.deleteByPrimaryKey(id);
        System.out.println("删除--" + flag);
        //判断父节点下是否还有子节点，如果没有需要把父节点的isparent改为false
        Long parentId = tbContentCategory.getParentId();
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        int i = tbContentCategoryMapper.countByExample(example);
        if (i == 0){
            TbContentCategory parentBean = new TbContentCategory();
            parentBean.setIsParent(false);
            parentBean.setId(parentId);
            int updateflag = tbContentCategoryMapper.updateByPrimaryKeySelective(parentBean);
            System.out.println("更新--" + updateflag);
        }
        map.put("flag",String.valueOf(flag));
        return E3Result.ok(map);
    }
}
