package com.white.invoice.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Payer {

    @NotBlank(message = "付款人全称不能为空!")
    private String name;

    @NotBlank(message = "付款人账号不能为空!")
    private String account;

    @NotBlank(message = "付款人开户银行不能为空!")
    private String bank;


}
