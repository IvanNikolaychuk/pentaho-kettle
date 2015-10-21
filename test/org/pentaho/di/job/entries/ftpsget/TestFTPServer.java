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

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.Collections;


public class TestFTPServer {

  public static final String USER = "user";
  public static final String PASSWORD = "user";
  public static final String DEFAULT_PORT = "8009";

  private FtpServer server;


  public static TestFTPServer createDefault() throws Exception {
    return new TestFTPServer( DEFAULT_PORT, USER, PASSWORD );
  }

  public TestFTPServer( String port, String username, String password ) throws Exception {
    this.server = createServer( port, username, password );
  }

  private FtpServer createServer( String port, String username, String password ) throws Exception {
    FtpServerFactory serverFactory = new FtpServerFactory();
    ListenerFactory factory = new ListenerFactory();
    // set the port of the listener
    factory.setPort( Integer.parseInt( port ) );
    // replace the default listener
    serverFactory.addListener( "default", factory.createListener() );
    // start the server

    PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
    UserManager userManager = userManagerFactory.createUserManager();
    BaseUser user = new BaseUser();
    user.setName( username );
    user.setPassword( password );
    user.setEnabled( true );
    user.setAuthorities( Collections.<Authority>singletonList( new WritePermission() ) );
    userManager.save( user );

    serverFactory.setUserManager( userManager );

    return serverFactory.createServer();
  }

  public void start() throws Exception {
    server.start();
  }

  public void stop() throws Exception {
    if ( server != null ) {
      server.stop();
    }
  }

}
