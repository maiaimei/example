package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.FileModel;
import cn.maiaimei.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PageController {
    @Autowired
    private FileService fileService;

    @GetMapping(value = {"", "/"})
    public ModelAndView list() {
        List<FileModel> list = fileService.list();
        Map<String, List<FileModel>> map = new HashMap<>();
        map.put("records", list);
        return new ModelAndView("index", map);
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }
}
