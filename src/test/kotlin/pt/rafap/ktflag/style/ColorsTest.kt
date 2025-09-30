package pt.rafap.ktflag.style

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ColorsTest {
    @Test
    fun colorText_appliesStylesAndReset() {
        val text = "Hello"
        val colored = Colors.colorText(text, Colors.BOLD, Colors.GREEN)
        assertTrue(colored.startsWith(Colors.BOLD + Colors.GREEN))
        assertTrue(colored.endsWith(Colors.RESET))
        assertTrue(colored.contains(text))
    }

    @Test
    fun colorText_withoutStylesStillAddsReset() {
        val text = "World"
        val colored = Colors.colorText(text)
        assertEquals(text + Colors.RESET, colored)
    }
}