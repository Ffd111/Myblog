package com.fariy.blog.service;

import com.fariy.blog.vo.Result;
import com.fariy.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    /**
     * 通过article ID找到对应的Tag
     */
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);
}
