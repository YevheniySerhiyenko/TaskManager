package com.taskmanager.service;

import com.taskmanager.exception.NotEmptyException;
import com.taskmanager.exception.NotFoundException;
import com.taskmanager.exception.WrongArgumentException;
import com.taskmanager.model.Column;
import com.taskmanager.repository.ColumnRepository;
import com.taskmanager.repository.TaskRepository;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

public class ColumnServiceImplTest {
    private ColumnRepository columnRepository;
    public ColumnService columnService;
    private TaskRepository taskRepository;

    @BeforeMethod
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        columnRepository = Mockito.mock(ColumnRepository.class);
        columnService = new ColumnServiceImpl(columnRepository, taskRepository);
    }

    @Test
    public void createColumn_ShouldCreateColumn() {
        Column actual = new Column();
        actual.setId(1L);
        actual.setName("column");
        actual.setIndex(1);

        Mockito
                .when(columnRepository.existsById(1L))
                .thenReturn(false);
        Mockito
                .when(columnRepository.findById(1L))
                .thenReturn(Optional.of(actual));
        Mockito
                .when(columnRepository.findByIndex(actual.getIndex()))
                .thenReturn(Optional.of(actual));

        Mockito
                .when(columnRepository.create(actual))
                .thenReturn(1L);

        Column expected = columnService.create("column");

        Assert.assertEquals(actual, expected);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void updateColumn_ColumnNotExist_ExceptionThrown() {
        columnService.update(1L, "column");
    }

    @Test
    public void updateColumn_ShouldUpdateColumn() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");

        Mockito
                .when(columnRepository.findById(1L))
                .thenReturn(Optional.of(column));

        Column actual = columnService.update(1L, "columnUpdate");

        Column expected = new Column();
        expected.setId(1L);
        expected.setName("columnUpdate");

        Assert.assertEquals(actual, expected);

        Mockito
                .verify(columnRepository, Mockito.times(1))
                .update(column);
    }

    @Test(expectedExceptions = NotEmptyException.class)
    public void deleteColumn_ColumnNotEmpty_ExceptionThrown() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        Mockito
                .when(columnRepository.findById(1L))
                .thenReturn(Optional.of(column));
        Mockito
                .when(taskRepository.existByColumnId(1L))
                .thenReturn(true);

        columnService.delete(1L);
    }

    @Test
    public void deleteColumn_ShouldDeleteColumn() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        Mockito
                .when(columnRepository.existsById(1L))
                .thenReturn(true);
        Mockito
                .when(columnRepository.findById(1L))
                .thenReturn(Optional.of(column));

        columnService.delete(1L);

        Mockito
                .verify(columnRepository, Mockito.times(1))
                .delete(1L);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void findById_ColumnNotExist_ExceptionThrown() {
        columnService.findById(1L);
    }

    @Test
    public void findById_ShouldReturnColumn() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        Mockito
                .when(columnRepository.findById(1L))
                .thenReturn(Optional.of(column));

        Column actual = columnService.findById(1L);

        Assert.assertEquals(actual, column);
    }

    @Test
    public void findAllColumns_ShouldReturnListColumns() {
        Column column = new Column();
        column.setId(1L);
        column.setName("column");
        column.setIndex(1);

        Mockito
                .when(columnRepository.findAll())
                .thenReturn(List.of(column));

        List<Column> actual = columnService.findAll();

        List<Column> expected = List.of(column);

        Assert.assertEquals(actual, expected);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void changeIndex_ColumnNotExists_ExceptionThrown() {
        columnService.changeIndex(1L, 2);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void changeIndex_ColumnWithOrderNotExist_ExceptionThrown() {
        Mockito
                .when(columnRepository.existsById(1L))
                .thenReturn(true);

        columnService.changeIndex(1L, 25);
    }

    //not finished
    @Test
    public void changeColumnIndex_ShouldChangeIndex() {
        Column actual = new Column();
        actual.setId(1L);
        actual.setName("column1");
        actual.setIndex(2);

        Mockito
                .when(columnRepository.existsById(1L))
                .thenReturn(true);
        Mockito
                .when(columnRepository.findById(1L))
                .thenReturn(Optional.of(actual));

        Mockito
                .when(columnRepository.getCount())
                .thenReturn(2);

        columnService.changeIndex(1L, 2);

        Column expected = new Column();
        expected.setId(1L);
        expected.setName("column1");
        expected.setIndex(2);

        Assert.assertEquals(actual, expected);

        Mockito
                .verify(columnRepository, Mockito.times(1))
                .update(actual);
    }
}
