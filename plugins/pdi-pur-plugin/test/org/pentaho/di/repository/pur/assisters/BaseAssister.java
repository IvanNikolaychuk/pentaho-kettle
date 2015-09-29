/*!
* Copyright 2010 - 2015 Pentaho Corporation.  All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/

package org.pentaho.di.repository.pur.assisters;

import org.pentaho.di.base.AbstractMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementInterface;

import static org.junit.Assert.assertNotNull;


// there are several methods that were used
// by all Assisters, that's why abstract class is better than interface.
public abstract class BaseAssister {
  protected static Repository repository;

  public static void setRepository( Repository repository ) {
    BaseAssister.repository = repository;
  }

  public void saveElement( RepositoryElementInterface element, String name, RepositoryDirectoryInterface directory )
    throws Exception {
    assertNotNull( directory );

    element.setName( name );
    element.setRepositoryDirectory( directory );
    repository.save( element, null, null );
  }

  public void save( AbstractMeta meta, String name, RepositoryDirectoryInterface directory )
    throws Exception {
    saveElement( meta, name, directory );
    assertExistsIn( directory, name, getMetaType() + " was not saved" );
  }

  public abstract void rename(AbstractMeta meta, String newName) throws KettleException;
  public abstract void rename( ObjectId metaId, RepositoryDirectoryInterface destFolder, String newName ) throws KettleException;
  public abstract void move( ObjectId metaId, RepositoryDirectoryInterface destFolder ) throws KettleException;
  public abstract void assertExistsIn(RepositoryDirectoryInterface dir, String name, String message) throws Exception;

  public abstract String getMetaType();
  public abstract AbstractMeta getMeta();
}
