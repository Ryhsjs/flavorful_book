package ru.itis.flavorful_book.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.itis.flavorful_book.repository.*;
import ru.itis.flavorful_book.service.*;
import ru.itis.flavorful_book.util.jdbc.SimpleDataSource;
import ru.itis.flavorful_book.util.validation.Validator;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class InitContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Проблема с загрузкой драйверов базы данных");
        }
        Properties properties = new Properties();
        InputStream inputStream = getClass().getResourceAsStream("/application.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Проблема с настройками проекта");
        }
        String url = properties.getProperty("url");

        DataSource dataSource = new SimpleDataSource(url, properties);


        UserRepository userRepository = new UserRepositoryJDBC(dataSource);
        SessionRepository sessionRepository = new SessionRepositoryJDBC(dataSource);
        RecipeRepository recipeRepository = new RecipeRepositoryJDBC(dataSource);
        CategoryRepository categoryRepository = new CategoryRepositoryJDBC(dataSource);
        IngredientRepository ingredientRepository = new IngredientRepositoryJDBC(dataSource);
        IngredientRecipeRepository ingredientRecipeRepository = new IngredientRecipeRepositoryJDBC(dataSource);
        ReviewRepository reviewRepository = new ReviewRepositoryJDBC(dataSource);

        Validator validator = new Validator(userRepository, recipeRepository);

        ImageService imageService = new ImageServiceImpl(
                sce.getServletContext().getRealPath(""),
                "C:/Users/User/IdeaProjects/flavorful_book/src/main/webapp"
        );
        SecurityService securityService = new SecurityServiceImpl(userRepository, sessionRepository, validator);
        RecipeService recipeService = new RecipeServiceImpl(recipeRepository, userRepository, categoryRepository,
                ingredientRecipeRepository, validator);
        CategoryService categoryService = new CategoryServiceImpl(categoryRepository, validator);
        IngredientService ingredientService = new IngredientServiceImpl(ingredientRepository);
        IngredientRecipeService ingredientRecipeService = new IngredientRecipeServiceImpl(
                ingredientRecipeRepository, validator);
        ReviewService reviewService = new ReviewServiceImpl(reviewRepository, userRepository, validator);
        UserService userService = new UserServiceImpl(userRepository, validator);

        sce.getServletContext().setAttribute("securityService", securityService);
        sce.getServletContext().setAttribute("recipeService", recipeService);
        sce.getServletContext().setAttribute("categoryService", categoryService);
        sce.getServletContext().setAttribute("ingredientService", ingredientService);
        sce.getServletContext().setAttribute("ingredientRecipeService", ingredientRecipeService);
        sce.getServletContext().setAttribute("reviewService", reviewService);
        sce.getServletContext().setAttribute("userService", userService);
        sce.getServletContext().setAttribute("imageService", imageService);
    }
}
