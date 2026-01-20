import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertNotEquals

class FirstTest {
    @Test
    fun test() {
        assertNotEquals(5, 2 + 2)

        assertEquals(5, 2 + 3)
    }
}