package com.example.ecommerce.enums;


import java.util.Arrays;

public enum ProductCategory {

    PRODUCT_CATEGORY_1(1, ""),
    PRODUCT_CATEGORY_2(2, ""),
    PRODUCT_CATEGORY_3(3, ""),
    PRODUCT_CATEGORY_4(4, "");

    private Integer code;
    private String description;

    ProductCategory(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {return code;}
    public String getDescription() {return description;}

    public static ProductCategory getProductCategory(Integer code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(ProductCategory.values()).toList().stream().filter(
                productCategory -> productCategory.getCode().equals(code)
        ).findFirst().orElse(null);
    }

}
