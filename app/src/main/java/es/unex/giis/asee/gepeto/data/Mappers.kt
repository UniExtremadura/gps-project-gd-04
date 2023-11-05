package es.unex.giis.asee.gepeto.data

import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.data.api.Ingredient
import es.unex.giis.asee.gepeto.data.api.Meal
import es.unex.giis.asee.gepeto.model.Ingrediente
import es.unex.giis.asee.gepeto.model.Receta

fun Meal.toShowMeal() = Receta(
    idReceta = idMeal ?: "",
    nombre = strMeal ?: "",
    descripcion = strInstructions ?: "",
    favorita = false,
    ingredientes = listOf(
        strIngredient1 ?: "",
        strIngredient2 ?: "",
        strIngredient3 ?: "",
        strIngredient4 ?: "",
        strIngredient5 ?: "",
        strIngredient6 ?: "",
        strIngredient7 ?: "",
        strIngredient8 ?: "",
        strIngredient9 ?: "",
        strIngredient10 ?: "",
        strIngredient11 ?: "",
        strIngredient12 ?: "",
        strIngredient13 ?: "",
        strIngredient14 ?: "",
        strIngredient15 ?: "",
        strIngredient16 ?: "",
        strIngredient17 ?: "",
        strIngredient18 ?: "",
        strIngredient19 ?: "",
        strIngredient20 ?: ""
    ),
    equipamientos = listOf(
        strMeasure1 ?: "",
        strMeasure2 ?: "",
        strMeasure3 ?: "",
        strMeasure4 ?: "",
        strMeasure5 ?: "",
        strMeasure6 ?: "",
        strMeasure7 ?: "",
        strMeasure8 ?: "",
        strMeasure9 ?: "",
        strMeasure10 ?: "",
        strMeasure11 ?: "",
        strMeasure12 ?: "",
        strMeasure13 ?: "",
        strMeasure14 ?: "",
        strMeasure15 ?: "",
        strMeasure16 ?: "",
        strMeasure17 ?: "",
        strMeasure18 ?: "",
        strMeasure19 ?: "",
        strMeasure20 ?: ""
    ),
    imagen = R.drawable.ejemplo_plato,
    imagenPath = strMealThumb ?: ""
)

fun Ingredient.toShowIngredients() = Ingrediente(
    idIngrediente = idIngredient ?: "",
    nombre = strIngredient ?: "",
    descripcion = strDescription ?: ""
)