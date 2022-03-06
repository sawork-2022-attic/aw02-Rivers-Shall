package com.example.poshell.cli;

import com.example.poshell.biz.PosService;
import com.example.poshell.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class PosCommand {

    private PosService posService;

    private String checkNullCart() {
        if (posService.getCart() == null) {
            return "No cart. Please create cart with cmd 'n'";
        }
        return "";
    }

    @Autowired
    public void setPosService(PosService posService) {
        this.posService = posService;
    }

    @ShellMethod(value = "List Products", key = "p")
    public String products() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (Product product : posService.products()) {
            stringBuilder.append("\t").append(++i).append("\t").append(product).append("\n");
        }
        return stringBuilder.toString();
    }

    @ShellMethod(value = "New Cart", key = "n")
    public String newCart() {
        return posService.newCart() + " OK";
    }

    @ShellMethod(value = "Add a Product to Cart", key = "a")
    public String addToCart(String productId, int amount) {
        if (amount <= 0) {
            return "Only positive amount is allowed";
        }
        if (checkNullCart().length() > 0) {
            return checkNullCart();
        }
        if (posService.add(productId, amount)) {
            return posService.getCart().toString();
        }
        return "ERROR";
    }

    @ShellMethod(value = "Checkout", key = "c")
    public String checkout() {
        if (checkNullCart().length() > 0) {
            return checkNullCart();
        }
        double total = posService.checkout(posService.getCart());
        return "Total is " + total + "\n" +
                "Empty Cart";
    }

    @ShellMethod(value = "Modify Item Amount in Cart", key = "m")
    public String modify(String productId, int amount) {
        if (amount < 0) {
            return "Only non-negative amount is allowed";
        }
        if (checkNullCart().length() > 0) {
            return checkNullCart();
        }
        if (posService.modify(productId, amount)) {
            return posService.getCart().toString();
        }
        return "ERROR";
    }

    @ShellMethod(value = "List Items in Cart", key = "l")
    public String list() {
        if (checkNullCart().length() > 0) {
            return checkNullCart();
        }
        return posService.getCart().toString();
    }

    @ShellMethod(value = "Remove Product in Cart", key = "r")
    public String remove(String productId) {
        if (checkNullCart().length() > 0) {
            return checkNullCart();
        }
        posService.remove(productId);
        return posService.getCart().toString();
    }
}
