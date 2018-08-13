/**
 * This package contains all base implementations of test cases a framework
 * needs to provide to the end user. All test cases are abstract, as they need
 * to be extended by the specific test framework in use. The adaption is
 * required to call the methods specified in the TestCase interface<br>
 * The adaption of following classes should be provided by a test framework
 * implementation:
 * <ul>
 * <li>ComponentTestBase - A test case to test any type of component (Node or
 * extending classes)</li>
 * <li>StageTestBase - A test case for stages</li>
 * <li>ApplicationClassTestBase - A test case for Applications</li>
 * </ul>
 * 
 */

package org.testfx.testcase.api;