-- Тестовый пользователь (пароль: test1234)
INSERT INTO users (username, email, password)
VALUES ('flavorful book', 'test@flavorful.ru', '$2a$10$p4lGryZp1h5d0C8oUW3TGuK3VNICep3hIff1ADfj6bcjDS.mq7aQS')
ON CONFLICT (username) DO NOTHING;

-- Тестовые рецепты от flavorful book.

DO $$
DECLARE
    v_author BIGINT;
    v_recipe BIGINT;
BEGIN
    SELECT id INTO v_author FROM users WHERE username = 'flavorful book';

    -- 1. Паста карбонара
    INSERT INTO recipes (title, description, instructions, active_cooking_time, total_cooking_time, servings, user_id)
    VALUES (
        'Паста карбонара',
        'Классическая итальянская паста с беконом и яичным соусом',
        E'1. Отварите макароны в подсоленной воде до состояния al dente.\n'
        '2. Обжарьте бекон на сухой сковороде до хрустящей корочки.\n'
        '3. Взбейте яйца с натёртым сыром и чёрным перцем.\n'
        '4. Смешайте горячие макароны с беконом, снимите с огня.\n'
        '5. Добавьте яичную смесь и быстро перемешайте — соус должен загустеть от тепла макарон, а не свернуться.\n'
        '6. Подавайте сразу, посыпав дополнительным сыром.',
        20, 30, 2, v_author
    ) RETURNING id INTO v_recipe;

    INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Макароны'),             200, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Бекон'),                150, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Яйца куриные'),           3, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Сыр твердый'),           80, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Перец черный молотый'),   1, 'TEASPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Соль'),                   1, 'TEASPOON');

    INSERT INTO recipe_categories (recipe_id, category_id) VALUES
        (v_recipe, (SELECT id FROM categories WHERE name = 'Ужины')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Основные блюда'));

    -- 2. Борщ
    INSERT INTO recipes (title, description, instructions, active_cooking_time, total_cooking_time, servings, user_id)
    VALUES (
        'Борщ',
        'Наваристый красный суп — символ домашней кухни',
        E'1. Сварите говядину до готовности, снимая пену. Бульон процедите, мясо нарежьте.\n'
        '2. Картофель нарежьте кубиками, добавьте в бульон, варите 10 минут.\n'
        '3. Капусту нашинкуйте, добавьте в суп.\n'
        '4. Обжарьте лук и морковь на масле, добавьте нарезанные помидоры, тушите 5 минут.\n'
        '5. Добавьте зажарку в суп, варите ещё 10 минут.\n'
        '6. Посолите, поперчите. Подавайте со сметаной и зеленью.',
        40, 100, 6, v_author
    ) RETURNING id INTO v_recipe;

    INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Говядина'),               500, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Капуста белокочанная'),   300, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Картофель'),                4, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Морковь'),                  1, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Лук репчатый'),             1, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Помидоры'),                 2, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Сметана'),                100, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Масло растительное'),       2, 'TABLESPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Соль'),                     1, 'TABLESPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Перец черный молотый'),     1, 'TEASPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Зелень петрушки'),          1, 'BUNCH');

    INSERT INTO recipe_categories (recipe_id, category_id) VALUES
        (v_recipe, (SELECT id FROM categories WHERE name = 'Обеды')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Супы'));

    -- 3. Блинчики на молоке
    INSERT INTO recipes (title, description, instructions, active_cooking_time, total_cooking_time, servings, user_id)
    VALUES (
        'Блинчики на молоке',
        'Тонкие нежные блины — идеальный завтрак или угощение к чаю',
        E'1. Взбейте яйца с сахаром и щепоткой соли.\n'
        '2. Добавьте молоко, перемешайте.\n'
        '3. Всыпьте просеянную муку и вымешайте тесто до однородности — оно должно быть как жидкая сметана.\n'
        '4. Добавьте растопленное сливочное масло, перемешайте и оставьте на 15 минут.\n'
        '5. Выпекайте на разогретой сковороде, смазанной маслом, с каждой стороны по 1–2 минуты.\n'
        '6. Подавайте со сметаной, вареньем или мёдом.',
        25, 40, 4, v_author
    ) RETURNING id INTO v_recipe;

    INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Мука пшеничная'),  250, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Молоко'),          500, 'MILLILITERS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Яйца куриные'),      2, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Сахар'),             2, 'TABLESPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Масло сливочное'), 30, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Соль'),              1, 'PINCH'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Масло растительное'), 1, 'TABLESPOON');

    INSERT INTO recipe_categories (recipe_id, category_id) VALUES
        (v_recipe, (SELECT id FROM categories WHERE name = 'Завтраки')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Блинчики и оладьи')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Для детей'));

    -- 4. Греческий салат
    INSERT INTO recipes (title, description, instructions, active_cooking_time, total_cooking_time, servings, user_id)
    VALUES (
        'Греческий салат',
        'Свежий овощной салат с моцареллой — лёгкий, яркий, быстрый',
        E'1. Нарежьте помидоры крупными кусочками.\n'
        '2. Огурцы нарежьте полукольцами.\n'
        '3. Болгарский перец нарежьте соломкой.\n'
        '4. Моцареллу нарежьте кубиками.\n'
        '5. Смешайте все ингредиенты в миске.\n'
        '6. Заправьте маслом, добавьте уксус, соль и перец по вкусу.\n'
        '7. Подавайте сразу — салат не терпит долгого ожидания.',
        15, 15, 2, v_author
    ) RETURNING id INTO v_recipe;

    INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Помидоры'),           3, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Огурцы'),             2, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Болгарский перец'),   1, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Сыр моцарелла'),    150, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Масло растительное'), 3, 'TABLESPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Уксус'),              1, 'TABLESPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Соль'),               1, 'PINCH');

    INSERT INTO recipe_categories (recipe_id, category_id) VALUES
        (v_recipe, (SELECT id FROM categories WHERE name = 'Закуски')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Вегетарианские')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Здоровое питание')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Быстрые рецепты'));

    -- 5. Куриный суп с вермишелью
    INSERT INTO recipes (title, description, instructions, active_cooking_time, total_cooking_time, servings, user_id)
    VALUES (
        'Куриный суп с вермишелью',
        'Лёгкий прозрачный суп на курином бульоне — лучшее домашнее блюдо',
        E'1. Залейте куриное филе холодной водой, доведите до кипения, слейте первый бульон.\n'
        '2. Залейте чистой водой (2.5 л), добавьте целую луковицу и морковь. Варите 30 минут.\n'
        '3. Достаньте курицу и нарежьте на кусочки. Лук выньте и выбросьте.\n'
        '4. Добавьте в бульон картофель кубиками, варите 10 минут.\n'
        '5. Всыпьте макароны, варите ещё 7 минут.\n'
        '6. Верните курицу, посолите, добавьте рубленую зелень.',
        20, 60, 4, v_author
    ) RETURNING id INTO v_recipe;

    INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Куриное филе'),   500, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Макароны'),       100, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Картофель'),        3, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Морковь'),          1, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Лук репчатый'),     1, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Зелень петрушки'),  1, 'BUNCH'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Соль'),             1, 'TABLESPOON');

    INSERT INTO recipe_categories (recipe_id, category_id) VALUES
        (v_recipe, (SELECT id FROM categories WHERE name = 'Обеды')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Супы'));

    -- 6. Омлет с сыром
    INSERT INTO recipes (title, description, instructions, active_cooking_time, total_cooking_time, servings, user_id)
    VALUES (
        'Омлет с сыром',
        'Пышный омлет за 10 минут — идеальный быстрый завтрак',
        E'1. Взбейте яйца с молоком, посолите.\n'
        '2. Растопите сливочное масло на сковороде на среднем огне.\n'
        '3. Вылейте яичную смесь, накройте крышкой и готовьте 5–6 минут на слабом огне.\n'
        '4. За минуту до готовности посыпьте тёртым сыром.\n'
        '5. Сложите пополам и сразу подавайте.',
        5, 10, 1, v_author
    ) RETURNING id INTO v_recipe;

    INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Яйца куриные'),   3, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Молоко'),        50, 'MILLILITERS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Сыр твердый'),   50, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Масло сливочное'), 15, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Соль'),            1, 'PINCH');

    INSERT INTO recipe_categories (recipe_id, category_id) VALUES
        (v_recipe, (SELECT id FROM categories WHERE name = 'Завтраки')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Омлеты и яичницы')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Быстрые рецепты'));

    -- 7. Овсяная каша с мёдом
    INSERT INTO recipes (title, description, instructions, active_cooking_time, total_cooking_time, servings, user_id)
    VALUES (
        'Овсяная каша с мёдом',
        'Полезный завтрак, который заряжает энергией на всё утро',
        E'1. Доведите молоко до кипения.\n'
        '2. Всыпьте овсяные хлопья, убавьте огонь до среднего.\n'
        '3. Варите, помешивая, 5–7 минут до загустения.\n'
        '4. Добавьте соль и сахар по вкусу.\n'
        '5. Снимите с огня, дайте постоять 2 минуты.\n'
        '6. Подавайте с ложкой мёда.',
        5, 10, 1, v_author
    ) RETURNING id INTO v_recipe;

    INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Овсяные хлопья'),  80, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Молоко'),          200, 'MILLILITERS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Мед'),               1, 'TABLESPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Сахар'),             1, 'TEASPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Соль'),              1, 'PINCH');

    INSERT INTO recipe_categories (recipe_id, category_id) VALUES
        (v_recipe, (SELECT id FROM categories WHERE name = 'Завтраки')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Каши')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Быстрые рецепты')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Здоровое питание'));

    -- 8. Пицца Маргарита
    INSERT INTO recipes (title, description, instructions, active_cooking_time, total_cooking_time, servings, user_id)
    VALUES (
        'Пицца Маргарита',
        'Домашняя пицца на тонком тесте с томатным соусом и моцареллой',
        E'1. Растворите дрожжи в 150 мл тёплой воды с щепоткой сахара, оставьте на 10 минут.\n'
        '2. Смешайте муку с солью, добавьте дрожжевую смесь и масло, замесите тесто.\n'
        '3. Дайте тесту подойти в тёплом месте 30–40 минут.\n'
        '4. Раскатайте в тонкий круг, выложите на смазанный маслом противень.\n'
        '5. Намажьте соусом из измельчённых помидоров, посолите.\n'
        '6. Выложите нарезанную моцареллу.\n'
        '7. Запекайте при 220°C 12–15 минут до золотистой корочки.',
        25, 70, 4, v_author
    ) RETURNING id INTO v_recipe;

    INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Мука пшеничная'),   300, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Дрожжи сухие'),       5, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Помидоры'),           3, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Сыр моцарелла'),    200, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Масло растительное'), 2, 'TABLESPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Соль'),               1, 'TEASPOON');

    INSERT INTO recipe_categories (recipe_id, category_id) VALUES
        (v_recipe, (SELECT id FROM categories WHERE name = 'Выпечка')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Пицца')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Вегетарианские'));

    -- 9. Котлеты домашние
    INSERT INTO recipes (title, description, instructions, active_cooking_time, total_cooking_time, servings, user_id)
    VALUES (
        'Котлеты домашние',
        'Сочные котлеты из мясного фарша — классика русской кухни',
        E'1. Намочите хлеб в воде, затем отожмите.\n'
        '2. Смешайте фарш с мелко нарезанным луком, хлебом, яйцом, солью и перцем.\n'
        '3. Хорошо вымешайте фарш руками до однородности.\n'
        '4. Слепите котлеты продолговатой формы, слегка приплющив их.\n'
        '5. Разогрейте масло на сковороде, обжаривайте котлеты по 4–5 минут с каждой стороны.\n'
        '6. Накройте крышкой и доведите на слабом огне ещё 5 минут.',
        20, 40, 5, v_author
    ) RETURNING id INTO v_recipe;

    INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Фарш мясной'),          500, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Лук репчатый'),            1, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Хлеб белый'),              2, 'SLICE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Яйца куриные'),            1, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Масло растительное'),      3, 'TABLESPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Соль'),                    1, 'TEASPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Перец черный молотый'),    1, 'TEASPOON');

    INSERT INTO recipe_categories (recipe_id, category_id) VALUES
        (v_recipe, (SELECT id FROM categories WHERE name = 'Ужины')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Основные блюда'));

    -- 10. Гречка с грибами
    INSERT INTO recipes (title, description, instructions, active_cooking_time, total_cooking_time, servings, user_id)
    VALUES (
        'Гречка с грибами',
        'Простое и сытное вегетарианское блюдо на каждый день',
        E'1. Промойте гречку, залейте 400 мл воды. Доведите до кипения, убавьте огонь и варите под крышкой 15 минут.\n'
        '2. Лук нарежьте кубиками, шампиньоны — пластинками.\n'
        '3. Обжарьте лук на масле до прозрачности, добавьте грибы.\n'
        '4. Жарьте, помешивая, 8–10 минут, пока не выпарится жидкость.\n'
        '5. Смешайте гречку с грибами, посолите и поперчите.\n'
        '6. Подавайте горячим, по желанию — со сметаной.',
        15, 30, 2, v_author
    ) RETURNING id INTO v_recipe;

    INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Гречка'),               200, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Шампиньоны'),           300, 'GRAMS'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Лук репчатый'),           1, 'PIECE'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Масло растительное'),     2, 'TABLESPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Соль'),                   1, 'TEASPOON'),
        (v_recipe, (SELECT id FROM ingredients WHERE name = 'Перец черный молотый'),   1, 'TEASPOON');

    INSERT INTO recipe_categories (recipe_id, category_id) VALUES
        (v_recipe, (SELECT id FROM categories WHERE name = 'Обеды')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Гарниры')),
        (v_recipe, (SELECT id FROM categories WHERE name = 'Вегетарианские'));

END $$;
