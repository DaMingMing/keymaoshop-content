package com.keymao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.keymao.common.jedis.JedisClient;
import com.keymao.common.pojo.EasyUIDataGridResult;
import com.keymao.common.utils.E3Result;
import com.keymao.common.utils.JsonUtils;
import com.keymao.content.service.ContentService;
import com.keymao.mapper.TbContentMapper;
import com.keymao.pojo.TbContent;
import com.keymao.pojo.TbContentCategory;
import com.keymao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    @Override
    public E3Result addContent(TbContent content) {
        //补全属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入数据
        contentMapper.insert(content);
        //缓存同步，删除缓存中对应的数据
        jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
        return E3Result.ok();

    }

    @Override
    public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = contentMapper.selectByExample(example);
        //取分页信息
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);

        //创建返回结果对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) pageInfo.getTotal());
        result.setRows(list);
        System.out.println("contenlist=" + list.size());
        return result;
    }

    @Override
    public List<TbContent> getContentListByCid(long categoryId) {
        //查询缓存
        try {
            String json = jedisClient.hget(CONTENT_LIST, categoryId + "");
            if(StringUtils.isNotBlank(json)){
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return  list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //执行查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = contentMapper.selectByExample(example);
        //向缓存添加数据
        try {
            jedisClient.hset(CONTENT_LIST,categoryId + "",JsonUtils.objectToJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public E3Result updateContent(TbContent content) {
        //补全属性
        content.setUpdated(new Date());
        //插入数据
        int flag = contentMapper.updateByPrimaryKeySelective(content);
        if(flag != 1){
            return E3Result.build(201,"更新内容失败！");
        }
        return E3Result.ok();
    }

    @Override
    public E3Result deleteContents(String ids) {
        if(null == ids || "".equals(ids)) {
            return E3Result.build(500,"'删除失败，请选择内容！",null);
        }
        StringBuffer msg = null;
        String arrId[] = ids.split(",");
        Long id = 0l;
        int length = arrId.length;
        for(int i = 0;i < length;i++) {
            id = Long.parseLong(arrId[i]);
            contentMapper.deleteByPrimaryKey(id);
        }
        return E3Result.ok();
    }

}
