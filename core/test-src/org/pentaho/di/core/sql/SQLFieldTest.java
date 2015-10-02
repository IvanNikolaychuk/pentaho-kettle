/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2013 by Pentaho : http://www.pentaho.com
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

package org.pentaho.di.core.sql;

import static org.junit.Assert.*;

import org.junit.Test;
import org.pentaho.di.core.Condition;
import org.pentaho.di.core.exception.KettleSQLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;


public class SQLFieldTest {
  public void testSqlFieldWithNoAggregation( String fieldClause, String fieldName, String fieldAlias )
    throws KettleSQLException {
    RowMetaInterface rowMeta = SQLTest.generateTest2RowMeta();

    SQLField field = new SQLField( "Service", fieldClause, rowMeta );

    assertEquals( fieldName, field.getName() );
    assertEquals( fieldAlias, field.getAlias() );
    assertNotNull( "The service data type was not discovered", field.getValueMeta() );
  }

  public void testSqlFieldWithAggregationEqSum( String fieldClause, String fieldName,
                                                String fieldAlias )
    throws KettleSQLException {
    RowMetaInterface rowMeta = SQLTest.generateTest2RowMeta();

    SQLField field = new SQLField( "Service", fieldClause, rowMeta );
    assertEquals( fieldName, field.getName() );
    assertEquals( fieldAlias, field.getAlias() );
    assertEquals( SQLAggregation.SUM, field.getAggregation() );
    assertNotNull( "The service data type was not discovered", field.getValueMeta() );
  }

  public void testSqlFieldWithAggregationEqCount( String fieldClause, String fieldName, String fieldAlias )
    throws KettleSQLException {
    RowMetaInterface rowMeta = SQLTest.generateTest2RowMeta();

    SQLField field = new SQLField( "Service", fieldClause, rowMeta );
    assertEquals( fieldName, field.getName() );
    assertEquals( fieldAlias, field.getAlias() );
    assertEquals( SQLAggregation.COUNT, field.getAggregation() );
    assertNull( field.getValueMeta() );
    assertTrue( field.isCountStar() );
  }

  public void testSqlFieldWithIIF( String fieldClause, String fieldLeftName, String funcDesc, String exactString,
                                   String holeFieldName, String fieldAlias ) throws KettleSQLException {
    RowMetaInterface rowMeta = SQLTest.generateTest2RowMeta();

    SQLField field = new SQLField( "Service", fieldClause, rowMeta );
    assertEquals( holeFieldName, field.getName() );
    assertEquals( fieldAlias, field.getAlias() );
    assertNull( "The service data type was discovered", field.getValueMeta() );

    assertNotNull( field.getIif() );
    Condition condition = field.getIif().getSqlCondition().getCondition();
    assertNotNull( condition );
    assertFalse( condition.isEmpty() );
    assertTrue( condition.isAtomic() );
    assertEquals( fieldLeftName, condition.getLeftValuename() );
    assertEquals( funcDesc, condition.getFunctionDesc() );
    assertEquals( exactString, condition.getRightExactString() );
  }

  @Test
  public void testSqlField01() throws KettleSQLException {
    String fieldClause = "A as foo";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField01Alias() throws KettleSQLException {
    String fieldClause = "Service.A as foo";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField01QuotedAlias() throws KettleSQLException {
    String fieldClause = "\"Service\".A as foo";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField02() throws KettleSQLException {
    String fieldClause = "A as \"foo\"";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField02Alias() throws KettleSQLException {
    String fieldClause = "Service.A as \"foo\"";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField03() throws KettleSQLException {
    String fieldClause = "\"A\" as \"foo\"";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField03Alias() throws KettleSQLException {
    String fieldClause = "Service.\"A\" as \"foo\"";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField04() throws KettleSQLException {
    String fieldClause = "\"A\" \"foo\"";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField04Alias() throws KettleSQLException {
    String fieldClause = "Service.\"A\" \"foo\"";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField05() throws KettleSQLException {
    String fieldClause = "A   as   foo";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField05Alias() throws KettleSQLException {
    String fieldClause = "Service.A   as   foo";

    testSqlFieldWithNoAggregation( fieldClause, "A", "foo" );
  }

  @Test
  public void testSqlField06() throws KettleSQLException {
    String fieldClause = "SUM(B) as total";

    testSqlFieldWithAggregationEqSum( fieldClause, "B", "total" );
  }

  @Test
  public void testSqlField06Alias() throws KettleSQLException {
    String fieldClause = "SUM(Service.B) as total";

    testSqlFieldWithAggregationEqSum( fieldClause, "B", "total" );
  }

  @Test
  public void testSqlField07() throws KettleSQLException {
    String fieldClause = "SUM( B ) as total";

    testSqlFieldWithAggregationEqSum( fieldClause, "B", "total" );
  }

  @Test
  public void testSqlField07Alias() throws KettleSQLException {
    String fieldClause = "SUM( Service.B ) as total";

    testSqlFieldWithAggregationEqSum( fieldClause, "B", "total" );
  }

  @Test
  public void testSqlField08() throws KettleSQLException {
    String fieldClause = "SUM( \"B\" ) as total";

    testSqlFieldWithAggregationEqSum( fieldClause, "B", "total" );
  }

  @Test
  public void testSqlField08Alias() throws KettleSQLException {
    String fieldClause = "SUM( Service.\"B\" ) as total";

    testSqlFieldWithAggregationEqSum( fieldClause, "B", "total" );
  }

  @Test
  public void testSqlField09() throws KettleSQLException {
    String fieldClause = "SUM(\"B\") as   \"total\"";

    testSqlFieldWithAggregationEqSum( fieldClause, "B", "total" );
  }

  @Test
  public void testSqlField09Alias() throws KettleSQLException {
    String fieldClause = "SUM(Service.\"B\") as   \"total\"";

    testSqlFieldWithAggregationEqSum( fieldClause, "B", "total" );
  }


  @Test
  public void testSqlField10() throws KettleSQLException {
    String fieldClause = "COUNT(*) as   \"Number of lines\"";

    testSqlFieldWithAggregationEqCount( fieldClause, "*", "Number of lines" );
  }

  @Test
  public void testSqlField10NoAlias() throws KettleSQLException {
    String fieldClause = "COUNT(*)";

    testSqlFieldWithAggregationEqCount( fieldClause, "*", "COUNT(*)" );
  }

  @Test
  public void testSqlField10Alias() throws KettleSQLException {
    String fieldClause = "COUNT(Service.*) as   \"Number of lines\"";

    testSqlFieldWithAggregationEqCount( fieldClause, "*", "Number of lines" );
  }

  @Test
  public void testSqlField11() throws KettleSQLException {
    RowMetaInterface rowMeta = SQLTest.generateTest2RowMeta();

    String fieldClause = "COUNT(DISTINCT A) as   \"Number of customers\"";

    SQLField field = new SQLField( "Service", fieldClause, rowMeta );
    assertEquals( "A", field.getName() );
    assertEquals( "Number of customers", field.getAlias() );
    assertEquals( SQLAggregation.COUNT, field.getAggregation() );
    assertNotNull( "The service data type was not discovered", field.getValueMeta() );
    assertTrue( field.isCountDistinct() );
  }

  @Test
  public void testSqlField11Alias() throws KettleSQLException {
    RowMetaInterface rowMeta = SQLTest.generateTest2RowMeta();

    String fieldClause = "COUNT(DISTINCT Service.A) as   \"Number of customers\"";

    SQLField field = new SQLField( "Service", fieldClause, rowMeta );
    assertEquals( "A", field.getName() );
    assertEquals( "Number of customers", field.getAlias() );
    assertEquals( SQLAggregation.COUNT, field.getAggregation() );
    assertNotNull( "The service data type was not discovered", field.getValueMeta() );
    assertTrue( field.isCountDistinct() );
  }

  @Test
  public void testSqlField12_Function() throws KettleSQLException {
    String fieldClause = "IIF( B>5000, 'Big', 'Small' ) as \"Sales size\"";

    testSqlFieldWithIIF( fieldClause, "B", ">", "5000", "IIF( B>5000, 'Big', 'Small' )", "Sales size" );
  }

  @Test
  public void testSqlField12Alias_Function() throws KettleSQLException {
    String fieldClause = "IIF( Service.B>5000, 'Big', 'Small' ) as \"Sales size\"";

    testSqlFieldWithIIF( fieldClause, "B", ">", "5000", "IIF( Service.B>5000, 'Big', 'Small' )", "Sales size" );
  }

  @Test
  public void testSqlField13_Function() throws KettleSQLException {
    String fieldClause = "IIF( B>50, 'high', 'low' ) as nrSize";

    testSqlFieldWithIIF( fieldClause, "B", ">", "50", "IIF( B>50, 'high', 'low' )", "nrSize" );
  }

  @Test
  public void testSqlField13Alias_Function() throws KettleSQLException {
    String fieldClause = "IIF( Service.B>50, 'high', 'low' ) as nrSize";

    testSqlFieldWithIIF( fieldClause, "B", ">", "50", "IIF( Service.B>50, 'high', 'low' )", "nrSize" );
  }

  @Test
  public void testSqlFieldConstants01() throws KettleSQLException {
    RowMetaInterface rowMeta = SQLTest.generateTest2RowMeta();

    String fieldClause = "1";

    SQLField field = new SQLField( "Service", fieldClause, rowMeta );
    assertEquals( "1", field.getName() );
    assertNull( field.getAlias() );
    assertNotNull( "The service data type was not discovered", field.getValueMeta() );
    assertEquals( field.getValueMeta().getType(), ValueMetaInterface.TYPE_INTEGER );
    assertEquals( 1L, field.getValueData() );
  }

  @Test
  public void testSqlFieldConstants02() throws KettleSQLException {
    RowMetaInterface rowMeta = SQLTest.generateTest2RowMeta();

    String fieldClause = "COUNT(1)";

    SQLField field = new SQLField( "Service", fieldClause, rowMeta );
    assertEquals( "1", field.getName() );
    assertEquals( "COUNT(1)", field.getAlias() );
    assertEquals( SQLAggregation.COUNT, field.getAggregation() );
    assertEquals( 1L, field.getValueData() );
    assertNotNull( "The service data type was not discovered", field.getValueMeta() );
    assertEquals( field.getValueMeta().getType(), ValueMetaInterface.TYPE_INTEGER );
    assertEquals( 1L, field.getValueData() );
  }

  /**
   * Mondrian generated CASE WHEN <condition> THEN true-value ELSE false-value END
   *
   * @throws KettleSQLException
   */
  @Test
  public void testSqlFieldCaseWhen01() throws KettleSQLException {
    RowMetaInterface rowMeta = SQLTest.generateServiceRowMeta();

    String fieldClause = "CASE WHEN \"Service\".\"Category\" IS NULL THEN 1 ELSE 0 END";

    SQLField field = new SQLField( "Service", fieldClause, rowMeta );
    assertEquals( "CASE WHEN \"Service\".\"Category\" IS NULL THEN 1 ELSE 0 END", field.getName() );
    assertNull( field.getAlias() );
    assertNotNull( field.getIif() );
    assertEquals( "\"Service\".\"Category\" IS NULL", field.getIif().getConditionClause() );
    assertEquals( 1L, field.getIif().getTrueValue().getValueData() );
    assertEquals( 0L, field.getIif().getFalseValue().getValueData() );
  }

}
