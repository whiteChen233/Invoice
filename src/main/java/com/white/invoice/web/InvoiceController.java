package com.white.invoice.web;


import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.white.invoice.core.R;
import com.white.invoice.model.Invoice;
import com.white.invoice.model.Item;
import com.white.invoice.utils.PdfUtil;

@Controller
public class InvoiceController {

    @GetMapping("")
    public String index(Model model) {
        List<Item> options = new ArrayList<>();
        for (int i = 0; i < 4; i ++) {
            options.add(new Item("Option" + i, "Code" + i, 1, "Norm" + i, (i + 1) * 10.0));
        }
        model.addAttribute("options", options);
        model.addAttribute("date", DateTimeFormatter.ofPattern(" yyyy 年 MM 月 dd 日").format(LocalDate.now()));
        model.addAttribute("invoice", Invoice.Builder.build4Init());
        return "invoice";
    }

    @PostMapping("save")
    @ResponseBody
    public R save(@RequestBody @Valid Invoice invoice) {
        File file = PdfUtil.get().createFile();
        boolean result = PdfUtil.get().generatePdf(file, invoice);
        if (result) {
            JSONObject json = new JSONObject();
            json.put("url", file.getPath());
            R r = R.ok();
            r.setData(json);
            return r;
        }
        return R.error().message("生成失败!");
    }
}
