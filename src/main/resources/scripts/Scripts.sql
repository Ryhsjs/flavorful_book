CREATE TABLE IF NOT EXISTS users
(
    user_id       BIGSERIAL PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(512) NOT NULL,
    salt          VARCHAR(255) NOT NULL,
    avatar_url    VARCHAR(500),
    created_at    TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sessions
(
    session_id VARCHAR(128) PRIMARY KEY,
    user_id    BIGINT    NOT NULL,
    expire_at  TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS recipes
(
    recipe_id           BIGSERIAL PRIMARY KEY,
    title               VARCHAR(255) NOT NULL,
    description         TEXT,
    instructions        TEXT         NOT NULL,
    active_cooking_time INT          NOT NULL,
    total_cooking_time  INT          NOT NULL,
    servings            INT          NOT NULL,
    image_url           VARCHAR(500),
    created_at          TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ   DEFAULT NULL,
    user_id             BIGINT       NOT NULL,
    views               BIGINT        DEFAULT 0,
    rating              DECIMAL(3, 2) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS reviews
(
    review_id  BIGSERIAL PRIMARY KEY,
    user_id    BIGINT   NOT NULL,
    recipe_id  BIGINT   NOT NULL,
    rating     SMALLINT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment    TEXT     NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT NULL,

    UNIQUE (recipe_id, user_id)
);

CREATE TABLE IF NOT EXISTS favorites
(
    user_id   BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    saved_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, recipe_id)
);

CREATE TABLE IF NOT EXISTS ingredients
(
    ingredient_id BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id        BIGSERIAL PRIMARY KEY,
    name               VARCHAR(255) UNIQUE NOT NULL,
    description        TEXT,
    parent_category_id BIGINT
);

CREATE TABLE IF NOT EXISTS recipe_ingredients
(
    recipe_id     BIGINT         NOT NULL,
    ingredient_id BIGINT         NOT NULL,
    quantity      DECIMAL(10, 2) NOT NULL,
    unit          VARCHAR(50)    NOT NULL,
    notes         VARCHAR(255),

    PRIMARY KEY (recipe_id, ingredient_id)
);

CREATE TABLE IF NOT EXISTS recipe_categories
(
    recipe_id   BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    PRIMARY KEY (recipe_id, category_id)
);


ALTER TABLE sessions
    ADD CONSTRAINT fk_sessions_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE;

ALTER TABLE recipes
    ADD CONSTRAINT fk_recipes_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE;

ALTER TABLE reviews
    ADD CONSTRAINT fk_reviews_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE,

    ADD CONSTRAINT fk_reviews_recipe_id
        FOREIGN KEY (recipe_id)
            REFERENCES recipes (recipe_id)
            ON DELETE CASCADE;

ALTER TABLE favorites
    ADD CONSTRAINT fk_favorites_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE,

    ADD CONSTRAINT fk_favorites_recipe_id
        FOREIGN KEY (recipe_id)
            REFERENCES recipes (recipe_id)
            ON DELETE CASCADE;

ALTER TABLE categories
    ADD CONSTRAINT fk_categories_parent_category_id
        FOREIGN KEY (parent_category_id)
            REFERENCES categories (category_id)
            ON DELETE SET NULL;

ALTER TABLE recipe_ingredients
    ADD CONSTRAINT fk_recipe_ingredients_recipe_id
        FOREIGN KEY (recipe_id)
            REFERENCES recipes (recipe_id)
            ON DELETE CASCADE,

    ADD CONSTRAINT fk_recipe_ingredients_ingredient_id
        FOREIGN KEY (ingredient_id)
            REFERENCES ingredients (ingredient_id);

ALTER TABLE recipe_categories
    ADD CONSTRAINT fk_recipe_categories_recipe_id
        FOREIGN KEY (recipe_id)
            REFERENCES recipes (recipe_id)
            ON DELETE CASCADE,

    ADD CONSTRAINT fk_recipe_categories_category_id
        FOREIGN KEY (category_id)
            REFERENCES categories (category_id);

ALTER TABLE recipe_views
    ADD CONSTRAINT fk_recipe_views_recipe_id
        FOREIGN KEY (recipe_id)
            REFERENCES recipes (recipe_id)
            ON DELETE CASCADE;


CREATE FUNCTION update_recipe_rating() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF tg_op = 'INSERT' OR tg_op = 'UPDATE' OR tg_op = 'DELETE' THEN
        UPDATE recipes
        SET rating = (SELECT ROUND(AVG(rating)::NUMERIC, 2)
                      FROM reviews
                      WHERE recipe_id = COALESCE(new.recipe_id, old.recipe_id))
        WHERE recipe_id = COALESCE(new.recipe_id, old.recipe_id);
    END IF;
    RETURN COALESCE(new, old);
END;
$$;

ALTER FUNCTION update_recipe_rating() OWNER TO postgres;

CREATE TRIGGER trigger_update_rating
    AFTER INSERT OR UPDATE OR DELETE
    ON reviews
    FOR EACH ROW
EXECUTE FUNCTION update_recipe_rating();