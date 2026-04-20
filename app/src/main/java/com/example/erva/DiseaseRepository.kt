package com.example.erva

data class Disease(
    val name: String,
    val description: String,
    val solution: String
)

object DiseaseRepository {
    private val diseases = mapOf(
        "Bacterial Spot" to Disease(
            "Bacterial Spot",
            "Bacterial spot is a common disease that affects a wide variety of plants. It is caused by several species of bacteria in the genus Xanthomonas. The disease is characterized by small, water-soaked spots on the leaves, which eventually turn dark and necrotic.",
            "Remove and destroy infected plant parts. Avoid overhead watering and improve air circulation around the plants. Apply copper-based bactericides as a preventive measure."
        ),
        "Leaf Mold" to Disease(
            "Leaf Mold",
            "Leaf mold is a fungal disease caused by Passalora fulva. It primarily affects tomatoes grown in greenhouses or in humid conditions. The disease appears as pale green or yellowish spots on the upper surface of leaves, with a velvety, olive-green mold on the underside.",
            "Improve air circulation and reduce humidity. Water at the base of the plants to keep the foliage dry. Apply fungicides containing chlorothalonil or mancozeb."
        ),
        "Late Blight" to Disease(
            "Late Blight",
            "Late blight is a destructive disease of potatoes and tomatoes caused by the water mold Phytophthora infestans. It thrives in cool, wet weather. Symptoms include large, dark brown to black lesions on leaves and stems, often with a white, downy mold on the underside of the leaves.",
            "Destroy infected plants immediately to prevent the disease from spreading. Apply fungicides proactively, especially during cool, wet weather. Plant resistant varieties if available."
        )
    )

    fun getDisease(name: String): Disease? {
        return diseases[name]
    }
}
