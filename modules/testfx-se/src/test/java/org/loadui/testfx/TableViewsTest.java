package org.loadui.testfx;

import com.google.common.base.Predicate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBoxBuilder;
import javafx.util.Callback;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNot;
import org.junit.Test;
import org.loadui.testfx.controls.TableViews;
import org.loadui.testfx.framework.robot.FxRobot;

import static javafx.collections.FXCollections.observableArrayList;

public class TableViewsTest extends GuiTest
{

	@Test
	public void shouldFindCellValues()
	{
		Assertions.verifyThat(".table-view", TableViews.containsCell("9"));
		Assertions.verifyThat(".table-view", IsNot.not(TableViews.containsCell("1")));

		TableView<?> tableView = FxRobot.find(".table-view");
		Assertions.verifyThat(tableView, TableViews.containsCell(9));
	}

	// If you're on Java 8, use lambda instead of an anonymous class.
	@Test
	public void shouldFindCellValues_usingPredicate()
	{
		Assertions.verifyThat(".table-view", TableViews.containsCell(new Predicate<String>() {
            @Override
            public boolean apply(String s) {
                return Integer.parseInt(s) == 9;
            }
        }));
	}

	@Test
	public void shouldHaveCorrectNumberOfRows()
	{
		Assertions.verifyThat(TableViews.numberOfRowsIn(".table-view"), CoreMatchers.is(3));

		TableView<?> tableView = FxRobot.find(".table-view");
		Assertions.verifyThat(TableViews.numberOfRowsIn(tableView), CoreMatchers.is(3));
	}

	@Override
	protected Parent getRootNode()
	{
		TableView<Integer> table = new TableView<>();
		table.setItems( observableArrayList( 1, 2, 3 ) );
		TableColumn<Integer, Integer> column = new TableColumn<>( "col" );
		column.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<Integer, Integer>, ObservableValue<Integer>>()
		{
			@Override
			public ObservableValue<Integer> call( TableColumn.CellDataFeatures<Integer, Integer> f )
			{
				return new SimpleObjectProperty<Integer>( f.getValue() * 3 );
			}
		} );
		table.getColumns().add( column );
		return VBoxBuilder
				.create()
				.children( table ).build();
	}
}
