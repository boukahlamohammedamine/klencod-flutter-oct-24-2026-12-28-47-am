package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.util.AlgerianCurrencyUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ميزانيتي", appName)
    }

    @Test
    fun `test algerian currency conversion`() {
        // 50,000 DZD should be 5,000,000 Centimes (5 ملايين)
        val centimes = AlgerianCurrencyUtil.dzdToCentimes(50000.0)
        assertEquals(5_000_000L, centimes)

        val spoken = AlgerianCurrencyUtil.toAlgerianSpokenCentimes(50000.0)
        assertTrue(spoken.contains("ملايين"))

        // Test 50/30/20 split
        val split = AlgerianCurrencyUtil.calculate503020Split(60000.0)
        assertEquals(30000.0, split.necessitiesDzd, 0.01)
        assertEquals(18000.0, split.lifestyleDzd, 0.01)
        assertEquals(12000.0, split.savingsDzd, 0.01)
    }
}
