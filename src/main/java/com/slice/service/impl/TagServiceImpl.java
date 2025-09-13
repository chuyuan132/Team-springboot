package com.slice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slice.entity.Tag;
import com.slice.mapper.TagMapper;
import com.slice.service.TagService;
import org.springframework.stereotype.Service;

/**
* @author zhangchuyuan
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2025-09-04 17:18:29
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




