package com.fariy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fariy.blog.dao.dos.Archives;
import com.fariy.blog.dao.mapper.ArticleMapper;
import com.fariy.blog.dao.mapper.TagMapper;
import com.fariy.blog.dao.pojo.Article;
import com.fariy.blog.dao.pojo.SysUser;
import com.fariy.blog.service.ArticleService;
import com.fariy.blog.service.SysUserService;
import com.fariy.blog.service.TagService;
import com.fariy.blog.vo.ArticleVo;
import com.fariy.blog.vo.Result;
import com.fariy.blog.vo.params.PageParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result listArticle(PageParams pageParams) {
        /**
         * 分页查询Article数据库表
         */
        Page<Article> page = new Page<Article>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //置顶进行排序
        //order by create_date desc 创建时间排序
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        //分页
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        //获取每个article
        List<Article> records = articlePage.getRecords();
        //把article数据传到vo
        List<ArticleVo> articleVoList = coptlist(records,true,true);

        return Result.success(articleVoList);
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        //select id,title from article order by viewcounts desc limit #{limit}
        List<Article> articles = articleMapper.selectList(queryWrapper);
        System.out.println("阿：" + articles);
        return Result.success(coptlist(articles,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.orderByAsc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        //select id,title from ms_article order by getCreate desc limit #{limit}
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(coptlist(articles,false,false));
    }

    @Override
    public Result listArchives(int limit) {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    /**
     * 把article列表复制对象属性articleVo列表
     * @param records
     * @param isTag
     * @param isAuthor
     * @return
     */
    private List<ArticleVo> coptlist(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor));
        }
        return articleVoList;
    }

    /**
     * 把Article对象属性赋值给ArticleVo
     * @param article
     * @param isTag
     * @param isAuthor
     * @return
     */
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

        //并不是所有接口都需要标签与作者

        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }

        return articleVo;
    }
}
