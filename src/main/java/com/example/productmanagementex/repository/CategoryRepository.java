package com.example.productmanagementex.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.productmanagementex.domain.Category;

/**
 * categoryのrepositoryクラス
 * 
 * @author hiraizumi
 */
@Repository
public class CategoryRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Category> CATEGORY_ROWMAPPER = (rs, i) -> {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setParentId(rs.getInt("parent_id"));
        category.setName(rs.getString("name"));
        category.setNameAll(rs.getString("name_all"));
        return category;
    };

    // 全件取得用のクエリ
    private final String FIND_ALL_SQL = """
            SELECT
                id, parent_id, name, name_all
            FROM
                category
            ORDER BY
                name, id
            ;
            """;

    // idを用いたname検索用クエリ
    private static final String FIND_NAME_BY_ID_SQL = """
            SELECT
                name
            FROM
                category
            WHERE
                id = :id
            ;
            """;

    // name_allで該当するレコードがあるかチェックするクエリ
    private final String CHECK_CATEGORY_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                name_all = :nameAll
            ;
            """;

    // name_allを更新するクエリ
    private final String UPDATE_NAMEALL_SQL = """
            UPDATE
                category
            SET
                -- :originalNameを含むname_allを:nameに更新
                name_all = REPLACE(name_all, :originalName, :name)
            WHERE
                name_all LIKE :originalNameLike
            ;
            """;

    // カテゴリ新規作成用のクエリ（存在しない組み合わせのみ新規追加）
    private final String INSERT_SQL = """
            CREATE OR REPLACE PROCEDURE add_category(new_category_name TEXT)
            LANGUAGE plpgsql
            AS $$
            DECLARE
                category_parts TEXT[];
                current_parent_id INTEGER;
                current_name_all TEXT;
                i INTEGER;
            BEGIN
                category_parts := string_to_array(new_category_name, '/');
                current_parent_id := NULL;
                current_name_all := '';

                FOR i IN 1..array_length(category_parts, 1) LOOP
                    IF i = 3 THEN
                        -- 孫カテゴリの場合、name_all を設定
                        current_name_all := category_parts[1] || '/' || category_parts[2] || '/' || category_parts[3];
                    ELSE
                        -- 親カテゴリと子カテゴリでは name_all を NULL に
                        current_name_all := NULL;
                    END IF;

                    IF NOT EXISTS (SELECT 1 FROM category WHERE name = category_parts[i] AND (parent_id = current_parent_id OR (parent_id IS NULL AND current_parent_id IS NULL))) THEN
                        INSERT INTO category (name, parent_id, name_all)
                        VALUES (category_parts[i], current_parent_id, current_name_all)
                        RETURNING id INTO current_parent_id;
                    ELSE
                        SELECT id INTO current_parent_id FROM category WHERE name = category_parts[i] AND (parent_id = current_parent_id OR (parent_id IS NULL AND current_parent_id IS NULL));
                    END IF;
                END LOOP;
            END;
            $$;

            CALL add_category(:nameAll)
            ;
            """;

    // 親カテゴリのみ取得するクエリ
    private static final String FIND_PARENT_SQL = """
            SELECT
                id, name, parent_id, name_all
            FROM
                category
            WHERE
                parent_id IS NULL AND name_all IS NULL
            ORDER BY
                id
            LIMIT
                30
            OFFSET
                (:page - 1)* 30
            ;
            """;

    // 子カテゴリのみ取得するクエリ
    private static final String FIND_CHILD_SQL = """
            SELECT
                id, name, parent_id, name_all
            FROM
                category
            WHERE
                parent_id IS NOT NULL AND name_all IS NULL
            ORDER BY
                parent_id, id
            LIMIT
                30
            OFFSET
                (:page - 1)* 30
            ;
            """;

    // 孫カテゴリのみ取得するクエリ
    private static final String FIND_GRAND_SQL = """
            SELECT
                id, name, parent_id, name_all
            FROM
                category
            WHERE
                name_all IS NOT NULL
            ORDER BY
                parent_id, id
            LIMIT
                30
            OFFSET
                (:page - 1)* 30
            ;
            """;

    // 親カテゴリのトータル件数を取得するクエリ
    private static final String PARENT_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                parent_id IS NULL AND name_all IS NULL
            ;
            ;
            """;

    // 子カテゴリのトータル件数を取得するクエリ
    private static final String CHILD_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                parent_id IS NOT NULL AND name_all IS NULL
            ;
            """;

    // 孫カテゴリのトータル件数を取得するクエリ
    private static final String GRAND_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                name_all IS NOT NULL
            ;
            """;

    // 検索条件に合う親カテゴリを取得するクエリ
    private static final String SEARCH_PARENT_SQL = """
            SELECT
                id, name, parent_id, name_all
            FROM
                category
            WHERE
                parent_id IS NULL AND name_all IS NULL
                AND name LIKE :name
            ORDER BY
                id
            LIMIT
                30
            OFFSET
                (:page - 1) * 30
            ;
            """;

    // 検索条件に合う子カテゴリを取得するクエリ
    private static final String SEARCH_CHILD_SQL = """
            SELECT
                id, name, parent_id, name_all
            FROM
                category
            WHERE
                parent_id IS NOT NULL AND name_all IS NULL
                AND name LIKE :name
            ORDER BY
                parent_id, id
            LIMIT
                30
            OFFSET
                (:page - 1) * 30
            ;
            """;

    // 検索条件に合う孫カテゴリを取得するクエリ
    private static final String SEARCH_GRAND_SQL = """
            SELECT
                id, name, parent_id, name_all
            FROM
                category
            WHERE
                name_all IS NOT NULL
                AND name LIKE :name
            ORDER BY
                parent_id, id
            LIMIT
                30
            OFFSET
                (:page - 1) * 30
            ;
            """;

    // 検索条件に合う親カテゴリのトータル件数を取得するクエリ
    private static final String SEARCH_PARENT_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                parent_id IS NULL AND name_all IS NULL
                AND name LIKE :name
            ;
            """;

    // 検索条件に合う子カテゴリのトータル件数を取得するクエリ
    private static final String SEARCH_CHILD_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                parent_id IS NOT NULL AND name_all IS NULL
                AND name LIKE :name
            ;
            """;

    // 検索条件に合う孫カテゴリのトータル件数を取得するクエリ
    private static final String SEARCH_GRAND_SIZE_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                name_all IS NOT NULL
                AND name LIKE :name
            ;
            """;

    // idで検索を行うクエリ
    private static final String FIND_BY_ID_SQL = """
            SELECT
                id, name, parent_id, name_all
            FROM
                category
            WHERE
                id = :id
            ;
            """;

    // 子カテゴリのみ全件取得するクエリ
    private static final String FIND_CHILD_CATEGORY_BY_PARENT_ID_SQL = """
            SELECT
                id, name, parent_id, name_all
            FROM
                category
            WHERE
                parent_id = :id
            ORDER BY
                parent_id, id
            ;
            """;

    // parent_idで検索したトータル件数を取得するクエリ
    private static final String CHILD_CATEGORY_SIZE_BY_PARENT_ID_SQL = """
            SELECT
                count(*)
            FROM
                category
            WHERE
                parent_id = :id
            ;
            """;

    // parent_idで検索し取得するクエリ
    private static final String FIND_PARENT_CATEGORY_BY_ID_SQL = """
            SELECT
                id, name, parent_id, name_all
            FROM
                category
            WHERE
                id = :parentId
            ;
            """;

    // 階層とparent_idで検索し取得するクエリ
    private static final String FIND_CHANGE_RECORD_ID_SQL = """
            SELECT
                id
            FROM
                category
            WHERE
                (:parentCondition = 1 AND parent_id IN (
                    SELECT
                        id
                    FROM
                        category
                    WHERE
                        parent_id = :parentId
                ))
            OR (:parentCondition = 2 AND parent_id = :id);
                """;

    // 物理削除を行うクエリ
    private static final String DELETE_SQL = """
            DELETE FROM
                category
            WHERE
                id = :id
            ;
            """;

    /**
     * pageに対応する30件取得
     * 
     * @return 検索結果
     */
    public List<Category> findAllCategory() {
        SqlParameterSource param = new MapSqlParameterSource();
        List<Category> categoryList = template.query(FIND_ALL_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    /**
     * idからnameを検索
     * 
     * @param id category id
     * @return name
     */
    public String findNameById(int id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        String name = template.queryForObject(FIND_NAME_BY_ID_SQL, param, String.class);
        return name;
    }

    /**
     * 重複するカテゴリがないかチェック
     * 
     * @param nameAll name_all
     * @return 検索結果があれば１、なければ0
     */
    public Integer checkCategory(String nameAll) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("nameAll", nameAll);
        Integer categoryCount = template.queryForObject(CHECK_CATEGORY_SQL, param, Integer.class);
        return categoryCount;
    }

    /**
     * 階層、名前が重複するものがないか確認
     * 
     * @param name            name
     * @param parentCondition 階層
     * @return 検索結果があれば１、なければ０
     */
    public Integer checkCategoryName(String name, int parentCondition) {
        // 動的にクエリ変えたいからstaticじゃない
        String sql = """
                SELECT
                    count(*)
                FROM
                    category
                WHERE
                    name = :name
                """;

        // 階層ごとにWHERE句にクエリを追加
        if (parentCondition == 0) {
            sql += "AND parent_id IS NULL";
        } else if (parentCondition == 1) {
            sql += "AND parent_id IS NOT NULL AND name_all IS NULL";
        } else {
            sql += "AND parent_id IS NOT NULL AND name_all IS NOT NULL";
        }
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", name);
        Integer count = template.queryForObject(sql, param, Integer.class);
        return count;
    }

    /**
     * カテゴリの編集
     * 
     * @param id              id
     * @param name            name
     * @param parentCondition 階層
     */
    public void editCategoryName(int id, String name, int parentCondition) {
        // 動的にクエリ変えたいからstaticじゃない
        String sql = """
                UPDATE
                    category
                SET
                    name = :name
                WHERE
                    name = (
                        SELECT
                            name
                        FROM
                            category
                        WHERE
                            id = :id
                        )
                """;

        // 階層ごとにWHERE句にクエリを追加
        if (parentCondition == 1) {
            sql += "AND parent_id IS NULL";
        } else if (parentCondition == 2) {
            sql += "AND parent_id IS NOT NULL AND name_all IS NULL";
        } else {
            sql += "AND parent_id IS NOT NULL AND name_all IS NOT NULL";
        }

        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id).addValue("name", name);
        template.update(sql, param);
    }

    /**
     * カテゴリ編集に伴うname_allの更新
     * 
     * @param id               id
     * @param name             name
     * @param originalName     元のnameを含んだ検索用の文字列 name/% とか
     * @param originalNameLike 元のname_all
     */
    public void editCategoryNameAll(int id, String name, String originalName, String originalNameLike) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id).addValue("name", name)
                .addValue("originalName", originalName).addValue("originalNameLike", originalNameLike);
        template.update(UPDATE_NAMEALL_SQL, param);
    }

    /**
     * 新規作成
     * 
     * @param nameAll name_all
     */
    public void insertCategory(String nameAll) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("nameAll", nameAll);
        template.update(INSERT_SQL, param);
    }

    /**
     * 親カテゴリ取得（pageに対応した３０件）
     * 
     * @param page page
     * @return 検索結果
     */
    public List<Category> findAllParentCategory(int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("page", page);
        List<Category> categoryList = template.query(FIND_PARENT_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    /**
     * 子カテゴリ取得（pageに対応した３０件）
     * 
     * @param page page
     * @return 検索結果
     */
    public List<Category> findAllChildCategory(int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("page", page);
        List<Category> categoryList = template.query(FIND_CHILD_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    /**
     * 孫カテゴリ取得（pageに対応した３０件）
     * 
     * @param page page
     * @return 検索結果
     */
    public List<Category> findAllGrandCategory(int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("page", page);
        List<Category> categoryList = template.query(FIND_GRAND_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    /**
     * 親カテゴリのトータル件数の取得
     * 
     * @return トータル件数
     */
    public Integer parentListSize() {
        SqlParameterSource param = new MapSqlParameterSource();
        Integer size = template.queryForObject(PARENT_SIZE_SQL, param, Integer.class);
        return size;
    }

    /**
     * 子カテゴリのトータル件数の取得
     * 
     * @return トータル件数
     */
    public Integer childListSize() {
        SqlParameterSource param = new MapSqlParameterSource();
        Integer size = template.queryForObject(CHILD_SIZE_SQL, param, Integer.class);
        return size;
    }

    /**
     * 孫カテゴリのトータル件数の取得
     * 
     * @return トータル件数
     */
    public Integer grandListSize() {
        SqlParameterSource param = new MapSqlParameterSource();
        Integer size = template.queryForObject(GRAND_SIZE_SQL, param, Integer.class);
        return size;
    }

    /**
     * 検索結果に一致する親カテゴリの取得
     * 
     * @param nameLike nameの曖昧検索
     * @param page     page
     * @return 検索結果
     */
    public List<Category> searchParentCategory(String nameLike, Integer page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", nameLike).addValue("page", page);
        List<Category> categoryList = template.query(SEARCH_PARENT_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    /**
     * 検索結果に一致する子カテゴリの取得
     * 
     * @param nameLike nameの曖昧検索
     * @param page     page
     * @return 検索結果
     */
    public List<Category> searchChildCategory(String nameLike, int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", nameLike).addValue("page", page);
        List<Category> categoryList = template.query(SEARCH_CHILD_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    /**
     * 検索結果に一致する孫カテゴリの取得
     * 
     * @param nameLike nameの曖昧検索
     * @param page     page
     * @return 検索結果
     */
    public List<Category> searchGrandCategory(String nameLike, int page) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", nameLike).addValue("page", page);
        List<Category> categoryList = template.query(SEARCH_GRAND_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    /**
     * 検索結果に一致する親カテゴリのトータル件数の取得
     * 
     * @param nameLike nameの曖昧検索
     * @return 検索結果
     */
    public Integer searchParentTotal(String nameLike) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", nameLike);
        Integer size = template.queryForObject(SEARCH_PARENT_SIZE_SQL, param, Integer.class);
        return size;
    }

    /**
     * 検索結果に一致する子カテゴリのトータル件数の取得
     * 
     * @param nameLike nameの曖昧検索
     * @return 検索結果
     */
    public Integer searchChildTotal(String nameLike) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", nameLike);
        Integer size = template.queryForObject(SEARCH_CHILD_SIZE_SQL, param, Integer.class);
        return size;
    }

    /**
     * 検索結果に一致する孫カテゴリのトータル件数の取得
     * 
     * @param nameLike nameの曖昧検索
     * @return 検索結果
     */
    public Integer searchGrandTotal(String nameLike) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", nameLike);
        Integer size = template.queryForObject(SEARCH_GRAND_SIZE_SQL, param, Integer.class);
        return size;
    }

    /**
     * idで検索
     * 
     * @param id id
     * @return 検索結果
     */
    public Category findById(int id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        List<Category> categoryList = template.query(FIND_BY_ID_SQL, param, CATEGORY_ROWMAPPER);
        Category category = categoryList.get(0);
        return category;
    }

    /**
     * idから子カテゴリのトータル件数を検索
     * 
     * @param id
     * @return 検索結果
     */
    public Integer childCategorySize(int id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        Integer size = template.queryForObject(CHILD_CATEGORY_SIZE_BY_PARENT_ID_SQL, param, Integer.class);
        return size;
    }

    /**
     * idから子カテゴリを検索
     * 
     * @param id id
     * @return 検索結果
     */
    public List<Category> findChildCategory(int id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        List<Category> categoryList = template.query(FIND_CHILD_CATEGORY_BY_PARENT_ID_SQL, param, CATEGORY_ROWMAPPER);
        return categoryList;
    }

    /**
     * parent_idから親カテゴリを検索
     * 
     * @param parentId parent_id
     * @return 検索結果
     */
    public Category findParentCategory(int parentId) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("parentId", parentId);
        Category category = template.queryForObject(FIND_PARENT_CATEGORY_BY_ID_SQL, param, CATEGORY_ROWMAPPER);
        return category;
    }

    /**
     * 階層とparent_id一致するレコードを検索
     * 
     * @param parentId        parent_id
     * @param parentCondition 階層
     * @return 検索結果
     */
    public List<Integer> findChangeRecordId(int parentId, int parentCondition) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("parentId", parentId).addValue(
                "parentCondition",
                parentCondition);
        List<Integer> changeRecordId = template.queryForList(FIND_CHANGE_RECORD_ID_SQL, param, Integer.class);
        return changeRecordId;
    }

    /**
     * 削除
     * 
     * @param id id
     */
    public void delete(int id) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        template.update(DELETE_SQL, param);
    }
}
