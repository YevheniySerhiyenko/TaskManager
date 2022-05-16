package com.taskmanager.service;

import com.taskmanager.exception.NotFoundException;
import com.taskmanager.exception.WrongArgumentException;
import com.taskmanager.model.Column;
import com.taskmanager.model.Task;
import com.taskmanager.repository.ColumnRepository;
import com.taskmanager.repository.TaskRepository;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskServiceImplTest {
    private TaskRepository taskRepository;
    private TaskService taskService;
    private ColumnRepository columnRepository;
    public ColumnService columnService;

    @BeforeMethod
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        columnRepository = Mockito.mock(ColumnRepository.class);
        taskService = new TaskServiceImpl(taskRepository);
        columnService = new ColumnServiceImpl(columnRepository, taskRepository);
    }

    @Test
    public void create_ShouldCreateTask() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        Task actual = new Task();
        actual.setId(1L);
        actual.setName("task");
        actual.setIndex(1);
        actual.setDescription("description");
        actual.setCreatedAt(LocalDate.now());
        actual.setColumnId(1L);


        Mockito
                .when(columnRepository.create(column))
                .thenReturn(1L);
        Mockito
                .when(taskRepository.findById(1L))
                .thenReturn(Optional.of(actual));
        Mockito
                .when(taskRepository.create(actual))
                .thenReturn(1L);

        Task expected = taskService.create(
                "task",
                "description",
                LocalDate.now(),
                column);

        Assert.assertEquals(actual, expected);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void updateTask_TaskNotExist_ExceptionThrown() {
        taskService.update(1L,
                "task",
                "description",
                LocalDate.now()
        );
    }

    @Test
    public void updateTask_ShouldUpdateTask() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        Task task = new Task();
        task.setId(1L);
        task.setName("task");
        task.setIndex(1);
        task.setDescription("description");
        task.setCreatedAt(LocalDate.now());
        task.setColumnId(column.getId());

        Mockito
                .when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        Task actual = taskService.update(1L,
                "task",
                "descriptionUpdate",
                LocalDate.now()
        );

        Task expected = new Task();
        expected.setId(1L);
        expected.setName("task");
        expected.setIndex(1);
        expected.setDescription("descriptionUpdate");
        expected.setCreatedAt(LocalDate.now());
        expected.setColumnId(column.getId());

        Assert.assertEquals(actual, expected);

        Mockito
                .verify(taskRepository, Mockito.times(1))
                .update(task);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void deleteTask_TaskNotExist_ExceptionThrown(){
        taskService.delete(1L);
    }

    @Test
    public void deleteTask_ShouldDeleteTask() {
        Mockito
                .when(taskRepository.existById(1L))
                .thenReturn(true);

        taskService.delete(1L);

        Mockito
                .verify(taskRepository, Mockito.times(1))
                .delete(1L);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void findByColumnAndId_TaskNotExist_ExceptionThrown() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        taskService.findByColumnAndId(column, 1L);
    }

    @Test
    public void findByColumnAndId_ShouldReturnTask() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        Task task = new Task();
        task.setName("task");
        task.setIndex(1);
        task.setDescription("description");
        task.setCreatedAt(LocalDate.now());
        task.setColumnId(1L);

        Mockito
                .when(taskRepository.findByColumnIdAndTaskId(1L,1L))
                .thenReturn(Optional.of(task));

        Task actual = taskService.findByColumnAndId(column, 1L);

        Assert.assertEquals(actual, task);
    }

    @Test
    public void findAllByColumn_ShouldReturnListOfTasks() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        Task task = new Task();
        task.setName("task");
        task.setIndex(1);
        task.setDescription("description");
        task.setCreatedAt(LocalDate.now());
        task.setColumnId(1L);

        Mockito
                .when(taskRepository.findAllByColumn(1L))
                .thenReturn(List.of(task));

        List<Task> actual = taskService.findAllByColumn(column);

        List<Task> expected = List.of(task);

        Assert.assertEquals(actual, expected);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void changeIndex_TaskNotExist_ExceptionThrown() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        taskService.changeIndex(column, 1L, 2);
    }

    @Test(expectedExceptions = WrongArgumentException.class)
    public void changeIndex_TaskWithOrderNotExist_ExceptionThrown() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        Mockito
                .when(taskRepository.existById(1L))
                .thenReturn(true);

        Mockito
                .when(columnRepository.existsById(1L))
                .thenReturn(true);

        taskService.changeIndex(column, 1L, 5);
    }

    @Test
    public void moveToAnotherColumn_ShouldMoveTaskToAnotherColumn() {
        Task actual = new Task();
        actual.setId(1L);
        actual.setName("task");
        actual.setIndex(1);
        actual.setDescription("description");
        actual.setCreatedAt(LocalDate.now());
        actual.setColumnId(1L);

        Column column = new Column();
        column.setId(2L);
        column.setName("column");
        column.setIndex(2);

        Mockito
                .when(columnRepository.findById(2L))
                .thenReturn(Optional.of(column));
        Mockito
                .when(taskRepository.findById(1L))
                .thenReturn(Optional.of(actual));

        taskService.moveToAnotherColumn(1L, column);

        Task expected = new Task();
        expected.setId(1L);
        expected.setName("task");
        expected.setIndex(1);
        expected.setDescription("description");
        expected.setCreatedAt(LocalDate.now());
        expected.setColumnId(2L);

        Assert.assertEquals(actual, expected);
    }

    //not finished
    @Test
    public void changeIndex_ShouldChangeIndex() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        Task task = new Task();
        task.setId(1L);
        task.setName("task");
        task.setIndex(1);
        task.setDescription("description");
        task.setCreatedAt(LocalDate.now());
        task.setColumnId(column.getId());

        Task actual = new Task();
        actual.setId(1L);
        actual.setName("task");
        actual.setIndex(2);
        actual.setDescription("description");
        actual.setCreatedAt(LocalDate.now());
        actual.setColumnId(column.getId());

        Mockito
                .when(columnRepository.existsById(1L))
                .thenReturn(true);
        Mockito
                .when(taskRepository.existById(1L))
                .thenReturn(true);
        Mockito
                .when(taskRepository.findByColumnIdAndTaskId(1L,1L))
                .thenReturn(Optional.of(actual));
        Mockito
                .when(taskRepository.getCountInColumn(1L))
                .thenReturn(2);

        List<Task> taskList = List.of(task, actual);
        Mockito
                .when(taskRepository.findAllByRange(1L,1,2))
                .thenReturn(taskList);

        taskService.changeIndex(column, 1L, 2);

        Task expected = new Task();
        expected.setId(1L);
        expected.setName("task");
        expected.setIndex(2);
        expected.setDescription("description");
        expected.setCreatedAt(LocalDate.now());
        expected.setColumnId(column.getId());

        Assert.assertEquals(actual, expected);

        Mockito
                .verify(taskRepository,Mockito.times(1))
                .updateAll(taskList);
    }
}
