package com.slice.controller;

import com.slice.common.BaseResponse;
import com.slice.dao.tag.TagCreateRequest;
import com.slice.dao.tag.TagUpdateRequest;
import com.slice.service.TagService;
import com.slice.utils.ResultUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
public class TagController {


    @Resource
    private TagService tagService;

    @PostMapping("/create")
    public BaseResponse<?> tagCreate(@RequestBody TagCreateRequest tagCreateRequest) {
        tagService.tagCreate(tagCreateRequest);
        return ResultUtils.success(null);
    }

    @PostMapping("/update")
    public BaseResponse<?> tagUpdate(@RequestBody TagUpdateRequest tagUpdateRequest) {
        tagService.tagUpdate(tagUpdateRequest);
        return ResultUtils.success(null);
    }
}
