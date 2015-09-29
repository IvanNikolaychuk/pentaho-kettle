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
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.trans.TransMeta;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;


public class TransAssister extends BaseAssister {


  @Override public void rename( AbstractMeta meta, String newName ) throws KettleException {
    repository.renameTransformation( meta.getObjectId(), meta.getRepositoryDirectory(), newName );
  }

  @Override public void rename( ObjectId metaId, RepositoryDirectoryInterface destFolder, String newName )
    throws KettleException {
    repository.renameTransformation( metaId, destFolder, newName );
  }

  @Override public void move( ObjectId metaId, RepositoryDirectoryInterface destFolder ) throws KettleException {
    repository.renameTransformation( metaId, destFolder, null );
  }

  @Override public void assertExistsIn( RepositoryDirectoryInterface dir, String name, String message )
    throws Exception {
    List<String> existingTrans = asList( repository.getTransformationNames( dir.getObjectId(), false ) );
    assertThat( message, existingTrans, hasItem( name ) );
  }

  @Override public String getMetaType() {
    return "Trans";
  }

  @Override public TransMeta getMeta() {
    return new TransMeta(  );
  }
}