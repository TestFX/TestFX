package org.loadui.testfx;

import com.google.common.base.Predicate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBoxBuilder;
import javafx.util.Callback;
import org.junit.Test;

import static javafx.collections.FXCollections.observableArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.TableViews.containsCell;
import static org.loadui.testfx.controls.TableViews.numberOfRowsIn;

public class TableViewsTest extends GuiTest
{

	@Test
	public void shouldFindCellValues()
	{
		verifyThat( ".table-view", containsCell( "9" ) );
		verifyThat( ".table-view", not( containsCell( "1" ) ) );

		TableView<?> tableView = find( ".table-view" );
		verifyThat( tableView, containsCell( 9 ) );
	}

	// If you're on Java 8, use lambda instead of an anonymous class.
	@Test
	public void shouldFindCellValues_usingPredicate()
	{
		verifyThat( ".table-view", containsCell( new Predicate<String>()
		{
			@Override
			public boolean apply( String s )
			{
				return Integer.parseInt( s ) == 9;
			}
		} ) );
	}

	@Test
	public void shouldHaveCorrectNumberOfRows()
	{
		verifyThat( numberOfRowsIn( ".table-view" ), is( 3 ) );

		TableView<?> tableView = find( ".table-view" );
		verifyThat( numberOfRowsIn( tableView ), is( 3 ) );
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
