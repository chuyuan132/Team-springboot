package com.slice.controller;

import com.slice.common.BaseResponse;
import com.slice.dao.tag.TagCreateRequest;
import com.slice.dao.tag.TagUpdateRequest;
import com.slice.enums.ErrorCode;
import com.slice.exception.BusinessException;
import com.slice.service.TagService;
import com.slice.utils.ResultUtils;
import com.slice.vo.tag.TagQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@Tag(name="标签服务")
public class TagController {


    @Resource
    private TagService tagService;

    @PostMapping("/create")
    @Operation(summary = "新增标签")
    public BaseResponse<?> tagCreate(@RequestBody TagCreateRequest tagCreateRequest) {
        if(tagCreateRequest.getName() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        tagService.tagCreate(tagCreateRequest);
        return ResultUtils.success(null);
    }

    @PostMapping("/update")
    @Operation(summary = "更新标签")
    public BaseResponse<?> tagUpdate(@RequestBody TagUpdateRequest tagUpdateRequest) {
        if(tagUpdateRequest.getId() == null || tagUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        tagService.tagUpdate(tagUpdateRequest);
        return ResultUtils.success(null);
    }

    @GetMapping("/delete")
    @Operation(summary = "删除标签")
    public BaseResponse<?> tagDelete(@RequestParam Long id) {
        if(id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        tagService.tagDelete(id);
        return ResultUtils.success(null);
    }


    @GetMapping("/list")
    @Operation(summary = "查询标签")
    public BaseResponse<List<TagQueryVo>> tagList() {
        List<TagQueryVo> tagQueryVos = tagService.tagQueryList();
        return ResultUtils.success(tagQueryVos);
    }
}
