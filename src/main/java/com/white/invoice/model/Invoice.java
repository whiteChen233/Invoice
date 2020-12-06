package com.white.invoice.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Invoice {

    @NotNull(message = "付款人信息不能为空!")
    @Valid
    private Payer payer;

    @NotNull(message = "收款人信息不能为空!")
    @Valid
    private Payee payee;

    @NotEmpty
    @Valid
    private List<Item> items;

    @NotBlank(message = "金额(大写)不能为空!")
    private String amountUpper;

    @NotNull
    @PositiveOrZero(message = "金额不能是负数!")
    private double amountLower;

    public static class Builder {

        private Builder() {
        }

        public static Invoice build4Init() {
            Invoice i = new Invoice();
            i.setPayer(new Payer());
            i.setPayee(new Payee());
            List<Item> list = new ArrayList<>();
            Collections.addAll(list, new Item(), new Item(), new Item());
            i.setItems(list);
            i.setAmountLower(0);
            i.setAmountUpper("");
            return i;
        }

    }
}