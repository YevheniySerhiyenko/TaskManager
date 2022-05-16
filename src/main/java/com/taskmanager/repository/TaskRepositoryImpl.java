package com.taskmanager.repository;

import com.taskmanager.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final DataSource dataSource;

    @Autowired
    public TaskRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long create(Task task) {
        String sql = ""
                + "INSERT INTO tasks (name,index,description,createdat,columnid)"
                + "VALUES (?,?,?,?,?)";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(
                sql,
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, task.getName());
            statement.setInt(2, task.getIndex());
            statement.setString(3, task.getDescription());
            statement.setDate(4, Date.valueOf(task.getCreatedAt()));
            statement.setLong(5, task.getColumnId());
            statement.execute();

            ResultSet set = statement.getGeneratedKeys();

            if (!set.next()) {
                throw new SQLException("Id is not generated");
            }

            return set.getLong("id");
        } catch (SQLException e) {
            throw new RuntimeException("Creation task failed", e);
        }
    }

    @Override
    public void update(Task task) {
        String sql = ""
                + " UPDATE tasks"
                + " SET    name = ?, index = ?, description = ?, createdat = ?,columnid = ?"
                + " WHERE  id = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setString(1, task.getName());
            statement.setInt(2, task.getIndex());
            statement.setString(3, task.getDescription());
            statement.setDate(4, Date.valueOf(task.getCreatedAt()));
            statement.setLong(5, task.getColumnId());
            statement.setLong(6, task.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Update task failed", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = ""
                + " DELETE FROM tasks "
                + " WHERE id = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Remove by id failed", e);
        }
    }

    @Override
    public void updateAll(List<Task> tasks) {
        String sql = ""
                + " UPDATE tasks"
                + " SET    index = ?"
                + " WHERE  id = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {

            for (Task task : tasks) {
                statement.setInt(1, task.getIndex());
                statement.setLong(2, task.getId());
                statement.execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Update task failed", e);
        }
    }

    @Override
    public Optional<Task> findById(Long id) {
        String sql = ""
                + " SELECT *"
                + " FROM tasks"
                + " WHERE id = ?";

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(toModel(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException("Task " + id + " not found", e);
        }
    }

    @Override
    public List<Task> findAllByColumn(Long columnId) {
        String sql = ""
                + " SELECT *"
                + " FROM tasks"
                + " WHERE columnid = ?"
                + " ORDER  BY index";

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql)) {

            preparedStatement.setLong(1, columnId);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Task> list = new ArrayList<>();

            while (resultSet.next()) {
                Task task = toModel(resultSet);
                list.add(task);
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Task not found", e);
        }
    }

    @Override
    public List<Task> findAllByRange(Long columnId, Integer startIndex, Integer endIndex) {
        String sql = ""
                + " SELECT *"
                + " FROM tasks"
                + " WHERE columnid = ?"
                + " AND index BETWEEN ? AND ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, columnId);
            statement.setInt(2, startIndex);
            statement.setInt(3, endIndex);
            ResultSet resultSet = statement.executeQuery();

            List<Task> list = new ArrayList<>();

            while (resultSet.next()) {
                Task task = toModel(resultSet);
                list.add(task);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Find all by range failed");
        }
    }

    @Override
    public Optional<Task> findByColumnIdAndTaskId(Long columnId, Long id) {
        String sql = ""
                + " SELECT *"
                + " FROM tasks"
                + " WHERE id = ?"
                + " AND columnid = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.setLong(2, columnId);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new SQLException("Task with id: " + id + " not exist");
            }

            return Optional.of(toModel(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException("Task with id: " + id + " not exist");
        }
    }

    @Override
    public boolean existById(Long id) {
        String sql = ""
                + " SELECT 1"
                + " FROM tasks"
                + " WHERE id = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException("Task with id: " + id + " already exist");
        }
    }

    @Override
    public int getCountInColumn(Long id) {
        String sql = ""
                + " SELECT COUNT(*)"
                + " FROM tasks"
                + " WHERE columnid = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            int count = 0;

            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            return count;
        } catch (SQLException e) {
            throw new RuntimeException("Column not found", e);
        }
    }

    @Override
    public boolean existByColumnId(Long columnId) {
        String sql = ""
                + " SELECT 1"
                + " FROM tasks"
                + " WHERE columnid = ?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(sql)) {
            statement.setLong(1, columnId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException("Task with columnId: " + columnId + " already exist");
        }
    }

    private static Task toModel(ResultSet resultSet) throws SQLException {
        Task task = new Task();
        task.setId(resultSet.getLong("id"));
        task.setName(resultSet.getString("name"));
        task.setIndex(resultSet.getInt("index"));
        task.setColumnId(resultSet.getLong("columnId"));
        task.setDescription(resultSet.getString("description"));
        task.setCreatedAt(resultSet.getDate("createdAt").toLocalDate());

        return task;
    }
}
