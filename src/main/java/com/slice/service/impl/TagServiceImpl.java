package com.slice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slice.dao.tag.TagCreateRequest;
import com.slice.dao.tag.TagUpdateRequest;
import com.slice.entity.Tag;
import com.slice.enums.ErrorCode;
import com.slice.exception.BusinessException;
import com.slice.mapper.TagMapper;
import com.slice.service.TagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author zhangchuyuan
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2025-09-04 17:18:29
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{


    @Resource
    private TagMapper tagMapper;

    @Override
    public void tagCreate(TagCreateRequest tagCreateRequest) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        String name = tagCreateRequest.getName();
        long parentId = tagCreateRequest.getParentId();
        tagQueryWrapper.eq("name", name);
        Tag tag = tagMapper.selectOne(tagQueryWrapper);
        if(tag != null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"标签已存在");
        }
        Tag newTag = new Tag();

        newTag.setName(name);
        newTag.setParentId(parentId);
        int insert = tagMapper.insert(newTag);
        if(insert <= 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"新增失败");
        }
    }

    @Override
    public void tagUpdate(TagUpdateRequest tagUpdateRequest) {
        Tag tag = new Tag();
        tag.setId(tag.getId());
        tag.setName(tagUpdateRequest.getName());
        tag.setParentId(tag.getParentId());
        int i = tagMapper.updateById(tag);
        if(i <=0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
    }
}