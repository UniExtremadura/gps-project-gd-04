package es.unex.giis.asee.gepeto.data

import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.data.api.Ingredient
import es.unex.giis.asee.gepeto.data.api.RecipesItem
import es.unex.giis.asee.gepeto.model.Ingrediente
import es.unex.giis.asee.gepeto.model.Receta

fun RecipesItem.toShowRecipe() = Receta(
    /*
    val idReceta: String,
    val nombre: String,
    val descripcion: String,
    var favorita: Boolean,
    val ingredientes: List<String> = emptyList(),
    val equipamientos: List<String> = emptyList(),
    val imagen: Int,
    val imagenPath: String
     */
    idReceta = id.toString(),
    nombre = title ?: "",
    descripcion = "",
    favorita = false,
    //apend de usedIngredients, missedIngredients, unusedIngredients
    ingredientes = usedIngredients.map { it.name } + missedIngredients.map { it.name },
    equipamientos = emptyList(),
    imagen = R.drawable.plato_ejemplo,
    imagenPath = image ?: ""
)

fun Ingredient.toShowIngredients() = Ingrediente(
    nombre = strIngredient ?: "",
)