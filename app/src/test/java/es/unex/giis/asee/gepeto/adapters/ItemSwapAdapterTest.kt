package es.unex.giis.asee.gepeto.adapters

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.TreeSet

class ItemSwapAdapterTest {

private lateinit var itemSwapAdapter: ItemSwapAdapter

    private lateinit var itemSet: TreeSet<String>
    @Mock
    private lateinit var onClickMock: (item: String) -> Unit
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        itemSet = TreeSet<String>().apply {
            add("Item1")
            add("Item2")
            add("Item3")
        }
        itemSwapAdapter = ItemSwapAdapter(itemSet, onClickMock)
    }


    @Test
    fun getItemCount() {
        val itemCount = itemSwapAdapter.itemCount

        assertEquals(itemSet.size, itemCount)
    }

    @Test
    fun getSet() {
        val retrievedSet = itemSwapAdapter.getSet()

        assertEquals(itemSet, retrievedSet)
    }

}