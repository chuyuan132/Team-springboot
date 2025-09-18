package com.slice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slice.dao.tag.TagCreateRequest;
import com.slice.dao.tag.TagUpdateRequest;
import com.slice.entity.Tag;


public interface TagService extends IService<Tag> {

    /**
     * 新增标签
     * @param tagCreateRequest
     */
    void tagCreate(TagCreateRequest tagCreateRequest);


    /**
     * 更新标签
     * @param tagUpdateRequest
     */
    void tagUpdate(TagUpdateRequest tagUpdateRequest);


}
