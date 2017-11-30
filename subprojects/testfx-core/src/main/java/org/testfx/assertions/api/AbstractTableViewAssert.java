/*
 * Copyright 2013-2014 SmartBear Software
 * Copyright 2014-2018 The TestFX Contributors
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the
 * European Commission - subsequent versions of the EUPL (the "Licence"); You may
 * not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the Licence for the
 * specific language governing permissions and limitations under the Licence.
 */
package org.testfx.assertions.api;

import javafx.scene.control.TableView;

import org.testfx.matcher.control.TableViewMatchers;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.assertions.impl.Adapter.fromInverseMatcher;
import static org.testfx.assertions.impl.Adapter.fromMatcher;

/**
 * Base class for all {@link javafx.scene.control.TableView} assertions.
 */
public class AbstractTableViewAssert<SELF extends AbstractTableViewAssert<SELF, T>, T>
        extends AbstractNodeAssert<SELF> {

    protected AbstractTableViewAssert(TableView<T> actual, Class<?> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.TableView} contains the given table cell
     * {@code expectedValue}.
     *
     * @param expectedValue the given table cell value to ensure the {@code TableView} contains
     * @return this assertion object
     */
    public SELF hasTableCell(Object expectedValue) {
        assertThat(actual).is(fromMatcher(TableViewMatchers.hasTableCell(expectedValue)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.TableView} does not contain the given table
     * cell {@code expectedValue}.
     *
     * @param expectedValue the given table cell value to ensure the {@code TableView} does not contain
     * @return this assertion object
     */
    public SELF doesNotHaveTableCell(Object expectedValue) {
        assertThat(actual).is(fromInverseMatcher(TableViewMatchers.hasTableCell(expectedValue)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.TableView} has exactly the given {@code amount}
     * of rows.
     *
     * @param rows the given amount of rows to compare the actual amount of rows to
     * @return this assertion object
     */
    public SELF hasExactlyNumRows(int rows) {
        assertThat(actual).is(fromMatcher(TableViewMatchers.hasNumRows(rows)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.TableView} does not have exactly the given
     * {@code amount} of rows.
     *
     * @param rows the given amount of rows to compare the actual amount of rows to
     * @return this assertion object
     */
    public SELF doesNotHaveExactlyNumRows(int rows) {
        assertThat(actual).is(fromInverseMatcher(TableViewMatchers.hasNumRows(rows)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.TableView} contains the given table {@code cells}
     * at the given {@code rowIndex}.
     * <p>
     * For example, given a {@code TableView} that has three columns:
     * <pre>{@code
     * TableColumn<RegularPolygon, String> nameColumn = new TableColumn<>("Name");
     * TableColumn<RegularPolygon, Integer> numSidesColumn = new TableColumn<>("Number of Sides");
     * TableColumn<RegularPolygon, Double> unitAreaColumn = new TableColumn<>("Area when Side = 1");
     * polygonsTable.getColumns().setAll(nameColumn, numSidesColumn, unitAreaColumn);
     * }</pre>
     * Then to verify that such a {@code TableView}, contains, at index 3, a row for a {@code RegularPolygon}
     * that has the name {@literal "Pentagon"}, the number of sides {@literal 5}, and a unit area of
     * {@literal 1.720477401} one would use:
     * <pre>{@code
     * assertThat(polygonsTable).containsRowAtIndex(3, "Pentagon", 5, 1.720477401);
     * }</pre>
     * Where the types of each argument, after the row index, correspond to the types of the {@code TableColumn}s
     * which in our example is {@code (String, Integer, Double)}.
     *
     * @param rowIndex the given row index that the actual {@code TableView} should contain the given cells on
     * @param cells the cells that should be contained at the given row index
     * @return this assertion object
     */
    public SELF containsRowAtIndex(int rowIndex, Object... cells) {
        assertThat(actual).is(fromMatcher(TableViewMatchers.containsRowAtIndex(rowIndex, cells)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.TableView} does not contain the given
     * table {@code cells} at the given {@code rowIndex}.
     *
     * @param rowIndex the given row index that the actual {@code TableView} should not contain
     * the given cells on
     * @param cells the cells that should not be contained at the given row index
     * @return this assertion object
     */
    public SELF doesNotContainRowAtIndex(int rowIndex, Object... cells) {
        assertThat(actual).is(fromInverseMatcher(TableViewMatchers.containsRowAtIndex(rowIndex, cells)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.TableView} contains the given table {@code cells}
     * at any row index.
     * <p>
     * For example, given a {@code TableView} that has three columns:
     * <pre>{@code
     * TableColumn<Person, String> nameColumn = new TableColumn<>("Name");
     * TableColumn<Person, Double> bmiColumn = new TableColumn<>("Body Mass Index");
     * TableColumn<Person, Boolean> membershipColumn = new TableColumn<>("Gym Membership Valid");
     * fitnessTable.getColumns().setAll(nameColumn, bmiColumn, membershipColumn);
     * }</pre>
     * Then to verify that such a {@code TableView}, contains at least one row with a {@code Person}
     * that has the name {@literal "Dan Anderson"}, the body mass index {@literal 28.83}, and a valid
     * gym membership ({@literal true}) one would use:
     * <pre>{@code
     * assertThat(fitnessTable).containsRow("Dan Anderson", 28.83, true);
     * }</pre>
     * Where the types of each argument correspond to the types of the {@code TableColumn}s which
     * in our example is {@code (String, Double, Boolean)}.
     *
     * @param cells the cells that should be contained at any (at least one) row index
     * @return this assertion object
     */
    public SELF containsRow(Object...cells) {
        assertThat(actual).is(fromMatcher(TableViewMatchers.containsRow(cells)));
        return myself;
    }

    /**
     * Verifies that the actual {@link javafx.scene.control.TableView} does not contain the given
     * table {@code cells} at any row index.
     *
     * @param cells the cells that should not be contained at any (at least one) row index
     * @return this assertion object
     */
    public SELF doesNotContainRow(Object... cells) {
        assertThat(actual).is(fromInverseMatcher(TableViewMatchers.containsRow(cells)));
        return myself;
    }
}
