package pt.rafap.ktflag.style

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ColorsTest {
    /**
     * Verifies that colorText applies the given styles and ends with a reset code.
     */
    @Test
    fun colorText_appliesStylesAndReset() {
        val text = "Hello"
        val colored = Colors.colorText(text, Colors.BOLD, Colors.GREEN)
        assertTrue(colored.startsWith(Colors.BOLD + Colors.GREEN))
        assertTrue(colored.endsWith(Colors.RESET))
        assertTrue(colored.contains(text))
    }

    /**
     * Checks that colorText without styles still adds the reset code to the output.
     */
    @Test
    fun colorText_withoutStylesStillAddsReset() {
        val text = "World"
        val colored = Colors.colorText(text)
        assertEquals(text + Colors.RESET, colored)
    }
}