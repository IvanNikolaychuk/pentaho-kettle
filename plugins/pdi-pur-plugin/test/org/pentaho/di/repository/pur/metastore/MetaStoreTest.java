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

package org.pentaho.di.repository.pur.metastore;

import org.junit.Test;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.pur.PurRepositoryBaseTest;
import org.pentaho.metastore.api.IMetaStore;
import org.pentaho.metastore.api.IMetaStoreAttribute;
import org.pentaho.metastore.api.IMetaStoreElement;
import org.pentaho.metastore.api.IMetaStoreElementType;
import org.pentaho.metastore.api.exceptions.MetaStoreDependenciesExistsException;
import org.pentaho.metastore.api.exceptions.MetaStoreException;
import org.pentaho.metastore.api.exceptions.MetaStoreNamespaceExistsException;
import org.pentaho.metastore.util.PentahoDefaults;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ivan_Nikolaichuk on 9/28/2015.
 */
public class MetaStoreTest extends PurRepositoryBaseTest {

  @Test
  public void testMetaStoreBasics() throws MetaStoreException {
    IMetaStore metaStore = purRepository.getMetaStore();
    assertNotNull( metaStore );

    MetaStoreTestBase base = new MetaStoreTestBase();
    base.testFunctionality( metaStore );
  }

  @Test
  public void testMetaStoreNamespaces() throws MetaStoreException {
    IMetaStore metaStore = purRepository.getMetaStore();
    assertNotNull( metaStore );

    // We start with a clean slate, only the pentaho namespace
    //
    assertEquals( 1, metaStore.getNamespaces().size() );

    String ns = PentahoDefaults.NAMESPACE;
    assertEquals( true, metaStore.namespaceExists( ns ) );

    metaStore.deleteNamespace( ns );
    assertEquals( false, metaStore.namespaceExists( ns ) );
    assertEquals( 0, metaStore.getNamespaces().size() );

    metaStore.createNamespace( ns );
    assertEquals( true, metaStore.namespaceExists( ns ) );

    List<String> namespaces = metaStore.getNamespaces();
    assertEquals( 1, namespaces.size() );
    assertEquals( ns, namespaces.get( 0 ) );

    try {
      metaStore.createNamespace( ns );
      fail( "Exception expected when a namespace already exists and where we try to create it again" );
    } catch ( MetaStoreNamespaceExistsException e ) {
      // OK, we expected this.
    }

    metaStore.deleteNamespace( ns );
    assertEquals( false, metaStore.namespaceExists( ns ) );
    assertEquals( 0, metaStore.getNamespaces().size() );
  }

  @Test
  public void testMetaStoreElementTypes() throws MetaStoreException {
    IMetaStore metaStore = purRepository.getMetaStore();
    assertNotNull( metaStore );
    String ns = PentahoDefaults.NAMESPACE;

    // We start with a clean slate...
    //
    assertEquals( 1, metaStore.getNamespaces().size() );
    assertEquals( true, metaStore.namespaceExists( ns ) );

    // Now create an element type
    //
    IMetaStoreElementType elementType = metaStore.newElementType( ns );
    elementType.setName( PentahoDefaults.KETTLE_DATA_SERVICE_ELEMENT_TYPE_NAME );
    elementType.setDescription( PentahoDefaults.KETTLE_DATA_SERVICE_ELEMENT_TYPE_DESCRIPTION );

    metaStore.createElementType( ns, elementType );

    IMetaStoreElementType verifyElementType = metaStore.getElementType( ns, elementType.getId() );
    assertEquals( PentahoDefaults.KETTLE_DATA_SERVICE_ELEMENT_TYPE_NAME, verifyElementType.getName() );
    assertEquals( PentahoDefaults.KETTLE_DATA_SERVICE_ELEMENT_TYPE_DESCRIPTION, verifyElementType.getDescription() );

    verifyElementType = metaStore.getElementTypeByName( ns, PentahoDefaults.KETTLE_DATA_SERVICE_ELEMENT_TYPE_NAME );
    assertEquals( PentahoDefaults.KETTLE_DATA_SERVICE_ELEMENT_TYPE_NAME, verifyElementType.getName() );
    assertEquals( PentahoDefaults.KETTLE_DATA_SERVICE_ELEMENT_TYPE_DESCRIPTION, verifyElementType.getDescription() );

    // Get the list of element type ids.
    //
    List<String> ids = metaStore.getElementTypeIds( ns );
    assertNotNull( ids );
    assertEquals( 1, ids.size() );
    assertEquals( elementType.getId(), ids.get( 0 ) );

    // Verify that we can't delete the namespace since it has content in it!
    //
    try {
      metaStore.deleteNamespace( ns );
      fail( "The namespace deletion didn't cause an exception because there are still an element type in it" );
    } catch ( MetaStoreDependenciesExistsException e ) {
      assertNotNull( e.getDependencies() );
      assertEquals( 1, e.getDependencies().size() );
      assertEquals( elementType.getId(), e.getDependencies().get( 0 ) );
    }

    metaStore.deleteElementType( ns, elementType );
    assertEquals( 0, metaStore.getElementTypes( ns ).size() );

    metaStore.deleteNamespace( ns );
  }

  @Test
  public void testMetaStoreElements() throws MetaStoreException {
    // Set up a namespace
    //
    String ns = PentahoDefaults.NAMESPACE;
    IMetaStore metaStore = purRepository.getMetaStore();
    if ( !metaStore.namespaceExists( ns ) ) {
      metaStore.createNamespace( ns );
    }

    // And an element type
    //
    IMetaStoreElementType elementType = metaStore.newElementType( ns );
    elementType.setName( PentahoDefaults.KETTLE_DATA_SERVICE_ELEMENT_TYPE_NAME );
    elementType.setDescription( PentahoDefaults.KETTLE_DATA_SERVICE_ELEMENT_TYPE_DESCRIPTION );
    metaStore.createElementType( ns, elementType );

    // Now we play with elements...
    //
    IMetaStoreElement oneElement = populateElement( metaStore, elementType, "Element One" );
    metaStore.createElement( ns, elementType, oneElement );

    IMetaStoreElement verifyOneElement = metaStore.getElement( ns, elementType, oneElement.getId() );
    assertNotNull( verifyOneElement );
    validateElement( verifyOneElement, "Element One" );

    assertEquals( 1, metaStore.getElements( ns, elementType ).size() );

    IMetaStoreElement twoElement = populateElement( metaStore, elementType, "Element Two" );
    metaStore.createElement( ns, elementType, twoElement );

    IMetaStoreElement verifyTwoElement = metaStore.getElement( ns, elementType, twoElement.getId() );
    assertNotNull( verifyTwoElement );

    assertEquals( 2, metaStore.getElements( ns, elementType ).size() );

    try {
      metaStore.deleteElementType( ns, elementType );
      fail( "Delete element type failed to properly detect element dependencies" );
    } catch ( MetaStoreDependenciesExistsException e ) {
      List<String> ids = e.getDependencies();
      assertEquals( 2, ids.size() );
      assertTrue( ids.contains( oneElement.getId() ) );
      assertTrue( ids.contains( twoElement.getId() ) );
    }

    metaStore.deleteElement( ns, elementType, oneElement.getId() );

    assertEquals( 1, metaStore.getElements( ns, elementType ).size() );

    metaStore.deleteElement( ns, elementType, twoElement.getId() );

    assertEquals( 0, metaStore.getElements( ns, elementType ).size() );
  }

  protected IMetaStoreElement populateElement( IMetaStore metaStore, IMetaStoreElementType elementType, String name )
    throws MetaStoreException {
    IMetaStoreElement element = metaStore.newElement();
    element.setElementType( elementType );
    element.setName( name );
    for ( int i = 1; i <= 5; i++ ) {
      element.addChild( metaStore.newAttribute( "id " + i, "value " + i ) );
    }
    IMetaStoreAttribute subAttr = metaStore.newAttribute( "sub-attr", null );
    for ( int i = 101; i <= 110; i++ ) {
      subAttr.addChild( metaStore.newAttribute( "sub-id " + i, "sub-value " + i ) );
    }
    element.addChild( subAttr );

    return element;
  }

  protected void validateElement( IMetaStoreElement element, String name ) throws MetaStoreException {
    assertEquals( name, element.getName() );
    assertEquals( 6, element.getChildren().size() );
    for ( int i = 1; i <= 5; i++ ) {
      IMetaStoreAttribute child = element.getChild( "id " + i );
      assertEquals( "value " + i, child.getValue() );
    }
    IMetaStoreAttribute subAttr = element.getChild( "sub-attr" );
    assertNotNull( subAttr );
    assertEquals( 10, subAttr.getChildren().size() );
    for ( int i = 101; i <= 110; i++ ) {
      IMetaStoreAttribute child = subAttr.getChild( "sub-id " + i );
      assertNotNull( child );
      assertEquals( "sub-value " + i, child.getValue() );
    }
  }

}
