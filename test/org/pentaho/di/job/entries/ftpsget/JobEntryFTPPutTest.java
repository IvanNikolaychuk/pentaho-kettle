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

package org.pentaho.di.job.entries.ftpsget;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.pentaho.di.job.entries.ftpsget.TestFtpServer.DEFAULT_PORT;
import static org.pentaho.di.job.entries.ftpsget.TestFtpServer.USER;
import static org.pentaho.di.job.entries.ftpsget.TestFtpServer.PASSWORD;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Result;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.ftpput.JobEntryFTPPUT;
import org.pentaho.di.job.entry.JobEntryCopy;
import java.io.IOException;


public class JobEntryFtpPutTest {
  public static TemporaryFolder folder;
  private final static String FILE = "test.txt";

  private static TestFtpServer server;
  private Job job;
  private JobEntryFTPPUT entry;
  private FTPSConnection connection;

  @BeforeClass
  public static void createServer() throws Exception {
    KettleEnvironment.init();

    server = TestFtpServer.createDefault();
    server.start();
  }

  @AfterClass
  public static void stopServer() throws Exception {
    if ( server != null ) {
      server.stop();
      server = null;
    }
  }

  @Before
  public void setUp() throws Exception {
    folder = new TemporaryFolder();
    folder.create();
    folder.newFile( FILE );

    connection =
      new FTPSConnection( FTPSConnection.CONNECTION_TYPE_FTP, "localhost", Integer.parseInt( DEFAULT_PORT ), USER,
        PASSWORD );
    connection.connect();

    String serverBaseDir = "file:////" + folder.getRoot();

    job = new Job( null, new JobMeta() );
    job.getJobMeta().addJobEntry( new JobEntryCopy( entry ) );
    job.setStopped( false );

    entry = new JobEntryFTPPUT();
    entry.setParentJob( job );
    entry.setUserName( USER );
    entry.setPassword( PASSWORD );
    entry.setLocalDirectory( "${" + org.pentaho.di.core.Const.INTERNAL_VARIABLE_JOB_FILENAME_DIRECTORY + "}" );
    entry.setServerName( "127.0.0.1" );
    entry.setServerPort( DEFAULT_PORT );
    entry.setActiveConnection( true );
    entry.setControlEncoding( "UTF-8" );
    entry.setBinaryMode( true );
    entry.setWildcard( ".*txt" );
    entry.setVariable( "Internal.Job.Filename.Directory", serverBaseDir );
  }


  @After
  public void tearDown() throws IOException {
    if ( connection != null ) {
      connection.disconnect();
      connection = null;
    }
    entry = null;
    job = null;
    folder.delete();
  }

  @Test
  public void fileIsPutInFTPServer() throws Exception {
    if ( connection.isFileExists( FILE ) ) {
      connection.deleteFile( FILE );
    }

    assertFalse( connection.isFileExists( FILE ) );
    entry.execute( new Result(), 0 );
    assertTrue( connection.isFileExists( FILE ) );
  }

}
