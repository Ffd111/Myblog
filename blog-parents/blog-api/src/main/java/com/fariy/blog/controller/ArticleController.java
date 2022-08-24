package com.fariy.blog.controller;

import com.fariy.blog.service.ArticleService;
import com.fariy.blog.vo.Result;
import com.fariy.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//json数据交互
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    public ArticleService articleService;

    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping()
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    /**
     * 最热文章
     * @return
     */
    @PostMapping("/hot")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 最新文章
     */
    @PostMapping("/new")
    public Result newArticles(){
        int limit = 5;
        return articleService.newArticles(limit);
    }

    @PostMapping("/listArchives")
    public Result listArchives(){
        int limit = 5;
        return articleService.listArchives(limit);
    }
}
