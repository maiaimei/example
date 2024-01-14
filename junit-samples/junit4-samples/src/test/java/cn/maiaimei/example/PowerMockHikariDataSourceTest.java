package cn.maiaimei.example;

import static org.junit.Assert.assertNull;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PrepareForTest(HikariDataSource.class)
@RunWith(PowerMockRunner.class)
public class PowerMockHikariDataSourceTest {

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = RuntimeException.class)
  public void test_getConnection_exception() {
    DBUtils.getConnection();
  }

  @Test
  public void test_getConnection_success() throws Exception {
    final HikariDataSource mockHikariDataSource = PowerMockito.mock(HikariDataSource.class);
    PowerMockito.whenNew(HikariDataSource.class).withAnyArguments()
        .thenReturn(mockHikariDataSource);
    final Connection connection = DBUtils.getConnection();
    assertNull(connection);
  }
}
