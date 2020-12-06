package com.white.invoice.model;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @NotBlank
    private String value;

    @NotBlank
    private String code;

    @NotNull
    @Positive(message = "数量应为正数!")
    private Integer num;

    @NotBlank
    private String norm;

    @NotNull
    @PositiveOrZero(message = "金额不能是负数!")
    private Double amount;

    public String getNumStr() {
        return Optional.ofNullable(amount).map(String::valueOf).orElse(" ");
    }

    public String getAmountStr() {
        return Optional.ofNullable(amount).map(String::valueOf).orElse(" ");
    }
}
