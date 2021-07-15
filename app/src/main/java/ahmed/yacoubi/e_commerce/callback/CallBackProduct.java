package ahmed.yacoubi.e_commerce.callback;

import java.util.List;

import ahmed.yacoubi.e_commerce.model.Product;

public interface CallBackProduct {
    void getProducts(List<Product> productList);
}
