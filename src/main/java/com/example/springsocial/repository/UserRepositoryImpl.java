package com.example.springsocial.repository;

import com.example.springsocial.exception.DataAccessException;
import com.example.springsocial.model.EnabledType;
import com.example.springsocial.model.User;
import com.example.springsocial.model.UserInfo;
import com.example.springsocial.model.UserRequest;
import com.example.springsocial.util.JsonCodec;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User create(UserRequest userRequest) throws DataAccessException {

        UserQueryBuilder userQueryBuilder = new UserQueryBuilder();
        userQueryBuilder.addParameter("name", userRequest.getName().trim())
                .addParameter("mail", userRequest.getMail().trim())
                .addParameter("created_by", userRequest.getUserAction().trim())
                .addParameter("last_update_by", userRequest.getUserAction().trim());

        int inserted = this.namedParameterJdbcTemplate.update(userQueryBuilder.getSimpleInsertQuery(), userQueryBuilder.getNamedParameters());
        if (inserted == 0) {
            throw new DataAccessException("INSERT", "No se pudo insertar el usuario");
        }
        return this.findByIdOrUser(0, userRequest.getMail());
    }

    @Override
    public void update(UserRequest userRequest) {
        UserQueryBuilder userQueryBuilder = new UserQueryBuilder();

        userQueryBuilder.addUpdateParameter("name", userRequest.getName())
                .addUpdateParameter("metadata", userRequest.getMetadata())
                .addUpdateParameter("avatar", userRequest.getAvatar())
                .addUpdateParameter("roles", JsonCodec.convertToJsonbIfIsNotNull(userRequest.getRole()))
                .addUpdateParameter("enabled", (userRequest.getEnabled() != null) ? userRequest.getEnabled().name() : null)
                //.addUpdateParameter("last_update_date", new Timestamp((new Date()).getTime()))
                .addUpdateParameter("last_update_by", userRequest.getUserAction())
                //TODO: que pasa si quiere editar el mail?
                .addParameter("mail", userRequest.getMail());
        int updated = this.namedParameterJdbcTemplate.update(userQueryBuilder.updateQuery(), userQueryBuilder.getNamedParameters());
        if (updated == 0) {
            throw new DataAccessException("UPDATE", "No se pudo editar el usuario");
        }
    }


    @Override
    public List<User> getAll() {
        UserQueryBuilder userQb = new UserQueryBuilder();

        List<User> users = this.namedParameterJdbcTemplate
                .query(userQb.getSelectQuery(),
                        userQb.getNamedParameters(),
                        new UserRowMapper());
        return users;

    }

    @Override
    public List<UserInfo> getAllEnabled() {
        UserQueryBuilder userQb = new UserQueryBuilder();
        userQb.addFilter("enabled", "enabled", EnabledType.ENABLED.name());
        List<UserInfo> users = this.namedParameterJdbcTemplate
                .query(userQb.getSelectQuery(),
                        userQb.getNamedParameters(),
                        new BeanPropertyRowMapper(UserInfo.class));
        return users;

    }

    @Override
    public User findByIdOrUser(int userId, String mail) {
        UserQueryBuilder userQb = new UserQueryBuilder();
        userQb.filterById(userId);
        userQb.filterByMail(mail);

        List<User> users = this.namedParameterJdbcTemplate
                .query(
                        userQb.getSelectQuery(),
                        userQb.getNamedParameters(),
                        new UserRowMapper());
        if (users.size() > 0) {
            return users.get(0);
        }

        return null;
    }


    @Override
    public Boolean existsByEmail(String email){
       if (findByIdOrUser(0, email) != null){
           return true;
       }
       return false;
    }

    @Override
    public void delete(String email) {
        UserQueryBuilder userQueryBuilder = new UserQueryBuilder();
        userQueryBuilder.addParameter("mail", email);
        this.namedParameterJdbcTemplate.update(userQueryBuilder.deleteQuery(), userQueryBuilder.getNamedParameters());
    }

    @Override
    public void enabledDisabled(int userId) {

    }

    public class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            return User.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .mail(resultSet.getString("mail"))
                    .avatar(resultSet.getString("avatar"))
                    .metadata(resultSet.getString("metadata"))
                    .enabled(resultSet.getString("enabled"))
                    .role(JsonCodec.fromJson(resultSet.getString("roles"), List.class))
                    .build();
        }
    }
}
