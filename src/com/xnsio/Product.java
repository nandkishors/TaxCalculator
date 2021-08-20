package com.xnsio;

import java.util.Set;

public class Product {
    private final String name;
    private final int quantity;
    private final double cost;
    private final ProductCategory category;
    private final boolean isImported;
    private double totalCost;

    public Product(String name, int quantity, double cost, ProductCategory category, boolean isImported) {
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.category = category;
        this.isImported = isImported;
    }

    public double calculateBasicTaxWith(double basicTaxRate, double importDutyRate,
                                        Set<ProductCategory> taxableCategories) {
        double taxRate = applyTaxForTaxableProductCategory(basicTaxRate, taxableCategories);
        taxRate = applyTaxForImportedProduct(importDutyRate, taxRate);
        double totalBasicTax = roundedCost(calculateTotalBasicTax(taxRate));
        totalCost = (cost * quantity) + totalBasicTax;
        return totalBasicTax;
    }

    private double applyTaxForImportedProduct(double importDutyRate, double taxRate) {
        return isImported ? taxRate + importDutyRate : taxRate;
    }

    private double applyTaxForTaxableProductCategory(double basicTaxRate, Set<ProductCategory> taxableCategories) {
        return taxableCategories.contains(category) ? basicTaxRate : 0.0;
    }

    private double calculateTotalBasicTax(double taxRate) {
        return cost * (taxRate / 100) * quantity;
    }

    private double roundedCost(double tax) {
        return Math.round(tax * 100.0) / 100.0;
    }

    public String generateCostReceipt() {
        StringBuilder receipt = new StringBuilder().append(quantity).append(" ");
        if (isImported)
            receipt.append("Imported ");
        return receipt.append(name).append(": ").append(totalCost).append("\n").toString();
    }

    public double getTotalCost() {
        return totalCost;
    }
}
