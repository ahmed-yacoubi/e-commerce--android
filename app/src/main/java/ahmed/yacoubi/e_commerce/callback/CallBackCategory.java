package ahmed.yacoubi.e_commerce.callback;

import java.util.List;

import ahmed.yacoubi.e_commerce.model.Category;

public interface CallBackCategory {
    void getCategories(List<Category> categoryList);
}
