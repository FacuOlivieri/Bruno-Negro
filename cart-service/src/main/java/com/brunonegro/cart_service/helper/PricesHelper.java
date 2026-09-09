package com.brunonegro.cart_service.helper;

import com.brunonegro.cart_service.dto.ProductDTO;
import com.brunonegro.cart_service.dto.ProductDetailDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class PricesHelper {

    private static final int PRICE_SCALE = 2;

    private PricesHelper() {
    }

    //Precio unitario final: los tres descuentos encadenados, cada uno sobre lo que quedo del anterior
    public static double calculatePriceWithDescount(ProductDTO product) {
        double price = product.getUnitPrice();

        price = applyDescount(price, product.getFirstDescount());
        price = applyDescount(price, product.getSecondDescount());
        price = applyDescount(price, product.getThirdDescount());

        return round(price);
    }

    //Descuenta un porcentaje del precio; si el descuento no es valido devuelve el precio intacto
    private static double applyDescount(double price, float descount) {
        if (descount <= 0 || descount > 100) {
            return price;
        }
        return price * (1 - descount / 100);
    }

    //Subtotal de una linea: precio unitario final por cantidad
    public static double calculateSubtotal(double unitPrice, int quantity) {
        return round(unitPrice * quantity);
    }

    //Total del carrito: suma de los subtotales de todas las lineas
    public static double calculateTotal(List<ProductDetailDTO> productList) {
        double total = productList.stream()
                .mapToDouble(ProductDetailDTO::getSubtotal)
                .sum();

        return round(total);
    }

    //Redondea un importe a 2 decimales (HALF_UP, o sea 0.005 sube a 0.01)
    public static double round(double amount) {
        return BigDecimal.valueOf(amount)
                .setScale(PRICE_SCALE, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
