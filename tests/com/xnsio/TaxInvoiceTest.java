package com.xnsio;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.xnsio.ProductCategory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaxInvoiceTest {
    private static final int BASIC_TAX_RATE = 10;
    private static final int IMPORT_DUTY_RATE = 5;
    private TaxInvoice taxInvoice;
    private String receipt;
    private final Set<ProductCategory> taxableProductCategories = Set.of(ENTERTAINMENT, COSMETICS);

    @Test
    void receiptForNonImportedNonTaxableProduct() {
        String expectedReceipt = "1 Book: 124.99\n" + "Tax: 0.0\n" + "Total: 124.99";
        taxInvoiceWithBasicTaxAndImportDuty()
                .calculateTaxFor(List.of(new Product("Book", 1, 124.99, BOOKS, false)))
                .verifyReceiptWith(expectedReceipt);
    }

    @Test
    void receiptForNonImportedWithTaxableProduct() {
        String expectedReceipt = "1 Music CD: 164.99\nTax: 15.0\nTotal: 164.99";
        taxInvoiceWithBasicTaxAndImportDuty()
                .calculateTaxFor(List.of(new Product("Music CD", 1, 149.99, ENTERTAINMENT, false)))
                .verifyReceiptWith(expectedReceipt);
    }

    @Test
    void receiptForNonImportedMixedTaxableProducts() {
        List<Product> products = List.of(new Product("Book", 1, 124.99, BOOKS, false),
                new Product("Music CD", 1, 149.99, ENTERTAINMENT, false),
                new Product("Chocolate bar", 1, 40.85, FOOD, false));
        String expectedReceipt = "1 Book: 124.99\n1 Music CD: 164.99\n1 Chocolate bar: 40.85\nTax: 15.0\nTotal: 330.83";
        taxInvoiceWithBasicTaxAndImportDuty().calculateTaxFor(products).verifyReceiptWith(expectedReceipt);
    }

    @Test
    void receiptForImportedMixedTaxableProducts() {
        List<Product> products = List.of(new Product("Box of chocolate", 1, 100.0, FOOD, true),
                new Product("Bottle of perfume", 1, 470.50, COSMETICS, true));
        String expectedReceipt = "1 Imported Box of chocolate: 105.0\n1 Imported Bottle of perfume: 541.08\n" +
                "Tax: 75.58\nTotal: 646.08";
        taxInvoiceWithBasicTaxAndImportDuty().calculateTaxFor(products).verifyReceiptWith(expectedReceipt);
    }

    @Test
    void receiptForMixedProducts() {
        List<Product> products = List.of(new Product("Bottle of perfume", 1, 270.99, COSMETICS, true),
                new Product("Bottle of perfume", 1, 180.99, COSMETICS, false),
                new Product("Packet of headache pills", 1, 19.75, MEDICAL, false),
                new Product("Box of chocolate", 1, 210.25, FOOD, true));
        String expectedReceipt = "1 Imported Bottle of perfume: 311.64\n1 Bottle of perfume: 199.09\n" +
                "1 Packet of headache pills: 19.75\n1 Imported Box of chocolate: 220.76\n" +
                "Tax: 69.26\nTotal: 751.24";
        taxInvoiceWithBasicTaxAndImportDuty().calculateTaxFor(products).verifyReceiptWith(expectedReceipt);
    }

    @Test
    void receiptForProductsWithMultipleQuantity() {
        List<Product> products = List.of(
                new Product("Bottle of perfume", 2, 180.99, COSMETICS, false),
                new Product("Box of chocolate", 3, 210.25, FOOD, true));
        String expectedReceipt = "2 Bottle of perfume: 398.18\n" +
                "3 Imported Box of chocolate: 662.29\nTax: 67.74\nTotal: 1060.47";
        taxInvoiceWithBasicTaxAndImportDuty().calculateTaxFor(products).verifyReceiptWith(expectedReceipt);
    }

    private void verifyReceiptWith(String expectedReceipt) {
        assertEquals(expectedReceipt, receipt);
    }

    private TaxInvoiceTest calculateTaxFor(List<Product> product) {
        receipt = taxInvoice.generateTaxReceipt(product);
        return this;
    }

    private TaxInvoiceTest taxInvoiceWithBasicTaxAndImportDuty() {
        taxInvoice = new TaxInvoice(BASIC_TAX_RATE, IMPORT_DUTY_RATE, taxableProductCategories);
        return this;
    }
}
