package ru.itis.flavorful_book.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.itis.flavorful_book.entity.Category;
import ru.itis.flavorful_book.entity.IngredientRecipe;
import ru.itis.flavorful_book.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Recipe> findWithFilters(
            List<Long> ingredientIds, boolean strictIngredients,
            List<Long> categoryIds, boolean strictCategories,
            int activeStart, int activeEnd,
            int totalStart, int totalEnd) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> root = cq.from(Recipe.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.between(root.get("activeCookingTime"), activeStart, activeEnd));
        predicates.add(cb.between(root.get("totalCookingTime"), totalStart, totalEnd));

        if (ingredientIds != null && !ingredientIds.isEmpty()) {
            if (strictIngredients) {
                Subquery<Long> sub = cq.subquery(Long.class);
                Root<IngredientRecipe> subRoot = sub.from(IngredientRecipe.class);
                sub.select(cb.countDistinct(subRoot.get("ingredient").get("id")));
                sub.where(
                        cb.equal(subRoot.get("recipe"), root),
                        subRoot.get("ingredient").get("id").in(ingredientIds)
                );
                predicates.add(cb.equal(sub, (long) ingredientIds.size()));
            } else {
                Join<Recipe, IngredientRecipe> join = root.join("ingredientRecipes", JoinType.INNER);
                predicates.add(join.get("ingredient").get("id").in(ingredientIds));
                cq.distinct(true);
            }
        }

        if (categoryIds != null && !categoryIds.isEmpty()) {
            if (strictCategories) {
                Subquery<Long> sub = cq.subquery(Long.class);
                Root<Recipe> subRoot = sub.from(Recipe.class);
                Join<Recipe, Category> subJoin = subRoot.join("categories");
                sub.select(cb.countDistinct(subJoin.get("id")));
                sub.where(
                        cb.equal(subRoot, root),
                        subJoin.get("id").in(categoryIds)
                );
                predicates.add(cb.equal(sub, (long) categoryIds.size()));
            } else {
                Join<Recipe, Category> join = root.join("categories", JoinType.INNER);
                predicates.add(join.get("id").in(categoryIds));
                cq.distinct(true);
            }
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("createdAt")));

        return em.createQuery(cq).getResultList();
    }
}
