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

package org.pentaho.di.repository.pur;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.base.AbstractMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.platform.api.repository2.unified.VersionSummary;
import  org.pentaho.di.repository.pur.assisters.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PurRepository_MoveAndRename_Test extends PurRepositoryTest {
  private final JobAssister jobAssister = new JobAssister();
  private final TransAssister transAssister = new TransAssister();

  @Before
  public void setUp() {
    BaseAssister.setRepository( repository );
  }

  @Test
  public void renameJob_Successfully() throws Exception {
    rename_Successfully( jobAssister );
  }

  @Test
  public void renameTrans_Successfully() throws Exception {
    rename_Successfully( transAssister );
  }

  private  void rename_Successfully( BaseAssister assister ) throws Exception {
    final String initial =  assister.getMetaType() + "rename_Successfully";
    final String renamed = initial + "_renamed";
    AbstractMeta meta = assister.getMeta();

    RepositoryDirectoryInterface directory = getPublicDir();
    assister.save( meta, initial, directory );

    assister.rename( meta, renamed );
    assister.assertExistsIn( directory, renamed, assister.getMetaType() + " was not renamed" );
  }

  @Test
  public void renameJob_CreatesNewRevision() throws Exception {
    rename_CreatesNewRevision( jobAssister );
  }

  @Test
  public void renameTrans_CreatesNewRevision() throws Exception {
    rename_CreatesNewRevision( transAssister );
  }

  private void rename_CreatesNewRevision( BaseAssister assister ) throws Exception {
    final String initial = assister.getMetaType() + "rename_CreatesNewRevision";
    final String renamed = initial + "_renamed";
    AbstractMeta meta = assister.getMeta();

    assister.save( meta, initial, getPublicDir() );

    List<VersionSummary> historyBefore = repo.getVersionSummaries( meta.getObjectId().getId() );
    repository.renameTransformation( meta.getObjectId(), meta.getRepositoryDirectory(), renamed );
    List<VersionSummary> afterBefore = repo.getVersionSummaries( meta.getObjectId().getId() );
    assertEquals( historyBefore.size() + 1, afterBefore.size() );
  }


  @Test( expected = KettleException.class )
  public void renameJob_FailsIfANameConflictOccurs() throws Exception {
    rename_FailsIfANameConflictOccurs( jobAssister );
  }

  @Test( expected = KettleException.class )
  public void renameTrans_FailsIfANameConflictOccurs() throws Exception {
    rename_FailsIfANameConflictOccurs( transAssister );
  }

  private void rename_FailsIfANameConflictOccurs( BaseAssister assister ) throws Exception {
    final String name = assister.getMetaType() + "rename_FailsIfANameConflictOccurs";
    AbstractMeta  meta = assister.getMeta();

    assister.save( meta, name, getPublicDir() );

    assister.rename( meta, name );
  }


  @Test
  public void moveJob_Successfully() throws Exception {
    move_Successfully( jobAssister );
  }

  @Test
  public void moveTrans_Successfully() throws Exception {
    move_Successfully( transAssister );
  }

  private void move_Successfully( BaseAssister assister ) throws Exception {
    final String filename = assister.getMetaType() + "move_Successfully";
    AbstractMeta meta = assister.getMeta();

    assister.save( meta, filename, getPublicDir() );

    RepositoryDirectoryInterface destFolder = getDirInsidePublic( filename );
    assertNotNull( destFolder );

    assister.move( meta.getObjectId(), destFolder );
    assister.assertExistsIn( destFolder, filename, assister.getMetaType() + " was not renamed" );
  }

  @Test
  public void moveJob_DoesNotCreateRevision() throws Exception {
    move_DoesNotCreateRevision( jobAssister );
  }

  @Test
  public void moveTrans_DoesNotCreateRevision() throws Exception {
    move_DoesNotCreateRevision( transAssister );
  }

  private <T extends AbstractMeta> void move_DoesNotCreateRevision( BaseAssister assister ) throws Exception {
    final String fileName = assister.getMetaType() + "move_DoesNotCreateRevision";
    AbstractMeta meta = assister.getMeta();

    assister.save( meta, fileName, getPublicDir() );


    List<VersionSummary> historyBefore = repo.getVersionSummaries( meta.getObjectId().getId() );
    repository.renameTransformation( meta.getObjectId(), getDirInsidePublic( fileName ), null );
    List<VersionSummary> historyAfter = repo.getVersionSummaries( meta.getObjectId().getId() );

    assertEquals( historyBefore.size(), historyAfter.size() );

  }


  @Test( expected = KettleException.class )
  public void moveJob_FailsIfANameConflictOccurs() throws Exception {
    move_FailsIfANameConflictOccurs( jobAssister );
  }

  @Test( expected = KettleException.class )
  public void moveTrans_FailsIfANameConflictOccurs() throws Exception {
    move_FailsIfANameConflictOccurs( transAssister );
  }

  private <T extends AbstractMeta> void move_FailsIfANameConflictOccurs( BaseAssister assister ) throws Exception {
    final String fileName = assister.getMetaType() + "move_FailsIfANameConflictOccurs";
    AbstractMeta meta = assister.getMeta();
    AbstractMeta anotherMeta = assister.getMeta();

    RepositoryDirectoryInterface directory = getPublicDir();
    assister.save( meta, fileName, directory );

    RepositoryDirectoryInterface destFolder = getDirInsidePublic( fileName );
    assister.save( anotherMeta, fileName, destFolder );

    assister.move( meta.getObjectId(), destFolder );

  }


  @Test
  public void moveAndRenameJob_Successfully() throws Exception {
    moveAndRename_Successfully( jobAssister );
  }

  @Test
  public void moveAndRenameTrans_Successfully() throws Exception {
    moveAndRename_Successfully( transAssister );

  }

  private <T extends AbstractMeta> void moveAndRename_Successfully( BaseAssister assister ) throws Exception {
    final String fileName = assister.getMetaType() + "moveAndRename_Successfully";
    final String renamed = fileName + "_renamed";
    AbstractMeta meta = assister.getMeta();

    assister.save( meta, fileName, getPublicDir() );

    RepositoryDirectoryInterface destFolder = getDirInsidePublic( fileName );
    assertNotNull( destFolder );

    assister.rename( meta.getObjectId(), destFolder, renamed );
    assister.assertExistsIn( destFolder, renamed, assister.getMetaType() + " was not renamed" );
  }


  @Test( expected = KettleException.class )
  public void moveAndRenameTrans_FailsIfANameConflictOccurs() throws Exception {
    moveAndRename_FailsIfANameConflictOccurs( transAssister );
  }

  @Test( expected = KettleException.class )
  public void moveAndRenameJob_FailsIfANameConflictOccurs() throws Exception {
    moveAndRename_FailsIfANameConflictOccurs( jobAssister );
  }

  private <T extends AbstractMeta> void moveAndRename_FailsIfANameConflictOccurs( BaseAssister assister ) throws Exception {
    final String fileName = assister.getMetaType() + "moveAndRename_FailsIfANameConflictOccurs";
    final String renamed = fileName + "_renamed";
    AbstractMeta meta = assister.getMeta();
    AbstractMeta anotherMeta = assister.getMeta();

    RepositoryDirectoryInterface directory = getPublicDir();
    assister.save( meta, fileName, directory );

    RepositoryDirectoryInterface destFolder = getDirInsidePublic( fileName );
    assister.save( anotherMeta, fileName, destFolder );

    assister.rename( meta.getObjectId(), destFolder, renamed );
  }

  private RepositoryDirectoryInterface getPublicDir() throws Exception {
    return repository.findDirectory( "public" );
  }

  private RepositoryDirectoryInterface getDirInsidePublic( String dirName ) throws Exception {
    RepositoryDirectoryInterface child = getPublicDir().findChild( dirName );
    return ( child == null ) ? repository.createRepositoryDirectory( getPublicDir(), dirName ) : child;
  }



}
