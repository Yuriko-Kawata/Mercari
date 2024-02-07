-- categoryテーブルへのデータの挿入
CREATE OR REPLACE PROCEDURE process_categories() LANGUAGE plpgsql AS $$
DECLARE category_record RECORD;
category_parts TEXT [];
current_parent_id INTEGER;
current_name_all TEXT;
BEGIN FOR category_record IN
SELECT DISTINCT category_name
FROM original LOOP category_parts := string_to_array(category_record.category_name, '/');
current_parent_id := NULL;
current_name_all := '';
FOR i IN 1..LEAST(array_length(category_parts, 1), 3) LOOP current_name_all := current_name_all || category_parts [i] || '/';
IF NOT EXISTS (
    SELECT 1
    FROM category
    WHERE name = category_parts [i]
        AND (
            parent_id = current_parent_id
            OR (
                current_parent_id IS NULL
                AND parent_id IS NULL
            )
        )
) THEN
INSERT INTO category (name, parent_id, name_all)
VALUES (category_parts [i], current_parent_id, NULL)
RETURNING id INTO current_parent_id;
IF i = LEAST(array_length(category_parts, 1), 3) THEN
UPDATE category
SET name_all = rtrim(current_name_all, '/')
WHERE id = current_parent_id;
END IF;
ELSE
SELECT id INTO current_parent_id
FROM category
WHERE name = category_parts [i]
    AND (
        parent_id = current_parent_id
        OR (
            current_parent_id IS NULL
            AND parent_id IS NULL
        )
    );
END IF;
END LOOP;
END LOOP;
END;
$$;
CALL process_categories();
-- itemsテーブルへのデータの挿入
CREATE OR REPLACE FUNCTION update_timestamp() RETURNS TRIGGER AS $$ BEGIN NEW.update_time = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER update_items_timestamp BEFORE
INSERT
    OR
UPDATE ON items FOR EACH ROW EXECUTE FUNCTION update_timestamp();
ALTER TABLE items
ADD name_all character varying(255);
INSERT INTO items(
        name,
        condition,
        category,
        brand,
        price,
        shipping,
        description,
        name_all
    )
SELECT o.name,
    o.condition_id,
    c.id,
    (COALESCE(o.brand, '')),
    o.price,
    o.shipping,
    o.description,
    o.category_name
FROM original AS o,
    category AS c
WHERE o.category_name = c.name_all;
ALTER TABLE items DROP COLUMN name_all;