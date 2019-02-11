package com.light.lgts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.light.lgts.dao.ArticleDao;
import com.light.lgts.service.ArticleService;
import com.light.lgts.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: hxy
 * @description:
 * @date: 2017/10/24 16:07
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    /**
     * 新增文章
     *
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addArticle(JSONObject jsonObject) {
        articleDao.addArticle(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 文章列表
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject listArticle(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = articleDao.countArticle(jsonObject);
        List<JSONObject> list = articleDao.listArticle(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 更新文章
     *
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateArticle(JSONObject jsonObject) {
        articleDao.updateArticle(jsonObject);
        return CommonUtil.successJson();
    }
}
