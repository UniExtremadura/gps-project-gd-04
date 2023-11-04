import com.google.gson.annotations.SerializedName
import es.unex.giis.asee.gepeto.data.api.Meal

data class MealList (

    @SerializedName("meals") var meals : ArrayList<Meal> = arrayListOf()

)