/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.trans.steps.googleanalytics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Pavel Sakun
 */
@RunWith( Parameterized.class )
public class GoogleAnalyticsApiFacadeTest {
  private String path;

  public GoogleAnalyticsApiFacadeTest( String path ) {
    this.path = path;
  }

  @Parameterized.Parameters
  public static Collection primeNumbers() {
    return Arrays.asList(
      new String[][] {
        { "C:/key.p12" },
        { "C:\\key.p12" },
        { "/key.p12" },
        { "file:///C:/key.p12" },
        { "file:///C:\\key.p12" },
        //      { "file:///key.p12" } - Path is incorrect.
      } );
  }

  @Test( expected = FileNotFoundException.class )
  public void test() throws Exception {
    GoogleAnalyticsApiFacade.createFor( "application-name", "account", path );
  }

}
