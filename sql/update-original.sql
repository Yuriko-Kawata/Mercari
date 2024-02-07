UPDATE original
SET category_name = 'カテゴリ無/カテゴリ無/カテゴリ無'
WHERE category_name IS NULL;
UPDATE original
SET category_name = SPLIT_PART(category_name, '/', 1) || '/' || SPLIT_PART(category_name, '/', 2) || '/' || SPLIT_PART(category_name, '/', 3)
WHERE REGEXP_COUNT(category_name, '/') >= 3;