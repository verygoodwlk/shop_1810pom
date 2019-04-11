package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    /**
     * 根据关键字搜索信息
     * @return
     */
    @RequestMapping("/searchByKeyWord")
    public String searchByKeyWord(String keyword, Model model){
        List<Goods> goods = searchService.searchGoods(keyword);
        model.addAttribute("goods", goods);
        return "searchlist";
    }
}
