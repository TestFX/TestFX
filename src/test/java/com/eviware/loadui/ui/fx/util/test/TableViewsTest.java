/*
 * Copyright 2013 SmartBear Software
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 */
package com.eviware.loadui.ui.fx.util.test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBoxBuilder;
import javafx.util.Callback;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;

import static javafx.collections.FXCollections.observableArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.Matchers.hasText;
import static org.loadui.testfx.controls.TableViews.containsCell;
import static org.loadui.testfx.controls.TableViews.numberOfRowsIn;

@Category( TestFX.class )
public class TableViewsTest extends GuiTest
{
	@Override
	protected Parent getRootNode()
	{
        TableView<Integer> table = new TableView<>();
        table.setItems(observableArrayList(1, 2, 3));
        TableColumn<Integer, Integer> column = new TableColumn<>("col");
        column.setCellValueFactory( new Callback<TableColumn.CellDataFeatures<Integer, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Integer, Integer> f) {
                return new SimpleObjectProperty( f.getValue() * 3 );
            }
        });
        table.getColumns().add(column);
		return VBoxBuilder
				.create()
				.children( table ).build();
	}

	@Test
	public void shouldFindCellValues()
	{
		verifyThat(".table-view", containsCell("9"));
        verifyThat(".table-view", not(containsCell("1")) );

        TableView<?> tableView = find(".table-view");
        verifyThat(tableView, containsCell(9));
	}

    @Test
    public void shouldHaveCorrectNumberOfRows()
    {
        verifyThat( numberOfRowsIn(".table-view"), is(3));

        TableView<?> tableView = find(".table-view");
        verifyThat( numberOfRowsIn( tableView ), is(3));
    }
}
