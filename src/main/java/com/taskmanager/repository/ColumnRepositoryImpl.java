package com.taskmanager.repository;

import com.taskmanager.model.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ColumnRepositoryImpl implements ColumnRepository {
    private final DataSource dataSource;

    @Autowired
    public ColumnRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long create(Column column) {
        String sql = ""
                + "INSERT INTO columns (name,index)"
                + "VALUES (?,?)";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(
                sql,
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, column.getName());
            statement.setInt(2, column.getIndex());
            statement.execute();

            ResultSet set = statement.getGeneratedKeys();

            if (!set.next()) {
                throw new SQLException("Column id is not generated");
            }

            return set.getLong("id");
        } catch (SQLException e) {
            throw new RuntimeException("Creation column failed", e);
        }
    }

    @Override
    public void update(Column column) {
        String sql = ""
                + " UPDATE columns"
                + " SET    name = ?, index = ?"
                + " WHERE  id = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setString(1, column.getName());
            statement.setInt(2, column.getIndex());
            statement.setLong(3, column.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Update column failed", e);
        }
    }

    @Override
    public void updateAll(List<Column> columns) {
        String sql = ""
                + " UPDATE columns"
                + " SET    index = ?"
                + " WHERE  id = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {

           for (Column column : columns){
               statement.setInt(1, column.getIndex());
               statement.setLong(2, column.getId());
               statement.execute();
           }

        } catch (SQLException e) {
            throw new RuntimeException("Update column failed", e);
        }
    }

    @Override
    public void delete(Long columnId) {
        String sql = ""
                + " DELETE"
                + " FROM columns"
                + " WHERE id = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, columnId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Removal column failed", e);
        }
    }

    @Override
    public Optional<Column> findById(Long id) {
        String sql = ""
                + " SELECT *"
                + " FROM columns"
                + " WHERE id = ?";

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(toModel(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException("Column " + id + " not found", e);
        }
    }

    @Override
    public Optional<Column> findByIndex(Integer index) {
        String sql = ""
                + " SELECT *"
                + " FROM columns "
                + " WHERE index = ?";

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, index);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(toModel(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException("Column with index" + index + " not found", e);
        }
    }

    @Override
    public List<Column> findAll() {
        String sql = ""
                + " SELECT *"
                + " FROM columns"
                + " ORDER BY index";

        try (Statement statement = dataSource.getConnection().createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            List<Column> list = new ArrayList<>();

            while (resultSet.next()) {
                Column column = toModel(resultSet);
                list.add(column);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Column not found", e);
        }
    }

    @Override
    public List<Column> findAllByRange(Integer startIndex, Integer endIndex) {
        String sql = ""
                + " SELECT *"
                + " FROM columns"
                + " WHERE index"
                + " BETWEEN ?"
                + " AND ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setInt(1, startIndex);
            statement.setInt(2, endIndex);
            ResultSet resultSet = statement.executeQuery();

            List<Column> list = new ArrayList<>();

            while (resultSet.next()) {
                Column column = toModel(resultSet);
                list.add(column);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Find all by range failed");
        }
    }

    @Override
    public boolean existsById(Long id) {
        String sql = ""
                + " SELECT 1"
                + " FROM columns"
                + " WHERE id = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException("Column with id: " + id + " already exist");
        }
    }

    @Override
    public Integer getCount() {
        try (Statement statement = dataSource.getConnection().createStatement()) {

            String sql = ""
                    + " SELECT count(*)"
                    + " FROM columns";

            ResultSet resultSet = statement.executeQuery(sql);
            int count = 0;
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            return count;
        } catch (SQLException e) {
            throw new RuntimeException("Column not found", e);
        }
    }

    private static Column toModel(ResultSet resultSet) throws SQLException {
        Column column = new Column();
        column.setId(resultSet.getLong("id"));
        column.setName(resultSet.getString("name"));
        column.setIndex(resultSet.getInt("index"));

        return column;
    }
}
