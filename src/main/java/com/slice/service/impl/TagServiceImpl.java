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
import com.slice.vo.tag.TagQueryVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

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
        if(tagCreateRequest.getName() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        String name = tagCreateRequest.getName();
        Long parentId = tagCreateRequest.getParentId();
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
        if(tagUpdateRequest.getId() == null || tagUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tag tag = new Tag();
        tag.setId(tag.getId());
        tag.setName(tagUpdateRequest.getName());
        tag.setParentId(tag.getParentId());
        int i = tagMapper.updateById(tag);
        if(i <=0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
    }

    @Override
    public void tagDelete(Long id) {
        if(id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int i = tagMapper.deleteById(id);
        if(i <=0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
    }

    @Override
    public List<TagQueryVo> tagQueryList() {
        List<Tag> tags = tagMapper.selectList(new QueryWrapper<>());
        Map<Long, TagQueryVo> longTagQueryVoMap = new HashMap<>();
        for(Tag tag : tags) {
            TagQueryVo tagQueryVo = new TagQueryVo();
            tagQueryVo.setChildren(new ArrayList<>());
            tagQueryVo.setName(tag.getName());
            tagQueryVo.setParentId(tag.getParentId());
            longTagQueryVoMap.put(tag.getId(), tagQueryVo);
        }
        ArrayList<TagQueryVo> tagQueryVos = new ArrayList<>();
        for(Tag tag : tags) {
            if(tag.getParentId() == 0) {
                tagQueryVos.add(longTagQueryVoMap.get(tag.getId()));
            } else {
                TagQueryVo parent = longTagQueryVoMap.get(tag.getParentId());
                if(parent != null) {
                    parent.getChildren().add(longTagQueryVoMap.get(tag.getId()));
                }

            }
        }
        return tagQueryVos;
    }
}