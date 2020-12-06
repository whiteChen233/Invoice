package com.white.invoice.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Payee {

    @NotBlank(message = "收款人全称不能为空!")
    private String name;

    @NotBlank(message = "收款人账号不能为空!")
    private String account;

    @NotBlank(message = "收款人开户银行不能为空!")
    private String bank;

}
