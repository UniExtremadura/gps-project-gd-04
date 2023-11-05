import com.google.gson.annotations.SerializedName
import es.unex.giis.asee.gepeto.data.api.Ingredient

data class IngredientList (
    @SerializedName("ingredients") var ingredients : ArrayList<Ingredient> = arrayListOf()
)