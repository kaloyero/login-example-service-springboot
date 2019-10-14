package com.example.springsocial.repository;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserQueryBuilder {
    private static final String INSERT_QUERY_SIMPLE = "INSERT INTO application_user" +
            "( name, mail,created_by, last_update_by) " +
            "VALUES (:name,:mail,:created_by,:last_update_by)";

    private static final String SELECT_QUERY = "SELECT * FROM application_user WHERE 1 = 1";

    private static final String DEFAULT_ORDER = " order by name";

    private static final String UPDATE_QUERY = "UPDATE application_user set ";

    private static final String DELETE_QUERY = "DELETE FROM application_user WHERE mail = :mail";

    private Map<String, Object> namedParameters;
    private StringBuilder baseQuery;
    private StringBuilder updateField;

    public UserQueryBuilder() {
        this.updateField = new StringBuilder();
        this.baseQuery = new StringBuilder();
        this.namedParameters = new HashMap<>();
    }

    public UserQueryBuilder addUpdateParameter(String key, Object value) {
        if (value == null) return this;

        if (this.updateField.length() > 0) {
            this.updateField.append(", ");
        }
        this.updateField.append("").append(key).append("").append(" = ").append(":").append(key);
        return this.addParameter(key, value);
    }


    public UserQueryBuilder addParameter(String key, Object value) {
        this.namedParameters.put(key, value);
        return this;
    }

    public UserQueryBuilder addFilter(String attributeName, String parameterName, Object value) {
        this.namedParameters.put(parameterName, value);
        this.baseQuery.append(" AND ").append(attributeName).append(" = :").append(parameterName);
        return this;
    }

    public UserQueryBuilder filterById(int userId) {
        if (userId == 0) return this;
        return this.addFilter("id", "id", userId);
    }

    public UserQueryBuilder filterByMail(String mail) {
        if (StringUtils.isEmpty(mail)) return this;
        return this.addFilter("mail", "mail", mail);
    }

    public String getSimpleInsertQuery() {
        return INSERT_QUERY_SIMPLE;
    }

    public String getSelectQuery() {
        return (new StringBuilder(SELECT_QUERY)).append(this.baseQuery).append(DEFAULT_ORDER).toString();
    }

    public String updateQuery() {
        return (new StringBuilder(UPDATE_QUERY)).append(this.updateField).append(" WHERE mail = :mail").toString();
    }

    public String deleteQuery() {
        return DELETE_QUERY;
    }
}
