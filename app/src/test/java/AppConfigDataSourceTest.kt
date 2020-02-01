import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.nhaarman.mockitokotlin2.*
import io.jcasas.weatherdagger2example.domain.Units
import io.jcasas.weatherdagger2example.domain.config.Configuration
import io.jcasas.weatherdagger2example.domain.config.NetworkStatus
import io.jcasas.weatherdagger2example.framework.config.AppConfigDataSource
import io.jcasas.weatherdagger2example.util.Constants
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class AppConfigDataSourceTest {

    private lateinit var SUT: AppConfigDataSource
    private lateinit var connectivityMgrMock: ConnectivityManager
    private lateinit var sharedPrefsMock: SharedPreferences
    private lateinit var networkInfoMock: NetworkInfo

    @Before
    fun setup() {
        connectivityMgrMock = mock()
        sharedPrefsMock = mock()
        networkInfoMock = mock()

        SUT = AppConfigDataSource(sharedPrefsMock, connectivityMgrMock)
    }

    @Test
    fun getConfiguration() {
        argumentCaptor<String> {
            SUT.getConfiguration()
            verify(sharedPrefsMock, times(1)).getString(capture(), capture())
            assertEquals(Constants.Keys.UNITS_KEY, firstValue)
            assertEquals(Constants.Values.UNITS_SI, secondValue)
        }
    }

    @Test
    fun getNetworkStatus() {
        networkInfoNull()
        var networkStatus = SUT.getNetworkStatus()
        verify(connectivityMgrMock, times(1)).activeNetworkInfo
        verify(networkInfoMock, times(0)).isConnected
        assertEquals(NetworkStatus.NOT_CONNECTED, networkStatus)
        networkInfoNotNull()
        notConnected()
        networkStatus = SUT.getNetworkStatus()
        verify(connectivityMgrMock, times(2)).activeNetworkInfo
        verify(networkInfoMock, times(1)).isConnected
        assertEquals(NetworkStatus.NOT_CONNECTED, networkStatus)
        connected()
        networkStatus = SUT.getNetworkStatus()
        verify(connectivityMgrMock, times(3)).activeNetworkInfo
        verify(networkInfoMock, times(2)).isConnected
        assertEquals(NetworkStatus.CONNECTED, networkStatus)
    }

    private fun networkInfoNull() {
        whenever(connectivityMgrMock.activeNetworkInfo).thenReturn(null)
    }

    private fun networkInfoNotNull() {
        whenever(connectivityMgrMock.activeNetworkInfo).thenReturn(networkInfoMock)
    }

    private fun notConnected() {
        whenever(networkInfoMock.isConnected)
                .thenReturn(false)
    }

    private fun connected() {
        whenever(networkInfoMock.isConnected).thenReturn(true)
    }
}