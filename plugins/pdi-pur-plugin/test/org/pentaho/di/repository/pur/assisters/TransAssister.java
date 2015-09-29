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