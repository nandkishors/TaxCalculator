package com.xnsio;

import java.util.List;
import java.util.Set;

public class TaxInvoice {
    private final double basicTaxRate;
    private final double importDutyRate;
    private Set<ProductCategory> taxableProductCategories;

    public TaxInvoice(double basicTaxRate, double importDutyRate, Set<ProductCategory> taxableProductCategories) {
        this.basicTaxRate = basicTaxRate;
        this.importDutyRate = importDutyRate;
        this.taxableProductCategories = taxableProductCategories;
    }

    public String generateTaxReceipt(List<Product> products) {
        StringBuilder receipt = new StringBuilder();
        double totalTax = 0.0;
        double totalCost = 0.0;
        for (Product product : products) {
            totalTax += product.calculateBasicTaxWith(basicTaxRate, importDutyRate, taxableProductCategories);
            totalCost += product.getTotalCost();
            receipt.append(product.generateCostReceipt());
        }
        receipt.append("Tax: ").append(getRoundedCost(totalTax)).append("\n").append("Total: ").append(getRoundedCost(totalCost));
        return receipt.toString();
    }

    private double getRoundedCost(double totalCost) {
        return Math.round(totalCost * 100.0) / 100.0;
    }
}
