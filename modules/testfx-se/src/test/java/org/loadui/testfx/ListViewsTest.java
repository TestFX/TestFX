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
package org.loadui.testfx;

import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBoxBuilder;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNot;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.categories.TestFX;
import org.loadui.testfx.controls.ListViews;
import org.loadui.testfx.framework.robot.FxRobot;

import static javafx.collections.FXCollections.observableArrayList;

@Category(TestFX.class)
public class ListViewsTest extends GuiTest
{
	@Override
	protected Parent getRootNode()
	{
		ListView<Integer> list = new ListView<>();
		list.setItems( observableArrayList( 1, 2, 3 ) );
		return VBoxBuilder
				.create()
				.children( list ).build();
	}

	@Test
	public void shouldFindRowValues()
	{
		Assertions.verifyThat(".list-view", ListViews.containsRow("1"));
		Assertions.verifyThat(".list-view", IsNot.not(ListViews.containsRow("4")));

		ListView<?> listView = FxRobot.find(".list-view");
		Assertions.verifyThat(listView, ListViews.containsRow(1));
	}

	@Test
	public void shouldHaveCorrectNumberOfRows()
	{
		Assertions.verifyThat(ListViews.numberOfRowsIn(".list-view"), CoreMatchers.is(3));

		ListView<?> listView = FxRobot.find(".list-view");
		Assertions.verifyThat(ListViews.numberOfRowsIn(listView), CoreMatchers.is(3));
	}
}
