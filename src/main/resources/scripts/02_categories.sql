-- Основные категории (родительские)
INSERT INTO categories (name, description) VALUES
('Завтраки',    'Рецепты для начала дня — каши, омлеты, бутерброды'),
('Обеды',       'Основные приёмы пищи — супы, горячие блюда'),
('Ужины',       'Лёгкие и сытные ужины'),
('Десерты',     'Сладкие блюда, выпечка, торты'),
('Напитки',     'Коктейли, соки, чаи, кофе, лимонады'),
('Закуски',     'Салаты, бутерброды, канапе'),
('Выпечка',     'Хлеб, пироги, пирожки, булочки'),
('Соусы',       'Домашние соусы, маринады и заправки'),
('Заготовки',   'Варенья, маринады, консервация на зиму')
ON CONFLICT (name) DO NOTHING;

-- Подкатегории Завтраков
INSERT INTO categories (name, description, parent_category_id) VALUES
('Каши',               'Овсяная, гречневая, рисовая и другие каши',  (SELECT id FROM categories WHERE name = 'Завтраки')),
('Омлеты и яичницы',   'Блюда из яиц на завтрак',                    (SELECT id FROM categories WHERE name = 'Завтраки')),
('Блинчики и оладьи',  'Сладкие и несладкие варианты',               (SELECT id FROM categories WHERE name = 'Завтраки')),
('Сэндвичи и тосты',   'Быстрые завтраки с хлебом',                  (SELECT id FROM categories WHERE name = 'Завтраки')),
('Смузи-боулы',        'Густые смузи с топпингами',                   (SELECT id FROM categories WHERE name = 'Завтраки'))
ON CONFLICT (name) DO NOTHING;

-- Подкатегории Обедов
INSERT INTO categories (name, description, parent_category_id) VALUES
('Супы',            'Горячие и холодные супы',                  (SELECT id FROM categories WHERE name = 'Обеды')),
('Крем-супы',       'Пюрированные супы-кремы',                  (SELECT id FROM categories WHERE name = 'Обеды')),
('Гарниры',         'Рис, гречка, картофель, макароны',         (SELECT id FROM categories WHERE name = 'Обеды')),
('Основные блюда',  'Мясные, рыбные, овощные блюда',           (SELECT id FROM categories WHERE name = 'Обеды')),
('Паста и ризотто', 'Итальянские блюда из пасты и риса',        (SELECT id FROM categories WHERE name = 'Обеды'))
ON CONFLICT (name) DO NOTHING;

-- Подкатегории Десертов
INSERT INTO categories (name, description, parent_category_id) VALUES
('Торты',           'Праздничные и повседневные торты',  (SELECT id FROM categories WHERE name = 'Десерты')),
('Печенье',         'Домашнее печенье и пряники',        (SELECT id FROM categories WHERE name = 'Десерты')),
('Пироги',          'Фруктовые и ягодные пироги',        (SELECT id FROM categories WHERE name = 'Десерты')),
('Кексы и маффины', 'Небольшая сладкая выпечка',         (SELECT id FROM categories WHERE name = 'Десерты')),
('Конфеты и трюфели', 'Домашние сладости без выпечки',   (SELECT id FROM categories WHERE name = 'Десерты')),
('Мороженое',       'Домашнее мороженое и сорбеты',      (SELECT id FROM categories WHERE name = 'Десерты'))
ON CONFLICT (name) DO NOTHING;

-- Подкатегории Выпечки
INSERT INTO categories (name, description, parent_category_id) VALUES
('Хлеб',      'Домашний хлеб на закваске и дрожжах',      (SELECT id FROM categories WHERE name = 'Выпечка')),
('Пирожки',   'Жареные и печёные пирожки',                 (SELECT id FROM categories WHERE name = 'Выпечка')),
('Пицца',     'Домашняя пицца с разными начинками',        (SELECT id FROM categories WHERE name = 'Выпечка')),
('Булочки',   'Сдобные булочки, синнабоны, круассаны',     (SELECT id FROM categories WHERE name = 'Выпечка')),
('Пироги',    'Закрытые и открытые пироги',                (SELECT id FROM categories WHERE name = 'Выпечка'))
ON CONFLICT (name) DO NOTHING;

-- Подкатегории Напитков
INSERT INTO categories (name, description, parent_category_id) VALUES
('Горячие напитки',   'Чай, кофе, какао, глинтвейн',          (SELECT id FROM categories WHERE name = 'Напитки')),
('Смузи и соки',      'Свежевыжатые соки и смузи',             (SELECT id FROM categories WHERE name = 'Напитки')),
('Лимонады',          'Освежающие безалкогольные напитки',      (SELECT id FROM categories WHERE name = 'Напитки')),
('Коктейли',          'Праздничные и повседневные коктейли',    (SELECT id FROM categories WHERE name = 'Напитки'))
ON CONFLICT (name) DO NOTHING;

-- Подкатегории Закусок
INSERT INTO categories (name, description, parent_category_id) VALUES
('Салаты',            'Овощные, мясные и рыбные салаты',       (SELECT id FROM categories WHERE name = 'Закуски')),
('Горячие закуски',   'Брускетты, жульены, тарталетки',        (SELECT id FROM categories WHERE name = 'Закуски')),
('Намазки и паштеты', 'Хумус, паштет, гуакамоле',              (SELECT id FROM categories WHERE name = 'Закуски'))
ON CONFLICT (name) DO NOTHING;

-- Специальные категории
INSERT INTO categories (name, description) VALUES
('Вегетарианские',   'Блюда без мяса и рыбы'),
('Веганские',        'Блюда без продуктов животного происхождения'),
('Быстрые рецепты',  'Приготовление до 30 минут'),
('Для детей',        'Рецепты, которые понравятся детям'),
('Праздничные',      'Блюда для особых случаев'),
('Здоровое питание', 'Низкокалорийные и полезные блюда'),
('Постные блюда',    'Рецепты без мяса и молочных продуктов'),
('На мангале',       'Барбекю, шашлыки, гриль'),
('Азиатская кухня',  'Блюда китайской, японской и тайской кухни'),
('Итальянская кухня','Паста, пицца, ризотто и другие итальянские блюда')
ON CONFLICT (name) DO NOTHING;
